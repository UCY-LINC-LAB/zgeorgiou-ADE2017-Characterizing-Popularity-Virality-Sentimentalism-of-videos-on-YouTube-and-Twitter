package com.zgeorg03.core;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zgeorg03.utils.YoutubeTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;

/**
 * Created by zgeorg03 on 2/24/17.
 */
public class VideoStreamListener implements StatusListener {
    private static final Logger logger = LoggerFactory.getLogger(VideoStreamListener.class);
    @Override
    public void onStatus(Status status) {
        URLEntity urls[] = status.getURLEntities();

        // No urls found
        if (urls.length == 0)
            return;

        for (URLEntity url : urls) {
            String expandedUrl = url.getExpandedURL();
            //Make sure that is a youtube link
            if(!YoutubeTool.applyYoutubeFilter(expandedUrl))
               continue;
            String videoId = YoutubeTool.extractID(expandedUrl);

            Place place = status.getPlace();
            if(place!=null) {
                GeoLocation[][] box = status.getPlace().getBoundingBoxCoordinates();
                if (box != null) {
                    JsonObject tweet = new JsonObject();
                    double y1 = box[0][0].getLatitude();
                    double y2 = box[0][2].getLatitude();
                    double x1 = box[0][0].getLongitude();
                    double x2 = box[0][2].getLongitude();
                    String name = place.getName();
                    String country = place.getCountry();
                    BoundingBox boundingBox = new BoundingBox(y1,y2,x1,x2);
                    WGS84Point point = boundingBox.getCenterPoint();
                    GeoHash hash = GeoHash.withCharacterPrecision(point.getLatitude(),point.getLongitude(),3);
                    tweet.addProperty("box",hash.toBase32());
                    tweet.addProperty("video_id",videoId);
                    tweet.addProperty("id",status.getId());
                    tweet.addProperty("created_at",status.getCreatedAt().getTime());
                    tweet.addProperty("place",place.getName());
                    tweet.addProperty("country_code",place.getCountryCode());
                    tweet.addProperty("text",status.getText());
                    tweet.addProperty("favorite_count",status.getFavoriteCount());
                    tweet.addProperty("lang",status.getLang());
                    tweet.addProperty("retweet_count",status.getRetweetCount());
                    tweet.addProperty("source",status.getSource());
                    tweet.addProperty("is_favored",status.isFavorited());
                    tweet.addProperty("is_possibly_sensitive",status.isPossiblySensitive());
                    tweet.addProperty("is_retweet",status.isRetweet());
                    User user = status.getUser();
                    tweet.addProperty("user_created_at",user.getCreatedAt().getTime());
                    tweet.addProperty("user_id",user.getId());
                    tweet.addProperty("user_followers_count",user.getFollowersCount());
                    tweet.addProperty("user_friends_count",user.getFriendsCount());
                    tweet.addProperty("user_favorites_count",user.getFavouritesCount());
                    tweet.addProperty("user_listed_count",user.getListedCount());
                    tweet.addProperty("user_statuses_count",user.getStatusesCount());
                    tweet.addProperty("user_verified",user.isVerified());
                    tweet.addProperty("user_lang",user.getLang());
                    JsonArray hashtags = new JsonArray();
                    for(HashtagEntity hashtagEntity :status.getHashtagEntities()){
                        hashtags.add(hashtagEntity.getText());
                    }
                    tweet.add("hashtags",hashtags);

                    /**
                    //TODO
                     1. Check first that the video exists in the database

                     2. If it exists, its fine we don't need to do anything at all

                     3. If it doesn't exist, we must create it! We need to import static data first and set a flag
                     so a MonitorService will find it and gather its dynamic data once per day.

                     **/
                    System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(tweet));
                }
            }

        }

    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

        logger.info("DEL:"+statusDeletionNotice.getStatusId());
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

        logger.info("LIMIT:"+numberOfLimitedStatuses);
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        logger.info("SCRUB:"+userId);

    }

    @Override
    public void onStallWarning(StallWarning warning) {

        logger.info("STALL:"+warning.getMessage());
    }

    @Override
    public void onException(Exception ex) {
        logger.error(ex.getLocalizedMessage());

    }
}
