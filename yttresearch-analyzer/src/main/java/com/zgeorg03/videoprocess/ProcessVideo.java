package com.zgeorg03.videoprocess;

import com.zgeorg03.database.DBServices;
import com.zgeorg03.database.services.ProcessVideosDBService;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by zgeorg03 on 3/2/17.
 */
public class ProcessVideo {
    private static final Logger logger = LoggerFactory.getLogger(DBServices.class);
    private final ProcessVideosDBService processVideosDBService;
    private final String videoID;

    private final Document video;
    private final List<Document> tweets;
    private final List<Document> comments;

    public ProcessVideo(ProcessVideosDBService processVideosDBService, String videoID) {
        this.processVideosDBService = processVideosDBService;
        this.videoID = videoID;
        video =processVideosDBService.getVideo(videoID);
        tweets = processVideosDBService.getTweets(videoID);
        comments = processVideosDBService.getComments(videoID);

        addStaticInfo();
    }

    private void addStaticInfo(){
        String title = video.getString("title");
        String description = video.getString("description");
        int category = video.getInteger("category_id");
        int artificial_category = getArtificialCategory(category);
        long published_at = video.getLong("published_at");
        long collected_at = ((Document)video.get("meta")).getLong("timestamp");
        long duration = video.getLong("duration");

        DBVideo dbVideo = new DBVideo(videoID,title,description,category,artificial_category,published_at,collected_at,duration);
        System.out.println(dbVideo);

    }

    /**
     * TODO Needs implementation
     * @param category
     * @return
     */
    private int getArtificialCategory(int category) {
        return category;
    }

}
