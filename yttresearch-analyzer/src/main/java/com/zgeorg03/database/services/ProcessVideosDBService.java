package com.zgeorg03.database.services;

import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.videoprocess.DBVideo;
import com.zgeorg03.videoprocess.Day;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by zgeorg03 on 3/2/17.
 */
public class ProcessVideosDBService {
    private static final Logger logger = LoggerFactory.getLogger(DBServices.class);

    private final MongoCollection videos;
    private final MongoCollection tweets;
    private final MongoCollection comments;
    private final MongoCollection processedDBVideos;

    public ProcessVideosDBService(MongoCollection videos, MongoCollection tweets, MongoCollection comments, MongoCollection processedVideos) {
        this.videos = videos;
        this.tweets = tweets;
        this.comments = comments;
        this.processedDBVideos = processedVideos;
    }


    /**
     * Add a processedVideo
     * @param video
     * @return
     */
    public boolean addOrReplaceProcessedVideo(DBVideo video){
       Document document = video.toBson();
        try{
            if(processedDBVideos.count(eq("_id",video.getVideo_id()))==1)
                processedDBVideos.replaceOne(eq("_id",video.getVideo_id()),document);
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
     * Get total number of videos ready for analysis
     * @return
     */
    public int getTotalVideos(){
        return (int) processedDBVideos.count();
    }

    /**
     * Retrieve video
     * @param videoId
     * @return
     */
    public DBVideo getVideo(String videoId){

        Document document =  (Document) processedDBVideos.find(eq("_id",videoId)).first();
        if(document ==null)
            return null;

        System.out.println(document);
        String title = document.getString("title");
        String description = document.getString("description");
        int category = document.getInteger("category");
        int artificial_category = document.getInteger("artificial_category");
        long published_at = document.getLong("published_at_timestamp");
        long collected_at = document.getLong("collected_at_timestamp");
        long duration = document.getLong("duration");

        List<Document> documents = (List<Document>) document.get("days");

        DBVideo dbVideo = new DBVideo(videoId,title,description,category,artificial_category,published_at,collected_at,duration);
        for(Document doc : documents){
            String date = doc.getString("date");
            long views_added = doc.getLong("views_added");
            long likes_added = doc.getLong("likes_added");
            long dislikes_added = doc.getLong("dislikes_added");
            long favorites_added = doc.getLong("favorites_added");
            long comments_added = doc.getLong("comments_added");
            long channel_views_added = doc.getLong("channel_views_added");
            long channel_comments_added = doc.getLong("channel_comments_added");
            long channel_subscriber_added = doc.getLong("channel_subscriber_added");
            long channel_videos_added = doc.getLong("channel_videos_added");
            Day day = new Day(date,views_added,likes_added,dislikes_added,favorites_added,comments_added,channel_views_added,channel_comments_added,channel_subscriber_added,channel_videos_added);
            dbVideo.getDays().add(day);
        }

        dbVideo.setTotal_views(document.getLong("total_views"));
        dbVideo.setTotal_likes(document.getLong("total_likes"));
        dbVideo.setTotal_dislikes(document.getLong("total_dislikes"));
        dbVideo.setTotal_comments(document.getLong("total_comments"));
        dbVideo.setTotal_tweets(document.getLong("total_tweets"));
        dbVideo.setTotal_original_tweets(document.getLong("total_original_tweets"));
        dbVideo.setTotal_retweets(document.getLong("total_retweets"));
        dbVideo.setTotal_channel_views(document.getLong("total_channel_views"));
        dbVideo.setTotal_channel_comments(document.getLong("total_channel_comments"));
        dbVideo.setTotal_channel_subscribers(document.getLong("total_channel_subscribers"));
        dbVideo.setTotal_channel_videos(document.getLong("total_channel_videos"));
        return dbVideo;
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
