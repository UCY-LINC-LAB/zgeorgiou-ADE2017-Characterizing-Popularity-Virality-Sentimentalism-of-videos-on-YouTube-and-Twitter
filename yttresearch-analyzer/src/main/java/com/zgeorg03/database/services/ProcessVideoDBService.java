package com.zgeorg03.database.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.rawvideos.models.RawVideo;
import com.zgeorg03.utils.DateUtil;
import com.zgeorg03.analysis.models.Video;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by zgeorg03 on 3/2/17.
 */
public class ProcessVideoDBService {
    private static final Logger logger = LoggerFactory.getLogger(DBServices.class);

    private final MongoCollection videos;
    private final MongoCollection tweets;
    private final MongoCollection comments;
    private final MongoCollection processedDBVideos;

    public ProcessVideoDBService(MongoCollection videos, MongoCollection tweets, MongoCollection comments, MongoCollection processedVideos) {
        this.videos = videos;
        this.tweets = tweets;
        this.comments = comments;
        this.processedDBVideos = processedVideos;
    }

    /**
     db.getCollection('processedVideos').aggregate([
     { "$unwind" : "$days"},
     { "$match" : {"days.day" : {$gte : 0}}},
     { "$group" :
     { _id : "$_id", sum : { $sum : "$days.views_added"}
     }},
     { "$sort" : { "sum"  : -1}},
     { "$limit" : 5 },
     { "$project" : {total_views:"$sum"}}
     ]);


     * @param limit
     * @return
     */
    public JsonArray getVideosWithTheMostViews(int artificial_category, int lbl_wnd, int limit){
//TODO Change this ...lblwnd
        if(limit==0)
            return new JsonArray();
        Document match;
        if(artificial_category!=0)
            match = new Document("$and", Arrays.asList(
                    new Document("artificial_category", artificial_category),
                    new Document("days.day",
                            new Document("$gte", lbl_wnd))
            ));
        else
            match = new Document("days.day", new Document("$gte", lbl_wnd));

        List<Document> query = Arrays.asList(
                new Document("$unwind", "$days"),
                new Document("$match",match),
                new Document("$group",
                        new Document("_id", "$_id")
                                .append("sum",
                                        new Document("$sum", "$days.views_added"))),
                new Document("$sort",
                        new Document("sum", -1)),
                new Document("$limit", limit),
                new Document("$project",
                        new Document("total_views", "$sum"))
        );

        MongoCursor cursor = processedDBVideos.aggregate(query).iterator();
        JsonArray array = new JsonArray();
        while(cursor.hasNext()){
            JsonObject object = new JsonObject();
            Document document = (Document) cursor.next();
            String video_id = document.getString("_id");
            long total_views = document.getLong("total_views");
            object.addProperty("video_id",video_id);
            object.addProperty("total_views",total_views);
            object.addProperty("url","/videos/"+video_id);
            array.add(object);
        }
        return array;

    }

    public JsonArray getVideosWithTheMostViews(int artificial_category, int limit){

        if(limit==0)
            return new JsonArray();
        Document match;
        if(artificial_category!=0)
            match = new Document("artificial_category", artificial_category) ;
        else
            match = new Document("artificial_category", new Document("$gt",0)) ;

        List<Document> query = Arrays.asList(
                new Document("$unwind", "$days"),
                new Document("$match",match),
                new Document("$group",
                        new Document("_id", "$_id")
                                .append("sum",
                                        new Document("$sum", "$days.views_added"))),
                new Document("$sort",
                        new Document("sum", -1)),
                new Document("$limit", limit),
                new Document("$project",
                        new Document("total_views", "$sum"))
        );

        MongoCursor cursor = processedDBVideos.aggregate(query).iterator();
        JsonArray array = new JsonArray();
        while(cursor.hasNext()){
            JsonObject object = new JsonObject();
            Document document = (Document) cursor.next();
            String video_id = document.getString("_id");
            long total_views = document.getLong("total_views");
            object.addProperty("video_id",video_id);
            object.addProperty("total_views",total_views);
            object.addProperty("url","/videos/"+video_id);
            array.add(object);
        }
        return array;

    }
    public JsonArray getVideosWithTheMostTweets(int artificial_category, int lbl_wnd, int limit){

        if(limit==0)
            return new JsonArray();
        Document match;
        if(artificial_category!=0)
            match = new Document("$and", Arrays.asList(
                    new Document("artificial_category", artificial_category),
                    new Document("days.day",
                            new Document("$gte", lbl_wnd))
            ));
        else
            match = new Document("days.day", new Document("$gte", lbl_wnd));

        List<Document> query = Arrays.asList(
                new Document("$unwind", "$days"),
                new Document("$match",match),
                new Document("$group",
                        new Document("_id", "$_id")
                                .append("sum",
                                        new Document("$sum", "$days.tweets_added"))),
                new Document("$sort",
                        new Document("sum", -1)),
                new Document("$limit", limit),
                new Document("$project",
                        new Document("total_tweets", "$sum"))
        );

        MongoCursor cursor = processedDBVideos.aggregate(query).iterator();
        JsonArray array = new JsonArray();
        while(cursor.hasNext()){
            JsonObject object = new JsonObject();
            Document document = (Document) cursor.next();
            String video_id = document.getString("_id");
            long total_tweets = document.getLong("total_tweets");
            object.addProperty("video_id",video_id);
            object.addProperty("total_tweets",total_tweets);
            object.addProperty("url","/videos/"+video_id);
            array.add(object);
        }
        return array;

    }

    public JsonArray getVideosWithTheMostTweets(int artificial_category, int limit){

        if(limit==0)
            return new JsonArray();
        Document match;
        if(artificial_category!=0)
            match = new Document("artificial_category", artificial_category);
        else
            match = new Document("artificial_category", new Document("$gt",0)) ;

        List<Document> query = Arrays.asList(
                new Document("$unwind", "$days"),
                new Document("$match",match),
                new Document("$group",
                        new Document("_id", "$_id")
                                .append("sum",
                                        new Document("$sum", "$days.tweets_added"))),
                new Document("$sort",
                        new Document("sum", -1)),
                new Document("$limit", limit),
                new Document("$project",
                        new Document("total_tweets", "$sum"))
        );

        MongoCursor cursor = processedDBVideos.aggregate(query).iterator();
        JsonArray array = new JsonArray();
        while(cursor.hasNext()){
            JsonObject object = new JsonObject();
            Document document = (Document) cursor.next();
            String video_id = document.getString("_id");
            long total_tweets = document.getLong("total_tweets");
            object.addProperty("video_id",video_id);
            object.addProperty("total_tweets",total_tweets);
            object.addProperty("url","/videos/"+video_id);
            array.add(object);
        }
        return array;

    }
    public JsonArray getRecentVideos(int days, int artificial_category, int limit){
        long daysInMillis = DateUtil.dayInMillis*days;
        if(limit==0)
            return new JsonArray();
        Document match;
        if(artificial_category!=0)
            match = new Document("$and", Arrays.asList(
                    new Document("artificial_category", artificial_category),
                    new Document("diff",
                            new Document("$lte", daysInMillis))
            ));
        else
            match = new Document("diff", new Document("$lte", daysInMillis));

        List<Document> query = Arrays.asList(
                new Document("$addFields",
                        new Document("a","$collected_at_timestamp").append("b","$published_at_timestamp")),
                new Document("$addFields",
                        new Document("diff", new Document("$subtract",Arrays.asList("$a","$b")))),

                new Document("$match",match),
                new Document("$project",
                        new Document("total_views", "$total_views").append("total_tweets","$total_tweets")),
                //new Document("$sort",new Document("total_views",-1)),
                //new Document("$limit", limit*4),
                new Document("$sample", new Document("size",limit))
        );

        MongoCursor cursor = processedDBVideos.aggregate(query).iterator();
        JsonArray array = new JsonArray();
        while(cursor.hasNext()){
            JsonObject object = new JsonObject();
            Document document = (Document) cursor.next();
            String video_id = document.getString("_id");
            long total_views = document.getLong("total_views");
            long total_tweets = document.getLong("total_tweets");
            object.addProperty("video_id",video_id);
            object.addProperty("total_views",total_views);
            object.addProperty("total_tweets",total_tweets);
            object.addProperty("url","/videos/"+video_id);
            array.add(object);
        }
        return array;

    }

    public JsonArray getRandomVideos(int artificial_category, int limit){
        if(limit==0)
            return new JsonArray();
        Document match;
        if(artificial_category!=0)
            match = new Document("artificial_category", artificial_category) ;
        else
            match = new Document("artificial_category", new Document("$gt",0)) ;

        List<Document> query = Arrays.asList(
                new Document("$match",match),
                new Document("$project",
                        new Document("total_views", "$total_views").append("total_tweets","$total_tweets")),
                //new Document("$sort",new Document("total_views",-1)),
                //new Document("$limit", limit*4),
                new Document("$sample", new Document("size",limit))
        );

        MongoCursor cursor = processedDBVideos.aggregate(query).iterator();
        JsonArray array = new JsonArray();
        while(cursor.hasNext()){
            JsonObject object = new JsonObject();
            Document document = (Document) cursor.next();
            String video_id = document.getString("_id");
            long total_views = document.getLong("total_views");
            long total_tweets = document.getLong("total_tweets");
            object.addProperty("video_id",video_id);
            object.addProperty("total_views",total_views);
            object.addProperty("total_tweets",total_tweets);
            object.addProperty("url","/videos/"+video_id);
            array.add(object);
        }
        return array;

    }
    /**
     * Add a processedVideo
     * @param rawVideo
     * @return
     */
    public boolean addOrReplaceProcessedVideo(RawVideo rawVideo){
       Document document = rawVideo.toBson();
        try{
            if(processedDBVideos.count(eq("_id", rawVideo.getVideo_id()))==1)
                processedDBVideos.replaceOne(eq("_id", rawVideo.getVideo_id()),document);
            else
                processedDBVideos.insertOne(document);
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
        } catch(Exception ex) {
            logger.error(ex.getLocalizedMessage());
            return false;
        }
    }
    /**
     * Set a processedVideo processed
     * @param videoId
     * @return
     */
    public boolean setVideoAsProcessed(String videoId ){
        try{
            videos.updateOne(eq("_id",videoId),new Document("$set",new Document("meta.processed",true)));
            return true;
        } catch(MongoWriteException we){
            logger.error(we.getError().getMessage());
            return false;
        } catch(MongoException ex) {
            logger.error(ex.getLocalizedMessage());
            return false;
        } catch(Exception ex) {
            logger.error(ex.getLocalizedMessage());
            return false;
        }
    }

    /**
     * Get total number of videos ready for analysis
     * @return
     */
    public int getTotalVideos(int artificial_category){
        if(artificial_category==0)
            return (int) processedDBVideos.count();
        else
            return (int) processedDBVideos.count(eq("artificial_category",artificial_category));
    }

    /**
     * Retrieve video
     * @param videoId
     * @return
     */
    public Video getVideo(String videoId){

        Document document =  (Document) processedDBVideos.find(eq("_id",videoId)).first();
        if(document ==null)
            return null;

        return new Video.Builder().create(document);

    }
    /**
     * Retrieve videos
     * @param
     * @return
     */
    public List<Video> getVideos(){
        List<Video> videos = new LinkedList<>();
        Iterator<Document> iterator =  processedDBVideos.find().iterator();
        while(iterator.hasNext()){
            Document document = iterator.next();
            videos.add(new Video.Builder().create(document));
        }
        return videos;

    }


    /**
     * Get all tweets from the specified video
     * @param videoId
     * @return
     */
    public List<Document> getTweets(String videoId){
        MongoCursor cursor =  tweets.find(eq("video_id",videoId)).iterator();
        List<Document> list = new LinkedList<>();
        while(cursor.hasNext()){
           Document tweet = (Document) cursor.next();
           list.add(tweet);
        }
        return list;
    }

    /**
     * Get all comments from the specified video
     * @param videoId
     * @return
     */
    public List<Document> getComments(String videoId){
        MongoCursor cursor =  comments.find(eq("video_id",videoId)).iterator();
        List<Document> list = new LinkedList<>();
        while(cursor.hasNext()){
            Document tweet = (Document) cursor.next();
            list.add(tweet);
        }
        return list;
    }
}
