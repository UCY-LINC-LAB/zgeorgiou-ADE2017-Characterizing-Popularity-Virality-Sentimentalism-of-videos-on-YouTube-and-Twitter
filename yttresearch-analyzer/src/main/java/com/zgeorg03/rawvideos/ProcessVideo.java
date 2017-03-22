package com.zgeorg03.rawvideos;

import com.zgeorg03.analysis.SentimentAnalysis;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.database.services.ProcessVideoDBService;
import com.zgeorg03.rawvideos.models.RawDay;
import com.zgeorg03.rawvideos.models.RawVideo;
import com.zgeorg03.utils.Categories;
import com.zgeorg03.utils.DateUtil;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zgeorg03 on 3/2/17.
 */
public class ProcessVideo {
    private static final Logger logger = LoggerFactory.getLogger(DBServices.class);
    private static final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    private final ProcessVideoDBService processVideoDBService;
    private final String videoID;

    private final Document video;
    private final List<Document> tweets;
    private final List<Document> comments;

    private RawVideo rawVideo;

    private final SentimentAnalysis sentimentAnalysis;
    public ProcessVideo(DBServices dbServices, String videoID, SentimentAnalysis sentimentAnalysis) throws Exception {

        this.processVideoDBService = dbServices.getProcessVideoDBService();
        this.videoID = videoID;
        this.sentimentAnalysis = sentimentAnalysis;
        video =dbServices.getVideo(videoID);
        if(video ==null)
            throw new Exception("RawVideo doesn't exist");
        tweets = processVideoDBService.getTweets(videoID);
        comments = processVideoDBService.getComments(videoID);

        addStaticInfo();
        addDynamicInfo();
    }

    private void addStaticInfo(){
        String title = video.getString("title");
        String description = video.getString("description");
        int category = video.getInteger("category_id");
        int artificial_category = Categories.getArtificialCategory(category);
        long published_at = video.getLong("published_at");
        long collected_at = ((Document) video.get("meta")).getLong("timestamp");
        long duration = video.getLong("duration");

        List<String> comments_text = new LinkedList<>();
        for(Document comment : comments){
            String text = comment.getString("text");
            comments_text.add(text);

        }
        rawVideo = new RawVideo(videoID,title,description,category,artificial_category,published_at,collected_at,duration,sentimentAnalysis,comments_text);
    }


    /**
     */
    private void addDynamicInfo(){
        List<Document> days = (List<Document>) this.video.get("days");
        if(days.size()!=16)
            logger.error("RawVideo: "+ videoID+" doesn't have 15 days. This shouldn't have happen.");
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

        RawDay first = new RawDay(0, DateUtil.toDate(timestamp), views_added,likes_added,dislikes_added,favorites_added,comments_added,channel_views_added,channel_comments_added,channel_subscribers_added,channel_videos_added, sentimentAnalysis);
        rawVideo.getRawDays().add(first);
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
            RawDay current = new RawDay(i,DateUtil.toDate(timestamp), views_added,likes_added,dislikes_added,favorites_added,comments_added,channel_views_added,channel_comments_added,channel_subscribers_added,channel_videos_added, sentimentAnalysis);
            rawVideo.getRawDays().add(current);
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
        rawVideo.setTotal_views(total_views);
        rawVideo.setTotal_likes(total_likes);
        rawVideo.setTotal_dislikes(dislikes_added);
        rawVideo.setTotal_favorited(favorites_added);
        rawVideo.setTotal_comments(comments_added);
        rawVideo.setTotal_channel_views(channel_views_added);
        rawVideo.setTotal_channel_comments(channel_comments_added);
        rawVideo.setTotal_channel_subscribers(total_channel_subscribers);
        rawVideo.setTotal_channel_videos(channel_videos_added);
        addTweets(rawVideo.getCollected_at());
    }

    /**
     * Add tweets
     */
    private void addTweets(long startOffset){
        long millisInDay = 1000*60*60*24;
        long total_tweets=0;
        long total_original_tweets=0;
        long total_retweets=0;
        int totalDays = rawVideo.getRawDays().size();

        for(Document document : tweets){
            long created_at = document.getLong("created_at");
            long diff = created_at - startOffset;
            if(diff<0)
                diff=0;
            int dayIndex = (int) (diff/millisInDay);

            if(dayIndex>=totalDays)
                continue;
            String  text = document.getString("text");
            String  lang = document.getString("lang");
            boolean is_favorited   = document.getBoolean("is_favorited");
            boolean is_possibly_sensitive   = document.getBoolean("is_possibly_sensitive");
            boolean is_retweet   = document.getBoolean("is_retweet");
            long  user_created_at = document.getLong("user_created_at");
            long  user_followers_count = document.getLong("user_followers_count");
            long  user_friends_count = document.getLong("user_friends_count");
            long  user_favorites_count = document.getLong("user_favorites_count");
            long  user_listed_count = document.getLong("user_listed_count");
            long  user_statuses_count = document.getLong("user_statuses_count");
            boolean  user_verified = document.getBoolean("user_verified");
            String  user_lang = document.getString("user_lang");
            List<String>  hashtags = (List<String>) document.get("hashtags");


            RawDay rawDay = rawVideo.getRawDays().get(dayIndex);

            rawDay.setTweetStuff(rawVideo.getPublished_at(),lang,text,is_favorited,is_possibly_sensitive,is_retweet,user_created_at,user_followers_count,user_friends_count,user_favorites_count,user_listed_count,user_statuses_count,user_verified,user_lang,hashtags);
            total_tweets++;
            if(is_retweet)
                total_retweets++;
            else
                total_original_tweets++;
        }
        rawVideo.setTotal_tweets(total_tweets);
        rawVideo.setTotal_original_tweets(total_original_tweets);
        rawVideo.setTotal_retweets(total_retweets);
    }


    public RawVideo getVideo() {
        return rawVideo;
    }
}
