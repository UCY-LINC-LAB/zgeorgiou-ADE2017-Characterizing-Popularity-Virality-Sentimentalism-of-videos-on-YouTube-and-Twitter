package com.zgeorg03.database;

import com.google.gson.JsonObject;

/**
 * Interface for Database services
 * Created by zgeorg03 on 2/25/17.
 */
public interface DBServicesI {




    /**
     * Add video comments
     * @param comments Comments
     * @return Number of comments added
     */
    int addComments(String video_id, JsonObject comments);

    /**
     * Delete comments from a video
     * @param video_id Video Id
     * @return Number of comments deleted
     */
    int deleteComments(String video_id);

    /**
     * Add Tweet
     * @param tweet_id The id of the tweet
     * @param tweet The tweet object
     * @return
     */
    @SuppressWarnings("JavaDoc")
    boolean addTweet(long tweet_id, JsonObject tweet);

    /**
     * Delete tweets mentioning a video
     * @param video_id
     * @return
     */
    @SuppressWarnings("JavaDoc")
    int deleteTweets(String video_id);


    /**
     * Get tweet
     * @param tweet_id
     * @return
     */
    @SuppressWarnings("JavaDoc")
    boolean containsTweet(long tweet_id);
    /**
     * Add a new YouTubeAPIKey.
     * @return
     */
    @SuppressWarnings("JavaDoc")
    boolean addYouTubeAPIKey(String key);

    /**
     * Get the key that has the least cost.
     * @return
     */
    @SuppressWarnings("JavaDoc")
    String getYouTubeAPIKey();

    /**
     * Get total number of youtube API keys in the database
     * @return
     */
    @SuppressWarnings("JavaDoc")
    int getTotalYouTubeAPIKeys();

    /**
     * Get a twitter app and set it to being used. It contains the 4 keys
     * @return
     */
    @SuppressWarnings("JavaDoc")
    JsonObject getTwitterAppForUse();

    /**
     * Release a twitter app that is currently in use
     * @return
     */
    @SuppressWarnings("JavaDoc")
    boolean releaseTwitterApp(String name);

    /**
     * Get the total number of twitter applications
     * @return
     */
    @SuppressWarnings("JavaDoc")
    int getTotalTwitterApps();


    /**
     * Add a new twitter application.
     * We need to specify if it is used
     * @return True if added
     */
    boolean addTwitterApp(String name, String consumer_key, String consumer_secret, String token, String token_secret);


    /**
     * Set the max number of videos being monitored
     * @param max
     * @return True if set
     */
    @SuppressWarnings("JavaDoc")
    boolean setMaxVideosBeingMonitored(int max);

    /**
     * Get max number of videos to monitor
     * @return Max videos
     */
    int getMaxVideosBeingMonitored();


    /**
     * Get max comments per video
     * @return max comments
     */
    int getMaxCommentsPerVideo();

    /**
     * Set max nubmer of comments per video
     * @param max
     * @return
     */
    @SuppressWarnings("JavaDoc")
    boolean setMaxCommentsPerVideo(int max);


    /**
     * Get total number of tweets in the database
     * @return Total tweets
     */
    int getTotalTweets();



}
