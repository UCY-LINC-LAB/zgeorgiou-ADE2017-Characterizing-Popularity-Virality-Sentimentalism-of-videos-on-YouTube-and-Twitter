package com.zgeorg03.classification;

import com.zgeorg03.classification.features.CreateBaseFeatures;
import com.zgeorg03.classification.features.CreateFeatures;
import com.zgeorg03.classification.records.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Giorgos on 29/12/2016.
 */
public class FeatureManager {

    private int t_window;
    private int offset;
    private int l_window;
    private int split_days;
    private float labPer;
    private String twFeatures;
    private String ytFeatures;
    private Map<String, VideoData> videosMapRecent;
    private Map<String, VideoData> videosMap;
    private ArrayList<String> uniqueVideos;
    private ArrayList<String> uniqueVideosRecent;


    public FeatureManager(int t, int o, int l, int split, float per, String yt, String tw){
        this.t_window = t;
        this.offset = o;
        this.l_window = l;
        this.split_days = split;
        this.labPer = per;
        this.ytFeatures = yt;
        this.twFeatures = tw;
        videosMap = new HashMap<>();
        videosMapRecent = new HashMap<>();
        uniqueVideos = new ArrayList<>();
        uniqueVideosRecent = new ArrayList<>();
    }


    public YoutubeFeatures mapYouTubeFeatures(VideoRecord vr, int numOfDays){

        int yt_category = vr.getYt_category();
        int yt_artificial_category = vr.getYt_artificial_category();
        long yt_uploaded = vr.getYt_uploaded();
        long duration = vr.getDuration();
        double comments_sentiment_neg = vr.getComments_sentiment_neg();
        double comments_sentiment_neu = vr.getComments_sentiment_neu();
        double comments_sentiment_pos = vr.getComments_sentiment_pos();
        double comments_sentiment_compound = vr.getComments_sentiment_compound();

        YoutubeFeatures yt =new YoutubeFeatures(yt_category, yt_artificial_category,yt_uploaded, duration,
                comments_sentiment_neg,comments_sentiment_neu, comments_sentiment_pos,comments_sentiment_compound);

        /**
         * Creating days for youtube features
         */

        for(int i=0;i<numOfDays;i++){
            DayRecord dr = vr.getDays().get(i);
            long views = dr.getViews();
            long likes = dr.getLikes();
            long dislikes = dr.getDislikes();
            long comments = dr.getComments();
            long channel_views = dr.getChannel_views();
            long channel_comments = dr.getChannel_comments();
            long channel_subscribers = dr.getChannel_subscribers();
            long channel_videos = dr.getChannel_videos();

            YoutubeDay day = new YoutubeDay(i,views,likes,dislikes,comments,channel_views,channel_comments,
                    channel_subscribers,channel_videos);
            yt.addDay(day);
        }

        return yt;

    }


    public TwitterFeatures mapTwitterFeatures(VideoRecord vr, int numOfDays){

        TwitterFeatures tw = new TwitterFeatures();

        /**
         * Creating days for twitter features
         */
        for(int i=0;i<numOfDays;i++){
            DayRecord dr = vr.getDays().get(i);
            TwitterDay day = new TwitterDay(i);
            day.setTw_tweets(dr.getTw_tweets());
            day.setTw_orig_tweets(dr.getTw_orig_tweets());
            day.setTw_retweets(dr.getTw_retweets());
            day.setTw_user_favorites(dr.getTw_user_favorites());
            day.setTw_hashtags(dr.getTw_hashtags());
            day.setTw_eng(dr.getTw_eng());
            day.setTw_sp(dr.getTw_sp());
            day.setTw_user_eng(dr.getTw_user_eng());
            day.setTw_user_sp(dr.getTw_user_sp());
            day.setTw_user_verified(dr.getTw_user_verified());
            day.setUsers_days_created_before_video(dr.getUsers_days_created_before_video());
            day.setTw_user_followers(dr.getTw_user_followers());
            day.setTw_user_friends(dr.getTw_user_friends());
            day.setTw_user_statuses(dr.getTw_user_statuses());
            tw.addDay(day,i);
        }

        return tw;
    }

    public void createFeatures(){
        CreateFeatures features = new CreateFeatures(false,t_window,offset,l_window);
        features.setLabPer(labPer);
        features.setT_window(t_window);
        features.setOffset(offset);
        features.setL_window(l_window);
        features.setUniqueVideos(uniqueVideos);
        features.setVideosMap(videosMap);
        features.setTwBinary(twFeatures);
        features.setYtBinary(ytFeatures);
        features.create();

        CreateFeatures featuresRecent = new CreateFeatures(true,t_window,offset,l_window);
        featuresRecent.setLabPer(labPer);
        featuresRecent.setT_window(t_window);
        featuresRecent.setOffset(offset);
        featuresRecent.setL_window(l_window);
        featuresRecent.setUniqueVideos(uniqueVideosRecent);
        featuresRecent.setVideosMap(videosMapRecent);
        featuresRecent.setTwBinary(twFeatures);
        featuresRecent.setYtBinary(ytFeatures);
        featuresRecent.create();

        CreateBaseFeatures baseFeatures = new CreateBaseFeatures(false,t_window,offset,l_window);
        baseFeatures.setLabPer(labPer);
        baseFeatures.setT_window(t_window);
        baseFeatures.setOffset(offset);
        baseFeatures.setL_window(l_window);
        baseFeatures.setVideosMap(videosMap);
        baseFeatures.setUniqueVideos(uniqueVideos);
        baseFeatures.create();

        CreateBaseFeatures baseFeaturesRecent = new CreateBaseFeatures(true,t_window,offset,l_window);
        baseFeaturesRecent.setLabPer(labPer);
        baseFeaturesRecent.setT_window(t_window);
        baseFeaturesRecent.setOffset(offset);
        baseFeaturesRecent.setL_window(l_window);
        baseFeaturesRecent.setVideosMap(videosMapRecent);
        baseFeaturesRecent.setUniqueVideos(uniqueVideosRecent);
        baseFeaturesRecent.create();

        System.out.println("Created Features");
    }


    public void populate(Map<String, VideoData> videosMap,Map<String, VideoData> videosMapRecent){

        this.videosMap = videosMap;
        this.videosMapRecent = videosMapRecent;
    }


}
