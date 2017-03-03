package com.zgeorg03.videoprocess;

import com.zgeorg03.database.DBServices;
import com.zgeorg03.database.services.ProcessVideosDBService;
import com.zgeorg03.utils.DateUtil;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zgeorg03 on 3/2/17.
 */
public class ProcessVideo {
    private static final Logger logger = LoggerFactory.getLogger(DBServices.class);
    private static final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    private final ProcessVideosDBService processVideosDBService;
    private final String videoID;

    private final Document video;
    private final List<Document> tweets;
    private final List<Document> comments;

    private  DBVideo dbVideo;

    public ProcessVideo(DBServices dbServices, String videoID) throws Exception {

        this.processVideosDBService = dbServices.getProcessVideosDBService();
        this.videoID = videoID;
        video =dbServices.getVideo(videoID);
        if(video==null)
            throw new Exception("Video doesn't exist");
        tweets = processVideosDBService.getTweets(videoID);
        comments = processVideosDBService.getComments(videoID);

        System.out.println(video);
        addStaticInfo();
        addDynamicInfo();
    }

    private void addStaticInfo(){
        String title = video.getString("title");
        String description = video.getString("description");
        int category = video.getInteger("category_id");
        int artificial_category = getArtificialCategory(category);
        long published_at = video.getLong("published_at");
        long collected_at = ((Document)video.get("meta")).getLong("timestamp");
        long duration = video.getLong("duration");

        dbVideo = new DBVideo(videoID,title,description,category,artificial_category,published_at,collected_at,duration);
    }


    /**
     * //TODO Map raw -> data
     */
    private void addDynamicInfo(){
        List<Document> days = (List<Document>) this.video.get("days");
        if(days.size()!=15)
            logger.error("Video: "+ videoID+" doesn't have 15 days. This shouldn't have happen.");
        long total_views = 0;
        long total_likes = 0;
        long total_dislikes = 0;
        long total_favorites = 0;
        long total_comments = 0;
        long total_channel_views = 0;
        long total_channel_comments = 0;
        long total_channel_subscribers = 0;
        long total_channel_videos = 0;

        //First day
        Document day = days.get(0);
        long timestamp = day.getLong("timestamp");
        long views_added = day.getLong("view_count");
        long likes_added = day.getLong("like_count");
        long dislikes_added = day.getLong("dislike_count");
        long favorites_added = day.getLong("favorite_count");
        long comments_added = day.getLong("comment_count");
        long channel_views_added = day.getLong("channel_view_count");
        long channel_comments_added = day.getLong("channel_comment_count");
        long channel_subscribers_added = day.getLong("channel_subscriber_count");
        long channel_videos_added = day.getLong("channel_video_count");

        total_views +=views_added;
        total_likes +=likes_added;
        total_dislikes +=dislikes_added;
        total_favorites +=favorites_added;
        total_comments +=comments_added;
        total_channel_views +=channel_views_added;
        total_channel_comments +=channel_comments_added;
        total_channel_subscribers +=channel_subscribers_added;
        total_channel_videos +=channel_videos_added;

        Day first = new Day(DateUtil.toDate(timestamp), views_added,likes_added,dislikes_added,favorites_added,comments_added,channel_views_added,channel_comments_added,channel_subscribers_added,channel_videos_added);
        dbVideo.getDays().add(first);
        for(int i=1;i<days.size();i++){
            day = days.get(i);
            timestamp = day.getLong("timestamp");
            views_added = day.getLong("view_count")-total_views;
            likes_added = day.getLong("like_count")-total_likes;
            dislikes_added = day.getLong("dislike_count")-total_dislikes;
            favorites_added = day.getLong("favorite_count")-total_favorites;
            comments_added = day.getLong("comment_count")-total_comments;
            channel_views_added = day.getLong("channel_view_count")-total_channel_views;
            channel_comments_added = day.getLong("channel_comment_count")-total_channel_comments;
            channel_subscribers_added = day.getLong("channel_subscriber_count")-total_channel_subscribers;
            channel_videos_added = day.getLong("channel_video_count")-total_channel_videos;
            Day current = new Day(DateUtil.toDate(timestamp), views_added,likes_added,dislikes_added,favorites_added,comments_added,channel_views_added,channel_comments_added,channel_subscribers_added,channel_videos_added);
            dbVideo.getDays().add(current);
            total_views +=views_added;
            total_likes +=likes_added;
            total_dislikes +=dislikes_added;
            total_favorites +=favorites_added;
            total_comments +=comments_added;
            total_channel_views +=channel_views_added;
            total_channel_comments +=channel_comments_added;
            total_channel_subscribers +=channel_subscribers_added;
            total_channel_videos +=channel_videos_added;
        }
        dbVideo.setTotal_views(total_views);
        dbVideo.setTotal_likes(likes_added);
        dbVideo.setTotal_dislikes(dislikes_added);
        dbVideo.setTotal_comments(total_comments);
        dbVideo.setTotal_channel_views(total_channel_views);
        dbVideo.setTotal_channel_comments(total_channel_comments);
        dbVideo.setTotal_channel_subscribers(total_channel_subscribers);
        dbVideo.setTotal_channel_videos(total_channel_videos);



    }

    /**
     * TODO Needs implementation
     * @param category
     * @return
     */
    private int getArtificialCategory(int category) {
        return category;
    }

    public DBVideo getDbVideo() {
        return dbVideo;
    }
}
