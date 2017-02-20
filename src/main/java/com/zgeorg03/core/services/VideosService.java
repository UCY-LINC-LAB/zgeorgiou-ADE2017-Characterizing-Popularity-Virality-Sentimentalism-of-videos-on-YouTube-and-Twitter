package com.zgeorg03.core.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zgeorg03.core.models.Day;
import com.zgeorg03.core.models.VideoRecord;
import com.zgeorg03.core.models.VideoRecords;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 2/20/17.
 */
public class VideosService {

    private final VideoRecords videoRecords;

    public VideosService(VideoRecords videoRecords) {
        this.videoRecords = videoRecords;
    }


    /**
     * Return video. If not found, it returns error:Video not found
     * @param id
     * @return
     */
    public JsonObject getVideo(String id){
        JsonObject object = new JsonObject();
        VideoRecord record = videoRecords.get(id);
        if(record==null) {
             object.addProperty("error", "Video: "+ id +" not found!");
             return object;
        }
        object.add("video",record.toJson());
        return  object;
    }

    public JsonObject getMostPopularInLabelWindow(int labeling_wnd_start, int limit, int artificialCategory){
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray();
        Map<String,Integer> view = new HashMap<>();
        view.put("video_id",1);
        view.put("totalViewsInLabelWnd",labeling_wnd_start);
        view.put("totalViews",1);
        view.put("totalTweetsInLabelWnd",labeling_wnd_start);
        view.put("totalTweets",1);
        List<VideoRecord> records = videoRecords.getMostPopularInLabelWindow(labeling_wnd_start,limit,artificialCategory);

        records.forEach(x -> array.add(x.toJson(view)));
        object.addProperty("total_videos",array.size());

        Map<Integer,Double> avgViews = videoRecords.averageViewsPerDay(records,labeling_wnd_start);
        JsonArray avgViewsArray= new JsonArray();
        avgViews.entrySet().stream().forEach(entry->{
            JsonObject obj = new JsonObject();
            obj.addProperty(entry.getKey()+"",entry.getValue());
            avgViewsArray.add(obj);
        });
        object.add("avg_views",avgViewsArray);

        object.add("videos",array);
        return  object;
    }



    public JsonArray getMostViralInLabelWindow(int labeling_wnd_start, int limit, int artificialCategory){
        JsonArray array = new JsonArray();
        Map<String,Integer> view = new HashMap<>();
        view.put("video_id",1);
        view.put("totalTweetsInLabelWnd",labeling_wnd_start);
        view.put("totalTweets",1);
        view.put("totalViewsInLabelWnd",labeling_wnd_start);
        view.put("totalViews",1);
        List<VideoRecord> records = videoRecords.getMostViralInLabelWindow(labeling_wnd_start,limit,artificialCategory);


        records.forEach(x -> array.add(x.toJson(view)));
        return  array;
    }


    /**
     * Get random recent videos
     * @param limit
     * @param range
     * @param rnd
     * @param artificialCategory
     * @return
     */
    public JsonArray getRecentVideos(int limit, int range, Random rnd, int artificialCategory){
        JsonArray array = new JsonArray();
        Map<String,Integer> view = new HashMap<>();
        view.put("video_id",1);
        view.put("totalTweets",1);
        view.put("totalViews",1);
        List<VideoRecord> records = videoRecords.getRecentVideos(limit,range,rnd,artificialCategory);


        records.forEach(x -> array.add(x.toJson(view)));
        return  array;
    }
    /**
     * Get random videos
     * @param limit
     * @param rnd
     * @param artificialCategory
     * @return
     */
    public JsonArray getRandomVideos(int limit,Random rnd, int artificialCategory){
        JsonArray array = new JsonArray();
        Map<String,Integer> view = new HashMap<>();
        view.put("video_id",1);
        view.put("totalTweets",1);
        view.put("totalViews",1);
        List<VideoRecord> records = videoRecords.getRandomVideos(limit,rnd,artificialCategory);
        records.forEach(x -> array.add(x.toJson(view)));
        return  array;
    }
    /**
     * Get total number of videos
     * @return
     */
    public int getTotalVideos(){
        return videoRecords.size();
    }

    public long getTotalVideosFromCategory(int category){
        return videoRecords.values().stream().filter(x->(category==0)?true:x.getArtificial_category()==category).count();
    }

    public VideoRecords getVideoRecords() {
        return videoRecords;
    }
}
