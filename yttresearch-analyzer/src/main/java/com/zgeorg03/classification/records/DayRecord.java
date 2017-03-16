package com.zgeorg03.classification.records;

/**
 * Created by gdemos01 on 3/15/2017. Updated by zgeorg03  on 3/16/2017
 */
public class DayRecord {

    private final long views;
    private final long likes;
    private final long dislikes;
    private final long comments;
    private final long channel_views;
    private final long channel_comments;
    private final long channel_subscribers;
    private final long channel_videos;

    private final long tw_tweets;
    private final long tw_orig_tweets;
    private final long tw_retweets;
    private final long tw_user_favorites;
    private final long tw_hashtags;
    private final long tw_eng;
    private final long tw_sp;
    private final long tw_user_eng;
    private final long tw_user_sp;
    private final long tw_user_verified;
    private final double users_days_created_before_video;
    private final double tw_user_followers;
    private final double tw_user_friends;
    private final double tw_user_statuses;


    public DayRecord(long views, long likes, long dislikes, long comments, long channel_views, long channel_comments,
                     long channel_subscribers, long channel_videos, long tw_tweets, long tw_orig_tweets, long tw_retweets,
                     long tw_user_favorites, long tw_hashtags, long tw_eng, long tw_sp, long tw_user_eng, long tw_user_sp,
                     long tw_user_verified, double users_days_created_before_video, double tw_user_followers,
                     double tw_user_friends, double tw_user_statuses) {
        this.views = views;
        this.likes = likes;
        this.dislikes = dislikes;
        this.comments = comments;
        this.channel_views = channel_views;
        this.channel_comments = channel_comments;
        this.channel_subscribers = channel_subscribers;
        this.channel_videos = channel_videos;
        this.tw_tweets = tw_tweets;
        this.tw_orig_tweets = tw_orig_tweets;
        this.tw_retweets = tw_retweets;
        this.tw_user_favorites = tw_user_favorites;
        this.tw_hashtags = tw_hashtags;
        this.tw_eng = tw_eng;
        this.tw_sp = tw_sp;
        this.tw_user_eng = tw_user_eng;
        this.tw_user_sp = tw_user_sp;
        this.tw_user_verified = tw_user_verified;
        this.users_days_created_before_video = users_days_created_before_video;
        this.tw_user_followers = tw_user_followers;
        this.tw_user_friends = tw_user_friends;
        this.tw_user_statuses = tw_user_statuses;
    }

    public long getViews() {
        return views;
    }

    public long getLikes() {
        return likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public long getComments() {
        return comments;
    }

    public long getChannel_views() {
        return channel_views;
    }

    public long getChannel_comments() {
        return channel_comments;
    }

    public long getChannel_subscribers() {
        return channel_subscribers;
    }

    public long getChannel_videos() {
        return channel_videos;
    }

    public long getTw_tweets() {
        return tw_tweets;
    }

    public long getTw_orig_tweets() {
        return tw_orig_tweets;
    }

    public long getTw_retweets() {
        return tw_retweets;
    }

    public long getTw_user_favorites() {
        return tw_user_favorites;
    }

    public long getTw_hashtags() {
        return tw_hashtags;
    }

    public long getTw_eng() {
        return tw_eng;
    }

    public long getTw_sp() {
        return tw_sp;
    }

    public long getTw_user_eng() {
        return tw_user_eng;
    }

    public long getTw_user_sp() {
        return tw_user_sp;
    }

    public long getTw_user_verified() {
        return tw_user_verified;
    }

    public double getUsers_days_created_before_video() {
        return users_days_created_before_video;
    }

    public double getTw_user_followers() {
        return tw_user_followers;
    }

    public double getTw_user_friends() {
        return tw_user_friends;
    }

    public double getTw_user_statuses() {
        return tw_user_statuses;
    }
}
