package com.zgeorg03.classification.records;

/**
 * Created by Giorgos on 06-Nov-16.
 */
public class YoutubeDay {

    private final int day;
    private final long yt_views;
    private final long yt_likes;
    private final long yt_dislikes;
    private final long yt_comments;
    private final long yt_channel_views;
    private final long yt_channel_comments;
    private final long yt_channel_subscribers;
    private final long yt_channel_videos;


    public YoutubeDay(int day, long yt_views, long yt_likes, long yt_dislikes, long yt_comments, long yt_channel_views,
                      long yt_channel_comments, long yt_channel_subscribers, long yt_channel_videos) {
        this.day = day;
        this.yt_views = yt_views;
        this.yt_likes = yt_likes;
        this.yt_dislikes = yt_dislikes;
        this.yt_comments = yt_comments;
        this.yt_channel_views = yt_channel_views;
        this.yt_channel_comments = yt_channel_comments;
        this.yt_channel_subscribers = yt_channel_subscribers;
        this.yt_channel_videos = yt_channel_videos;
    }

    public int getDay() {
        return day;
    }

    public long getYt_views() {
        return yt_views;
    }

    public long getYt_likes() {
        return yt_likes;
    }

    public long getYt_dislikes() {
        return yt_dislikes;
    }

    public long getYt_comments() {
        return yt_comments;
    }

    public long getYt_channel_views() {
        return yt_channel_views;
    }

    public long getYt_channel_comments() {
        return yt_channel_comments;
    }

    public long getYt_channel_subscribers() {
        return yt_channel_subscribers;
    }

    public long getYt_channel_videos() {
        return yt_channel_videos;
    }
}