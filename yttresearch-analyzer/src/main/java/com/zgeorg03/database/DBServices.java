package com.zgeorg03.database;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.zgeorg03.database.services.ProcessVideosDBService;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;

/**
 * Created by zgeorg03 on 3/2/17.
 */
public class DBServices {
    private static final Logger logger = LoggerFactory.getLogger(DBServices.class);

    private final String databaseName;

    private final MongoCollection videos;
    private final MongoCollection tweets;
    private final MongoCollection comments;
    private final MongoCollection processedVideos;

    private final ProcessVideosDBService processVideosDBService;

    public DBServices(DBConnection connection){
        videos = connection.getDatabase().getCollection("videos");
        tweets = connection.getDatabase().getCollection("tweets");
        comments = connection.getDatabase().getCollection("comments");
        processedVideos = connection.getDatabase().getCollection("processedVideos");
        databaseName = connection.getDatabase().getName();

        processVideosDBService = new ProcessVideosDBService(videos,tweets,comments);
    }



    /**
     * Get the number of videos that are finished but not processed
     * @return
     */
    public int getFinishedButNotProcessedVideosCount(){
       return (int) videos.count(and(
               eq("meta.finished",true),eq("meta.processed",false))
       );
    }

    /**
     * Get a list of videos that are finished but not processed
     * @param max Max number of videos to return
     * @return
     */
    public List<String> getFinishedButNotProcessedVideos(int max){
        try( MongoCursor cursor = videos.find(
                and(
                        eq("meta.finished",true),
                        eq("meta.processed",false)
                )
        ).projection(include("_id")).limit(max).iterator()){
            List<String> videos = new LinkedList<>();
            while(cursor.hasNext()){
                videos.add (((Document) cursor.next()).getString("_id"));
            }
            return  videos;
        }catch (MongoException ex){
            logger.error(ex.getLocalizedMessage());
            return  new LinkedList<>();
        }


    }

    public String getDatabaseName() {
        return databaseName;
    }


    public ProcessVideosDBService getProcessVideosDBService() {
        return processVideosDBService;
    }
}
