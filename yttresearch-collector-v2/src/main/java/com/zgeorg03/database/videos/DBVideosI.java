package com.zgeorg03.database.videos;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Created by zgeorg03 on 3/1/17.
 */
public interface DBVideosI {

    /**
     * This function will give the number of videos that are in monitored state and not finished
     * @return
     */
    int getTotalMonitoredVideosAndNotFinished();

    /**
     * Set video as finished
     * @return
     */
    boolean setVideoAsFinished(String video_id);

    /**
     *
     * This function will give the number of videos that  have been finished
     * @return
     */
    int getTotalFinishedVideos();

    /**
     * Return the number of videos that are marked as incomplete
     * @return
     */
    int getTotalIncompleteVideos();

    /**
     *
     * Get a list of videos that are marked as incomplete
     * @return List of id's
     */
    List<String> getVideosThatAreIncomplete();

    /**
     *
     * Get a list of videos that need a dynamic update.
     * The duration is the period that has to pass in order to set a video for dynamic update
     * @return List of id's
     */
    List<String> getVideosThatNeedDynamicUpdate();

    /**
     *
     * Get a list of videos that have comments to be collected, not over maxComments
     *
     *
     * @param maxComments
     * @return List of id's
     */
    Map<String,Integer> getVideosThatNeedComments(int maxComments);

    /**
     * Check if this video is in the database in monitor state.
     *
     * @param video_id
     * @return
     */
    boolean checkVideoExistenceAndBeingMonitored(String video_id);

    /**
     * Check if this video is in the database
     *
     * @param video_id
     * @return
     */
    boolean checkVideoIfExistsInDB(String video_id);


    /**
     * Return true if 15 days have passed
     * @param video_id
     * @return
     */
    boolean checkVideoIsFinished(String video_id);
    /**
     * Add a new video record in the database.
     * We need to record  the timestamp of this transaction, since all the calculations are based on creation time
     * The information contained in this object should be all the static data of the video.
     * @param videoObject
     * @return
     */
    boolean addNewVideo(String video_id, JsonObject videoObject);

    /**
     * Add dynamic data
     * @param video_id
     * @param dynamicData
     * @return
     */
    boolean addDynamicData(String video_id, JsonObject dynamicData);

    /**
     * If the video id is found, it deletes it from videos collection
     * @param video_id
     * @return
     */
    boolean deleteVideo(String video_id);

    /**
     * Get the channel id of the video
     * @param videoId
     * @return
     */
    String getChannelId(String videoId);

    /**
     * Mark the video as incomplete
     * @param videoId
     * @return
     */
    boolean setVideoAsIncomplete(String videoId);
}
