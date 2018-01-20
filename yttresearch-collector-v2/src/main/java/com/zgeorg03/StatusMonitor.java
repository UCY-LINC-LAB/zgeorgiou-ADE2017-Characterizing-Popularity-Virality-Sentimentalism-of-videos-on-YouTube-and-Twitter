package com.zgeorg03;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.utils.YoutubeTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class StatusMonitor implements Runnable{
    private final Logger logger = LoggerFactory.getLogger(StatusMonitor.class);
    private final Queue<Status> statusQueue = new ConcurrentLinkedDeque<>();
    private final DBServices dbServices;
    private final VideoStreamListener videoStreamListener;
    private final TwitterStream twitterStream;
    private final Arguments arguments;
    private final Map<String,Boolean> apps = new HashMap<>();

    private long startedTime;

    private final Stats stats;

    private boolean reachedMonitorCapacity;


    public StatusMonitor(Arguments arguments, DBServices dbServices) {
        this.stats = new Stats();
        this.arguments = arguments;
        this.dbServices = dbServices;
        this.videoStreamListener = new VideoStreamListener(this, stats);
        twitterStream = new TwitterStreamFactory().getInstance();
    }


    @Override
    public void run() {
        init();
        long sleepTime = 100;
        long last=0;

        while (true){
            long now = System.currentTimeMillis();
            Status status;
            status = statusQueue.poll();
            if(status==null) {
                try { TimeUnit.MILLISECONDS.sleep(sleepTime); } catch (InterruptedException e) { e.printStackTrace();}
                continue;
            }

            //Process Status
            processStatus(status);


            //We processed one here
            stats.addProcessedStatus();
            stats.setAverageQueueSize(statusQueue.size());

            if(arguments.isDebug_mode()) {
                if (now - last > 10000) {
                    System.out.println(stats.toCsv());
                    last = now;
                }
            }
        }
    }

    private void processStatus(Status status) {
        boolean videoToBeAdded = false;
        JsonObject videoData = null;

        boolean isRetweet = status.isRetweet();
        Status quoted = status.getQuotedStatus();

        //Just for Stats
        if (isRetweet)
            stats.addRetweet();
        else if ( quoted!= null)
            stats.addQuote();
        else
            stats.addOriginalTweet();


        // Collect urls if any
        URLEntity urls[] = status.getURLEntities();
        if (urls.length == 0)
            return;

        //Collect the video if any
        String video = null;
        for (URLEntity url : urls) {
            String expandedUrl = url.getExpandedURL();
            //Make sure that is a youtube link
            if (!YoutubeTool.applyYoutubeFilter(expandedUrl))
                continue;
            video = YoutubeTool.extractID(expandedUrl);
            break;
        }

        // No video found
        if(video==null)
            return;

        //We found a link to a youtube video. First, the easy case. The one that the video doesn't exist
        // If it does exist, it may be in the following states:
        // monitored:  True when the video is active
        // finished:
        // incomplete:
        // comments_finished:
        boolean videoExists = dbServices.getDbVideosService().checkVideoIfExistsInDB(video);
        boolean videoAdded = false;


        // If video doesn't exist, and we didn't reached the capacity yet
        // so we are able to add a new video
        if(!videoExists && !reachedMonitorCapacity) {

            // We only add the video if we didn't reach our capacity
            // If we reached the capacity, then the tweet is useless
            if (dbServices.getDbVideosService().getTotalMonitoredVideosAndNotFinished() >= dbServices.getMaxVideosBeingMonitored()) {
                reachedMonitorCapacity = true;
                logger.info("Reached Monitoring videos capacity");
                return;
            }

            // Collect a key first, so we can add the video
            String key = dbServices.getYouTubeAPIKey();
            if (key.isEmpty()) {
                if(arguments.isDebug_mode()) {
                    key = "AIzaSyCSC05BGk8RIdoHBT81RP4CWj2SnLFoHnA";
                }else {
                    logger.error("No YouTube key available, to add the new video");
                    return;
                }
            }

            // Then we check if the static data can be fetched
            YouTubeRequests requests = new YouTubeRequests(video, key);
            videoData = requests.getStaticData();
            if (videoData.get("error") != null) {
                logger.info("Cannot add Video: " + video + " because of " + videoData.get("error").getAsString());
                return;
            }

            // The data is fetched. The next step is to add the video to the database
            if(isRetweet || quoted!=null){
                videoToBeAdded=true;
                logger.info("Video: " + video + " will be added to DB");
            }else {
                videoAdded = dbServices.getDbVideosService().addNewVideo(video, videoData);
                if (!videoAdded) {
                    logger.info("Video: " + video + " was fetched but couldn't be added, to the DB!");
                    return;
                } else {
                    logger.info("Video: " + video + " added to DB");
                }
            }

        } else if ( !videoExists && reachedMonitorCapacity){
            // The case where we can't handle a new video
            return;
        }

        // For the rest block of code we have the video already in the database

        // However it could have finished, or be incomplete...

        // If it is not active, thus being monitored we finish
        // videoAdded is checked first for performance reasons...
        if(!videoAdded
                && dbServices.getDbVideosService().checkVideoIfExistsInDB(video)
                && !dbServices.getDbVideosService().checkVideoExistenceAndBeingMonitored(video)){
            return;
        }

        // Collect all information from the current status
        JsonObject tweet = getStatusInfo(status, video);

        // If current status is a retweet
        if(isRetweet) {
            Status original = status.getRetweetedStatus();
            addRetweetOrQuoted(tweet,status,original,video,"Retweet",videoToBeAdded,videoData);
            stats.addVideoRetweet();
        }else if( quoted!=null){
            addRetweetOrQuoted(tweet,status,quoted, video,"Quoted", videoToBeAdded,videoData);
            stats.addVideoQuoted();
        }else {

            //Original - the one we caught
            // We should add the original to the database
            boolean added = dbServices.addTweet(status.getId(), tweet);

            if (added) {
                logger.info("Tweet added for video: " + video);
                stats.addVideoOriginal();
            } else {
                logger.info("Failed to add the original tweet to db for video: " + video);
                return;
            }


        }

    }

    public void addRetweetOrQuoted(JsonObject tweet, Status status, Status original, String video,String log,boolean videoToBeAdded, JsonObject videoData){

        URLEntity urls[] = original.getURLEntities();
        // No urls found
        if (urls.length == 0) {
            logger.info("Original tweet do not mention any video");
            return;
        }

        // Check if we have the original tweet in the database
        if (dbServices.containsTweet(original.getId())) {

            // If it is already in, then we add just the retweet, thus the status
            boolean added =  dbServices.addTweet(status.getId(), tweet);
            if(added) {
                logger.info("Since we had the original tweet, "+log+" only added for video: "+video);
                return;
            }else {
                logger.info("Failed to add the "+log+" to db for video: " + video);
                return;
            }


        } else {

            // The original tweet is not in the database. This means that the video was taken from a retweet
            // We need to check if the original is in the dates of observation
            long timestamp =  dbServices.getDbVideosService().getVideoTimestamp(video);
            long original_timestamp = original.getCreatedAt().getTime();

            // If the original was before the time we caught the video, it is useless
            // so we add only if it is after the timestamp of the video
            //System.out.println(new Date(original_timestamp)+">="+new Date(timestamp));
            if(original_timestamp>=timestamp){

                // We should add the original to the database
                JsonObject originalTweet = getStatusInfo(original,video);
                boolean added =  dbServices.addTweet(original.getId(),originalTweet);

                if(added) {
                    logger.info("The original tweet is added"+video);
                    stats.addVideoOriginal();

                    boolean videoAdded = dbServices.getDbVideosService().addNewVideo(video, videoData);
                    if (!videoAdded) {
                        logger.info("Video: " + video + " was fetched but couldn't be added, to the DB!");
                        return;
                    } else {
                        logger.info("Video: " + video + " added to DB");
                    }

                    // We need to add the retweet, also
                    added =  dbServices.addTweet(status.getId(), tweet);
                    if(added) {
                        logger.info(log+" also added only added for video: "+video);
                        return;
                    }else {
                        logger.info("Failed to add the "+log+" to db for video: " + video);
                        return;
                    }

                }else {
                    logger.info("Failed to add the original tweet to db for video: " + video);
                    //So, we don't try to add the retweet
                    return;
                }

            }else{
                //This is the case where the original tweet was published before the time
                // we have collected the video, thus we don't do anything...
                return;
            }
        }

    }
    public JsonObject getStatusInfo(Status status, String video){
        JsonObject tweet = new JsonObject();
        long id = status.getId();

        tweet.addProperty("status_id", id);
        tweet.addProperty("video_id", video);
        tweet.addProperty("created_at", status.getCreatedAt().getTime());
        tweet.addProperty("text",status.getText());
        tweet.addProperty("favorite_count",status.getFavoriteCount());
        tweet.addProperty("lang",status.getLang());
        tweet.addProperty("retweet_count",status.getRetweetCount());
        tweet.addProperty("is_favorited",status.isFavorited());
        tweet.addProperty("is_possibly_sensitive",status.isPossiblySensitive());
        tweet.addProperty("is_retweet",status.isRetweet());

        User user = status.getUser();
        tweet.addProperty("user_id",user.getId());
        tweet.addProperty("user_created_at",user.getCreatedAt().getTime());
        tweet.addProperty("user_followers_count",user.getFollowersCount());
        tweet.addProperty("user_friends_count",user.getFriendsCount());
        tweet.addProperty("user_favorites_count",user.getFavouritesCount());
        tweet.addProperty("user_listed_count",user.getListedCount());
        tweet.addProperty("user_statuses_count",user.getStatusesCount());
        tweet.addProperty("user_verified",user.isVerified());
        tweet.addProperty("user_lang",user.getLang());

        JsonArray hashtags = new JsonArray();
        for(HashtagEntity hashtagEntity :status.getHashtagEntities())
            hashtags.add(hashtagEntity.getText());
        tweet.add("hashtags",hashtags);

        return tweet;
    }
    public void processOriginalTweet(JsonObject tweet, String videoId){
        tweet.addProperty("video_id", videoId);
        System.out.println(tweet);

    }

    public void init() {
        JsonObject app;
        while (true) {
            app = dbServices.getTwitterAppForUse();
            if (app.get("error") != null) {
                try {
                    logger.info("Twitter apps not found! Sleeping for 30 seconds...");

                    if (arguments.isDebug_mode()) {
                        app.addProperty("name", "debug");
                        app.addProperty("consumer_key", "PY0bCZZ9xwHSKWyHwZodq5Fyz");
                        app.addProperty("consumer_secret", "IJibNf3lckPHcnoG6ece29LEaocaJbnyXNJ9Nj501c5VpCLC8v");
                        app.addProperty("token", "724557665578287105-LDXQEcFbkczxses2og35a8U0vQhRZNY");
                        app.addProperty("token_secret", "ZmTVm8UQSfGwdrqWRuvpOdd0E5iSOGRxMpnKVjRJB7JMT");
                        apps.put("debug", true);
                        logger.info("Using twitter keys for Debug mode");
                        break;
                    }
                    Thread.sleep(30000);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }

        }
        String consumer_key = app.get("consumer_key").getAsString();
        String consumer_secret = app.get("consumer_secret").getAsString();
        String token = app.get("token").getAsString();
        String token_secret = app.get("token_secret").getAsString();

        twitterStream.setOAuthConsumer(consumer_key, consumer_secret);
        twitterStream.setOAuthAccessToken(new AccessToken(token, token_secret));
        twitterStream.addListener(videoStreamListener);
        twitterStream.filter("youtube");

        startedTime = System.currentTimeMillis();
    }

    public void  enqueue(Status status) {
        statusQueue.add(status);
    }
    public void shutdown(){
        logger.info("Status monitor shutting down...");
        for(String name : apps.keySet())
            dbServices.releaseTwitterApp(name);
    }
    public void setReachedMonitorCapacity(boolean reachedMonitorCapacity) {
        this.reachedMonitorCapacity= reachedMonitorCapacity;
    }
    public int getQueueSize(){
        return statusQueue.size();
    }

    /**
     * Get Status duration
     * @return
     */
    public long getDuration(){
        if(startedTime==0)
            return 0;
        return System.currentTimeMillis()-startedTime;
    }

    public Stats getStats() {
        return stats;
    }
}
