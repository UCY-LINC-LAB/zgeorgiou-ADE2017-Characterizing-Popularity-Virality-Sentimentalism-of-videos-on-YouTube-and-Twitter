package com.zgeorg03.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

/**
 * Created by zgeorg03 on 2/25/17.
 */
public  class DBServices implements DBServicesI{
    private final Logger logger  = LoggerFactory.getLogger(DBServices.class);
    private final int default_max_videos_being_monitored= 5;
    private final int default_max_comments_per_video= 100;

    private final String databaseName;
    private final MongoCollection videos;
    private final MongoCollection comments;
    private final MongoCollection tweets;
    private final MongoCollection info;
    private final MongoCollection twitterApps;
    private final MongoCollection youtubeKeys;

    public DBServices(DBConnection connection) {
        videos = connection.getDatabase().getCollection("videos");
        tweets = connection.getDatabase().getCollection("tweets");
        comments = connection.getDatabase().getCollection("comments");
        info = connection.getDatabase().getCollection("info");
        twitterApps = connection.getDatabase().getCollection("twitterApps");
        youtubeKeys = connection.getDatabase().getCollection("youtubeKeys");
        this.databaseName = connection.getDatabase().getName();

        Document keys = new Document("video_id",1).append("_id",1);
        tweets.createIndex(keys);
        comments.createIndex(keys);

    }

    @Override
    public int getTotalMonitoredVideosAndNotFinished() {
        return (int) videos.count(
                and( eq("meta.monitored",true) ,eq("meta.finished",false)) );
    }

    @Override
    public boolean setVideoAsFinished(String video_id) {
        Document video =(Document) videos.find(eq("_id",video_id)).first();
        if(video==null){
            logger.error("Video not found:"+video_id);
            return false;
        }

        Document meta = (Document) video.get("meta");

        meta.put("finished",true);
        meta.put("monitored",false);

        video.put("meta",meta);
        videos.replaceOne(eq("_id",video_id),video);
        return true;
    }

    @Override
    public int getTotalFinishedVideos() {
        return (int) videos.count( and(
                eq("meta.finished",true),eq("meta.incomplete",false)));
    }

    @Override
    public List<String> getVideosThatNeedDynamicUpdate(long duration, TimeUnit unit) {
        long durationInMillis = unit.toMillis(duration);
        long time = System.currentTimeMillis();

        Collection documents = videos.find(eq("meta.monitored",true)).into(new LinkedList<Document>());
        List<String> videos = new LinkedList<>();

        for(Object obj : documents){
            Document doc = (Document)obj;
            String video_id = (String) doc.get("_id");

            long last_update = ((Document)doc.get("meta")).getLong("last_update");
            if(time-last_update>=durationInMillis)
                videos.add(video_id);
        }

        return videos;
    }

    @Override
    public Map<String,Integer> getVideosThatNeedComments(int maxComments) {
        Map<String,Integer> ids = new HashMap<>();
        MongoCursor<Document> cursor = videos.find(and(eq("meta.monitored",true),
                eq("meta.comments_finished",false)))
                .projection(fields(include("_id","days","meta"))).iterator();

        while(cursor.hasNext()){
            Document document  = cursor.next();
            String id= document.getString("_id");
            Document meta = (Document) document.get("meta");
            List<Document> days = (List<Document>) document.get("days");
            if(days.isEmpty())
                continue;
            long comments = days.get(days.size()-1).getLong("comment_count");
            long commentsCollected = meta.getLong("comments_collected");
            int commentsToReach = (int) Math.min(comments,maxComments);
            int remaining  = (int) (commentsToReach-commentsCollected);
            if(remaining==0)
                continue;
            ids.put(id,commentsToReach);

        }
        cursor.close();

        return ids;
    }

    @Override
    public boolean checkVideoExistenceAndBeingMonitored(String video_id) {
        return videos.count(and(eq("_id",video_id),eq("meta.monitored",true)))==1;
    }

    @Override
    public boolean checkVideoExistenceOnly(String video_id) {
        return videos.count(eq("_id",video_id))==1;
    }

    @Override
    public boolean checkVideoIsFinished(String video_id) {
        return videos.count(and(eq("_id",video_id),eq("meta.current_date",16)))==1;
    }

    @Override
    public boolean addNewVideo(String video_id,JsonObject videoObject) {
        Document document = new Document();


        try {
            document.append("_id",video_id);
            document.append("title",videoObject.get("title").getAsString());
            document.append("channel_id",videoObject.get("channel_id").getAsString());
            document.append("description",videoObject.get("description").getAsString());
            document.append("category_id",videoObject.get("category_id").getAsInt());
            document.append("published_at",videoObject.get("published_at").getAsLong());
            document.append("duration",videoObject.get("duration").getAsLong());

            Document meta = new Document("timestamp",System.currentTimeMillis())
                    .append("monitored",true)
                    .append("finished",false)
                    .append("incomplete",false)
                    .append("comments_collected",0L)
                    .append("comments_finished",false)
                    .append("last_update",-1L)
                    .append("current_date",0);
            JsonArray topicsArray = videoObject.get("topics").getAsJsonArray();
            List<String> topics = new LinkedList<>();
            for (JsonElement element : topicsArray)
                topics.add(element.getAsString());

            document.append("topics",topics);
            document.append("days",new LinkedList<>());
            document.append("meta",meta);
            videos.insertOne(document);
            return true;
        } catch(NullPointerException ex){
            logger.error("Missing fields from the object");
            return  false;
        } catch(MongoWriteException we){
            logger.error(we.getError().getMessage());
            return false;
        } catch(MongoException ex) {
            logger.error(ex.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public boolean addDynamicData(String video_id, JsonObject dynamicData) {
        Document video =(Document) videos.find(eq("_id",video_id)).first();

        if(video==null){
            logger.error("Video not found:"+video_id);
            return false;
        }
        List<Document> days = (List<Document>) video.get("days");
        Document meta = (Document) video.get("meta");
        int current_date = meta.getInteger("current_date");

        try {
            Document day;

            // If its the first day
            if(days.size()==0) {
                day = new Document()
                        .append("day", current_date)
                        .append("timestamp", dynamicData.get("timestamp").getAsLong())
                        .append("view_count", dynamicData.get("view_count").getAsLong())
                        .append("like_count", dynamicData.get("like_count").getAsLong())
                        .append("dislike_count", dynamicData.get("dislike_count").getAsLong())
                        .append("favorite_count", dynamicData.get("favorite_count").getAsLong())
                        .append("comment_count", dynamicData.get("comment_count").getAsLong())
                        .append("channel_view_count", dynamicData.get("channel_view_count").getAsLong())
                        .append("channel_comment_count", dynamicData.get("channel_comment_count").getAsLong())
                        .append("channel_subscriber_count", dynamicData.get("channel_subscriber_count").getAsLong())
                        .append("channel_video_count", dynamicData.get("channel_video_count").getAsLong());
            }else{
                day = new Document();
                Document last_day = days.get(days.size()-1);

                long ld_view_count = last_day.getLong("view_count");
                if(ld_view_count==-1)
                        day.append("view_count", dynamicData.get("view_count").getAsLong());
                else
                        day.append("view_count", dynamicData.get("view_count").getAsLong()-ld_view_count);

                long ld_like_count = last_day.getLong("like_count");
                if(ld_like_count==-1)
                    day.append("like_count", dynamicData.get("like_count").getAsLong());
                else
                    day.append("like_count", dynamicData.get("like_count").getAsLong()-ld_like_count);

                long ld_dislike_count = last_day.getLong("dislike_count");
                if(ld_dislike_count==-1)
                    day.append("dislike_count", dynamicData.get("dislike_count").getAsLong());
                else
                    day.append("dislike_count", dynamicData.get("dislike_count").getAsLong()-ld_dislike_count);

                long ld_favorite_count = last_day.getLong("favorite_count");
                if(ld_favorite_count==-1)
                    day.append("favorite_count", dynamicData.get("favorite_count").getAsLong());
                else
                    day.append("favorite_count", dynamicData.get("favorite_count").getAsLong()-ld_favorite_count);

                long ld_comment_count = last_day.getLong("comment_count");
                if(ld_comment_count==-1)
                    day.append("comment_count", dynamicData.get("comment_count").getAsLong());
                else
                    day.append("comment_count", dynamicData.get("comment_count").getAsLong()-ld_comment_count);

                long ld_channel_view_count = last_day.getLong("channel_view_count");
                if(ld_channel_view_count==-1)
                    day.append("channel_view_count", dynamicData.get("channel_view_count").getAsLong());
                else
                    day.append("channel_view_count", dynamicData.get("channel_view_count").getAsLong()-ld_channel_view_count);

                long ld_channel_comment_count = last_day.getLong("channel_comment_count");
                if(ld_channel_comment_count==-1)
                    day.append("channel_comment_count", dynamicData.get("channel_comment_count").getAsLong());
                else
                    day.append("channel_comment_count", dynamicData.get("channel_comment_count").getAsLong()-ld_channel_comment_count);

                long ld_channel_subscriber_count = last_day.getLong("channel_subscriber_count");
                if(ld_channel_subscriber_count==-1)
                    day.append("channel_subscriber_count", dynamicData.get("channel_subscriber_count").getAsLong());
                else
                    day.append("channel_subscriber_count", dynamicData.get("channel_subscriber_count").getAsLong()-ld_channel_subscriber_count);


            }
            days.add(day);
            meta.put("last_update",System.currentTimeMillis());
            meta.put("comments_finished",false); //Request new comments
            meta.put("current_date",current_date+1);
            video.put("days",days);
            video.put("meta",meta);
            videos.replaceOne(eq("_id",video_id),video);
            return true;
        } catch(NullPointerException ex){
            ex.printStackTrace();
            logger.error("Missing fields from the object");
            return  false;
        } catch(MongoWriteException we){
            logger.error(we.getError().getMessage());
            return false;
        } catch(MongoException ex) {
            logger.error(ex.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public boolean deleteVideo(String video_id) {
        try{
            return videos.deleteOne(eq("_id",video_id)).getDeletedCount() == 1;
        } catch(MongoWriteException we){
            logger.error(we.getError().getMessage());
            return false;
        } catch(MongoException ex) {
            logger.error(ex.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public int addComments(String video_id, JsonObject comments) {
        int count =0 ;
        if(comments.get("comments")==null) {
            Document doc = new Document("$set", new Document("meta.comments_finished", true));
            videos.updateOne(eq("_id", video_id), doc);
            return 0;
        }

        int size = comments.get("size").getAsInt();
        JsonArray commentsArray = comments.get("comments").getAsJsonArray();
        for(JsonElement element : commentsArray){
           JsonObject obj = element.getAsJsonObject();
           Document document = new Document()
                   .append("_id",obj.get("id").getAsString())
                   .append("video_id",video_id)
                   .append("published_at",obj.get("published_at").getAsLong())
                   .append("text",obj.get("text").getAsString())
                   .append("like_count",obj.get("like_count").getAsLong()) ;
            try{
                this.comments.insertOne(document);
                count++;
                Document doc = new Document("$inc",new Document("meta.comments_collected",1));
                videos.updateOne(eq("_id",video_id),doc);
            } catch(NullPointerException ex) {
                logger.error("Missing fields from the object");
            }catch (DuplicateKeyException ex ){
                logger.error(ex.getMessage());
            } catch(MongoWriteException we){
                logger.error(we.getError().getMessage());
            } catch(MongoException ex) {
                logger.error(ex.getLocalizedMessage());
            }
        }

        Document doc = new Document("$set",new Document("meta.comments_finished",true));
        videos.updateOne(eq("_id",video_id),doc);
        return count;
    }

    @Override
    public boolean addTweet(long tweetId, JsonObject tweet) {

        Document document = new Document();
        document.append("_id",tweetId)
                .append("video_id",tweet.get("video_id").getAsString())
                .append("created_at",tweet.get("created_at").getAsLong())
                .append("text",tweet.get("text").getAsString())
                .append("favorite_count",tweet.get("favorite_count").getAsLong())
                .append("lang",tweet.get("lang").getAsString())
                .append("retweet_count",tweet.get("retweet_count").getAsLong())
                .append("source",tweet.get("source").getAsString())
                .append("is_favorited",tweet.get("is_favorited").getAsBoolean())
                .append("is_possibly_sensitive",tweet.get("is_possibly_sensitive").getAsBoolean())
                .append("is_retweet",tweet.get("is_retweet").getAsBoolean())
                .append("user_created_at",tweet.get("user_created_at").getAsLong())
                .append("user_followers_count",tweet.get("user_followers_count").getAsLong())
                .append("user_friends_count",tweet.get("user_friends_count").getAsLong())
                .append("user_favorites_count",tweet.get("user_favorites_count").getAsLong())
                .append("user_listed_count",tweet.get("user_listed_count").getAsLong())
                .append("user_statuses_count",tweet.get("user_statuses_count").getAsLong())
                .append("user_verified",tweet.get("user_verified").getAsBoolean())
                .append("user_lang",tweet.get("user_lang").getAsString());

        List<String> hashtags = new LinkedList<>();
        JsonArray hash = tweet.getAsJsonArray("hashtags");
        for(JsonElement tag : hash)
            hashtags.add(tag.getAsString());

        document.append("hashtags",hashtags);

        try{
            tweets.insertOne(document);
        } catch(MongoWriteException we){
            logger.error(we.getError().getMessage());
            return false;
        } catch(MongoException ex) {
            logger.error(ex.getLocalizedMessage());
            return false;
        }

        return true;

    }

    @Override
    public boolean addYouTubeAPIKey(String key) {
        Document document = new Document();

        document.append("_id",key);
        document.append("created_at",System.currentTimeMillis());
        document.append("cost",0);

        try{
            youtubeKeys.insertOne(document);

        } catch(MongoWriteException we){
            logger.error(we.getError().getMessage());
            return false;
        } catch(MongoException ex) {
            logger.error(ex.getLocalizedMessage());
            return false;
        }
        return true;
    }

    @Override
    public String getYouTubeAPIKey() {
        Document sort  = new Document("cost",1);
        Document document = (Document) youtubeKeys.find().sort(sort).first();
        if(document==null){
            return "";
        }
        String key = document.getString("_id");
        try{
            youtubeKeys.updateOne(eq("_id",key), Updates.inc("cost",1));
        } catch(MongoWriteException we){
            logger.error(we.getError().getMessage());
            return "";
        } catch(MongoException ex) {
            logger.error(ex.getLocalizedMessage());
            return "";
        }
        return key;
    }

    @Override
    public int getTotalYouTubeAPIKeys() {
        return (int) youtubeKeys.count();
    }

    @Override
    public JsonObject getTwitterAppForUse() {
        Document document = (Document) twitterApps.find(eq("is_used",false)).first();
        JsonObject result = new JsonObject();
        if(document==null) {
            logger.info("No more free Twitter Apps");
            result.addProperty("error","No more free TwitterApps");
            return  result;
        }
        result.addProperty("name",document.getString("_id"));
        result.addProperty("consumer_key",document.getString("consumer_key"));
        result.addProperty("consumer_secret",document.getString("consumer_secret"));
        result.addProperty("token",document.getString("token"));
        result.addProperty("token_secret",document.getString("token_secret"));

        try{
            document.append("is_used",true);
            twitterApps.replaceOne(eq("_id",document.getString("_id")),document);
        } catch(MongoWriteException we){
            logger.error(we.getError().getMessage());
            result.addProperty("error","Couldn't set app to used");
            return  result;
        } catch(MongoException ex) {
            logger.error(ex.getLocalizedMessage());
            result.addProperty("error","Couldn't set app to used");
            return result;
        }
        return result;
    }

    @Override
    public boolean releaseTwitterApp(String name) {
        Document document = (Document) twitterApps.find(eq("_id",name)).first();
        if(document==null) {
            logger.error("Twitter app doesn't exist");
            return false;
        }
        boolean is_used = document.getBoolean("is_used");
        if(!is_used){
            logger.error("Twitter app is already free!");
            return false;
        }
        try{
            document.append("is_used",false);
            twitterApps.replaceOne(eq("_id",name),document);
        } catch(MongoWriteException we){
            logger.error(we.getError().getMessage());
            return  false;
        } catch(MongoException ex) {
            logger.error(ex.getLocalizedMessage());
            return false;
        }
        return true;
    }

    @Override
    public int getTotalTwitterApps() {
        return (int) twitterApps.count();
    }

    @Override
    public int getTotalFreeTwitterApps() {
        //TODO getTotalFreeTwitterApps
        return 0;
    }

    @Override
    public boolean addTwitterApp(String name,String consumer_key, String consumer_secret, String token, String token_secret) {
        Document document = new Document();
        document.append("_id",name);
        document.append("created_at",System.currentTimeMillis());
        document.append("is_used",false);
        document.append("consumer_key",consumer_key);
        document.append("consumer_secret",consumer_secret);
        document.append("token",token);
        document.append("token_secret",token_secret);

        try{
            twitterApps.insertOne(document);

        } catch(MongoWriteException we){
            logger.error(we.getError().getMessage());
            return false;
        } catch(MongoException ex) {
            logger.error(ex.getLocalizedMessage());
            return false;
        }
        return true;
    }

    @Override
    public JsonObject getStatistics() {
        //TODO
        return null;
    }

    @Override
    public JsonObject getConfiguration() {
        //TODO
        return null;
    }

    @Override
    public boolean setMaxVideosBeingMonitored(int max) {
        Document document = (Document) info.find(eq("_id","config")).first();
        try{
            if(document==null){
                logger.info("Creating configuration record");
                document = createDefaultConfiguration();
                document.append("max_videos_being_monitored",max);
                info.insertOne(document);
            }else {
                document.append("max_videos_being_monitored", max);
                info.replaceOne(eq("_id", "config"), document);
            }
        } catch(MongoWriteException we){
            logger.error(we.getError().getMessage());
            return false;
        } catch(MongoException ex) {
            logger.error(ex.getLocalizedMessage());
            return false;
        }
        return true;
    }

    @Override
    public int getMaxVideosBeingMonitored() {
        Document document = (Document) info.find(eq("_id","config")).first();
        if(document==null){
            logger.info("Creating configuration record");
            document =  createDefaultConfiguration();
            try{
                info.insertOne(document);
                return 10;
            } catch(MongoWriteException we){
                logger.error(we.getError().getMessage());
                return 10;
            } catch(MongoException ex) {
                logger.error(ex.getLocalizedMessage());
                return 10;
            }
        }
        return document.getInteger("max_videos_being_monitored");
    }

    @Override
    public int getMaxCommentsPerVideo() {
        Document document = (Document) info.find(eq("_id","config")).first();
        if(document==null){
            logger.info("Creating configuration record");
            document = createDefaultConfiguration();
            try{
                info.insertOne(document);
                return 10;
            } catch(MongoWriteException we){
                logger.error(we.getError().getMessage());
                return 10;
            } catch(MongoException ex) {
                logger.error(ex.getLocalizedMessage());
                return 10;
            }
        }
        return document.getInteger("max_comments_per_video");
    }

    @Override
    public boolean setMaxCommentsPerVideo(int max) {
        Document document = (Document) info.find(eq("_id","config")).first();
        try{
            if(document==null){
                logger.info("Creating configuration record");
                document = createDefaultConfiguration();
                document.append("max_comments_per_video",max);
                info.insertOne(document);
            }else {
                document.append("max_comments_per_video", max);
                info.replaceOne(eq("_id", "config"), document);
            }
        } catch(MongoWriteException we){
            logger.error(we.getError().getMessage());
            return false;
        } catch(MongoException ex) {
            logger.error(ex.getLocalizedMessage());
            return false;
        }
        return true;
    }


    @Override
    public String getChannelId(String video_id) {
        Document video =(Document) videos.find(eq("_id",video_id)).first();

        if(video==null){
            logger.error("Video not found:"+video_id);
            return "error";
        }
        return video.getString("channel_id");
    }

    @Override
    public void setStatusMonitorStats() {
        //TODO setStatusMonitorStats()
    }

    @Override
    public int getTotalTweets() {
        return (int)tweets.count();
    }

    @Override
    public boolean setVideoAsIncomplete(String videoId) {
        Document document = (Document) videos.find(eq("_id",videoId)).first();
        if(document==null)
            return false;
        try{
            Document meta = (Document) document.get("meta");
            meta.put("incomplete",true);
            meta.put("monitored",false);
            meta.put("finished",false);
            document.put("meta",meta);
            videos.replaceOne(eq("_id", videoId), document);
        } catch(MongoWriteException we){
            logger.error(we.getError().getMessage());
            return false;
        } catch(MongoException ex) {
            logger.error(ex.getLocalizedMessage());
            return false;
        }
        return true;
    }

    public String getDatabaseName() {
        return databaseName;
    }


    public Document createDefaultConfiguration(){
        Document document = new Document("_id","config");
        document.append("max_videos_being_monitored",default_max_videos_being_monitored);
        document.append("max_comments_per_video",default_max_comments_per_video);
        return document;
    }
}
