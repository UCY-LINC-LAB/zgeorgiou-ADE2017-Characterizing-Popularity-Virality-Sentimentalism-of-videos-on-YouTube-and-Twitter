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
import com.zgeorg03.database.videos.DBVideosService;
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


    private final DBVideosService dbVideosService;

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

        dbVideosService  = new DBVideosService(connection,videos);


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
    public void setStatusMonitorStats() {
        //TODO setStatusMonitorStats()
    }

    @Override
    public int getTotalTweets() {
        return (int)tweets.count();
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

    public DBVideosService getDbVideosService() {
        return dbVideosService;
    }
}
