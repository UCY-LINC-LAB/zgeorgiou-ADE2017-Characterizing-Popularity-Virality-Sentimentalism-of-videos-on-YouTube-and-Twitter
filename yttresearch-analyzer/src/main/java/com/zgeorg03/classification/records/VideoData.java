package com.zgeorg03.classification.records;

import com.zgeorg03.analysis.models.Video;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Giorgos on 06-Nov-16.
 */
public class VideoData implements Comparable<VideoData>{

    private long published_at;
    private long collected_at;
    private YoutubeFeatures youtubeFeatures;
    private TwitterFeatures twitterFeatures;
    private Map<Integer,TwitterDay> twitterDayMap;
    private String video_id;
    private long totalViews;
    private long totalTweets;
    private long channel_uploads;
    private long channel_subscribers;
    private long channel_views;
    private long channel_comments;

    public VideoData(Video video, YoutubeFeatures youtubeFeatures, TwitterFeatures twitterFeatures){
        this.published_at = video.getPublished_at();
        this.collected_at = video.getCollected_at();
        this.video_id = video.getVideo_id();
        this.youtubeFeatures = youtubeFeatures;
        this.twitterFeatures = twitterFeatures;
        this.twitterDayMap = (twitterFeatures!=null)?twitterFeatures.getAllDays():new HashMap<>();
    }

    public YoutubeFeatures getYoutubeFeatures(){
        return youtubeFeatures;
    }

    public TwitterFeatures getTwitterFeatures(){
        return twitterFeatures;
    }


    public String getVideo_id() {
        return video_id;
    }

    /***
     * totalViews and totalTweets are going to be used later
     * in Labeling
     *
     * */
    public void calculateTotals(int offset,int l_win){

        for(int i=offset+1;i<=l_win;i++) {
            totalViews += youtubeFeatures.getFeaturesForDay(i).getYt_views();
            totalTweets += twitterDayMap.get(i).getTw_tweets();
            channel_uploads+= youtubeFeatures.getFeaturesForDay(i).getYt_channel_videos();
            channel_subscribers+= youtubeFeatures.getFeaturesForDay(i).getYt_channel_subscribers();
            channel_comments+= youtubeFeatures.getFeaturesForDay(i).getYt_channel_comments();
            channel_views+= youtubeFeatures.getFeaturesForDay(i).getYt_channel_views();
        }

    }

    public long getTotalViews() {
        return totalViews;
    }

    public long getTotalTweets() {
        return totalTweets;
    }

    public long getChannel_uploads() {
        return channel_uploads;
    }

    public long getChannel_subscribers() {
        return channel_subscribers;
    }

    public long getChannel_views() {
        return channel_views;
    }

    public long getChannel_comments() {
        return channel_comments;
    }


    public long getPublished_at() {
        return published_at;
    }

    public long getCollected_at() {
        return collected_at;
    }

    @Override
    public int compareTo(VideoData videoData) {

        return this.getVideo_id().compareTo(videoData.getVideo_id());
    }
}
