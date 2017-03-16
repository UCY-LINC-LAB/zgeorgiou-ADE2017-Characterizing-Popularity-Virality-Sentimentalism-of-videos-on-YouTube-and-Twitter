package com.zgeorg03.classification.records;

/**
 * Created by Giorgos on 06-Nov-16.
 */
public class TwitterDay {

    private int day;
    private long tw_tweets;
    private long tw_orig_tweets;
    private long tw_retweets;
    private long tw_user_favorites;
    private long tw_hashtags;
    private long tw_eng;
    private long tw_sp;
    private long tw_user_eng;
    private long tw_user_sp;
    private long tw_user_verified;
    private double users_days_created_before_video;
    private double tw_user_followers;
    private double tw_user_friends;
    private double tw_user_statuses;


    public TwitterDay(int day){
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getTw_tweets() {
        return tw_tweets;
    }

    public void setTw_tweets(long tw_tweets) {
        this.tw_tweets = tw_tweets;
    }

    public long getTw_orig_tweets() {
        return tw_orig_tweets;
    }

    public void setTw_orig_tweets(long tw_orig_tweets) {
        this.tw_orig_tweets = tw_orig_tweets;
    }

    public long getTw_retweets() {
        return tw_retweets;
    }

    public void setTw_retweets(long tw_retweets) {
        this.tw_retweets = tw_retweets;
    }

    public long getTw_user_favorites() {
        return tw_user_favorites;
    }

    public void setTw_user_favorites(long tw_user_favorites) {
        this.tw_user_favorites = tw_user_favorites;
    }

    public long getTw_hashtags() {
        return tw_hashtags;
    }

    public void setTw_hashtags(long tw_hashtags) {
        this.tw_hashtags = tw_hashtags;
    }

    public long getTw_eng() {
        return tw_eng;
    }

    public void setTw_eng(long tw_eng) {
        this.tw_eng = tw_eng;
    }

    public long getTw_sp() {
        return tw_sp;
    }

    public void setTw_sp(long tw_sp) {
        this.tw_sp = tw_sp;
    }

    public long getTw_user_eng() {
        return tw_user_eng;
    }

    public void setTw_user_eng(long tw_user_eng) {
        this.tw_user_eng = tw_user_eng;
    }

    public long getTw_user_sp() {
        return tw_user_sp;
    }

    public void setTw_user_sp(long tw_user_sp) {
        this.tw_user_sp = tw_user_sp;
    }

    public long getTw_user_verified() {
        return tw_user_verified;
    }

    public void setTw_user_verified(long tw_user_verified) {
        this.tw_user_verified = tw_user_verified;
    }

    public double getUsers_days_created_before_video() {
        return users_days_created_before_video;
    }

    public void setUsers_days_created_before_video(double users_days_created_before_video) {
        this.users_days_created_before_video = users_days_created_before_video;
    }

    public double getTw_user_followers() {
        return tw_user_followers;
    }

    public void setTw_user_followers(double tw_user_followers) {
        this.tw_user_followers = tw_user_followers;
    }

    public double getTw_user_friends() {
        return tw_user_friends;
    }

    public void setTw_user_friends(double tw_user_friends) {
        this.tw_user_friends = tw_user_friends;
    }

    public double getTw_user_statuses() {
        return tw_user_statuses;
    }

    public void setTw_user_statuses(double tw_user_statuses) {
        this.tw_user_statuses = tw_user_statuses;
    }
}
