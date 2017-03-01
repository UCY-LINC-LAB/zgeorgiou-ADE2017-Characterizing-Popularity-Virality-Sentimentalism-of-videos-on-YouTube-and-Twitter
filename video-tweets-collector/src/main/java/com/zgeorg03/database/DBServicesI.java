package com.zgeorg03.database;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zgeorg03 on 2/25/17.
 */
public interface DBServicesI {




    /**
     * Add video comments
     * @param comments
     * @return
     */
    int addComments(String video_id,JsonObject comments);

    /**
     * Add Tweet
     * @param video_id The id of the video
     * @param tweet The tweet object
     * @return
     */
    boolean addTweet(long video_id,JsonObject tweet);


    /**
     * Add a new YouTubeAPIKey.
     * @return
     */
    boolean addYouTubeAPIKey(String key);

    /**
     * Get the key that has the least cost.
     * @return
     */
    String getYouTubeAPIKey();

    /**
     * Get total number of youtube API keys in the database
     * @return
     */
    int getTotalYouTubeAPIKeys();

    /**
     * Get a twitter app and set it to being used. It contains the 4 keys
     * @return
     */
    JsonObject getTwitterAppForUse();

    /**
     * Release a twitter app that is currently in use
     * @return
     */
    boolean releaseTwitterApp(String name);

    /**
     * Get the total number of twitter applications
     * @return
     */
    int getTotalTwitterApps();

    /**
     * Get the total number of twitter applications that are free
     * @return
     */
    int getTotalFreeTwitterApps();

    /**
     * Add a new twitter application.
     * We need to specify if it is used
     * @return
     */
    boolean addTwitterApp(String name, String consumer_key,String consumer_secret,String token,String token_secret);



    JsonObject getStatistics();
    /**
     * Configuration
     */

    JsonObject getConfiguration();


    /**
     * Set the max number of videos being monitored
     * @param max
     * @return
     */
    boolean setMaxVideosBeingMonitored(int max);

    /**
     * Get max number of videos to monitor
     * @return
     */
    int getMaxVideosBeingMonitored();


    /**
     * Get max comments per video
     * @return
     */
    int getMaxCommentsPerVideo();

    /**
     * Set max nubmer of comments per video
     * @param max
     * @return
     */
    boolean setMaxCommentsPerVideo(int max);



    /**
     * Set statistics of status monitoring
     */
    void setStatusMonitorStats();

    /**
     * Get total number of tweets in the database
     * @return
     */
    int getTotalTweets();



}
