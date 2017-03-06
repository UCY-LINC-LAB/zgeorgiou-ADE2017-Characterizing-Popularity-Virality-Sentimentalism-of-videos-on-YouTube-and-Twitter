package com.zgeorg03.database.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.zgeorg03.database.DBServices;
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

    public ProcessVideosDBService(MongoCollection videos, MongoCollection tweets, MongoCollection comments) {
        this.videos = videos;
        this.tweets = tweets;
        this.comments = comments;
    }

    public Document getVideo(String videoId){
        return (Document) videos.find(eq("_id",videoId)).first();
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
