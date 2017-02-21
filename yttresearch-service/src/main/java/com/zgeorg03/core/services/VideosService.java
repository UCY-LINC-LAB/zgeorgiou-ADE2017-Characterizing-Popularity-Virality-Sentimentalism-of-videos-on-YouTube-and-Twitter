package com.zgeorg03.core.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zgeorg03.core.calculations.Average;
import com.zgeorg03.core.calculations.Median;
import com.zgeorg03.core.calculations.StandardDeviation;
import com.zgeorg03.core.models.VideoRecord;
import com.zgeorg03.core.models.VideoRecords;
import com.zgeorg03.core.utils.DataInputHandler;

import java.util.*;
import java.util.stream.Collector;
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

    public JsonObject getMostPopularInLabelWindow(boolean statsEnabled,int labeling_wnd_start, int limit, int artificialCategory){
        JsonObject object = new JsonObject();
        JsonArray videos = new JsonArray();
        Map<String,Integer> view = new HashMap<>();
        view.put("video_id",1);
        view.put("totalViewsInLabelWnd",labeling_wnd_start);
        view.put("totalViews",1);
        view.put("totalTweetsInLabelWnd",labeling_wnd_start);
        view.put("totalTweets",1);
        view.put("img",1);
        List<VideoRecord> records = videoRecords.getMostPopularInLabelWindow(labeling_wnd_start,limit,artificialCategory);

        records.forEach(x -> videos.add(x.toJson(view)));
        object.addProperty("total_videos",videos.size());


        if(statsEnabled) {
            JsonObject stats = getStats(records, labeling_wnd_start);
            object.add("stats", stats);
        }

        object.add("videos",videos);
        return  object;
    }


    public JsonObject getMostViralInLabelWindow(boolean statsEnabled, int labeling_wnd_start, int limit, int artificialCategory){
        JsonObject object = new JsonObject();
        JsonArray videos = new JsonArray();
        Map<String,Integer> view = new HashMap<>();
        view.put("video_id",1);
        view.put("totalTweetsInLabelWnd",labeling_wnd_start);
        view.put("totalTweets",1);
        view.put("totalViewsInLabelWnd",labeling_wnd_start);
        view.put("totalViews",1);
        view.put("img",1);
        List<VideoRecord> records = videoRecords.getMostViralInLabelWindow(labeling_wnd_start,limit,artificialCategory);

        records.forEach(x -> videos.add(x.toJson(view)));
        object.addProperty("total_videos",videos.size());

        if(statsEnabled) {
            JsonObject stats = getStats(records, labeling_wnd_start);
            object.add("stats", stats);
        }

        return  object;
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

    /**
     * Return an object with statistics for the given group of video records
     * @param records
     * @param lbl_wnd
     * @return
     */
    private JsonObject getStats(List<VideoRecord> records,int lbl_wnd){
        JsonObject stats= new JsonObject();

        //Compute daily stats
        JsonArray array= getDailyStats(records,lbl_wnd);

        stats.add("daily",array);

        return stats;
    }

    /**
     * Get daily stats for video group
     * @param records
     * @param lbl_wnd
     * @return
     */
    private JsonArray getDailyStats(List<VideoRecord> records,int lbl_wnd){

        Map<Integer,Double> avgViews = Average.ViewsPerDay(records, lbl_wnd);
        Map<Integer,Double> avgViewsStd = StandardDeviation.viewsPerDay(records,lbl_wnd,avgViews);
        Map<Integer,Integer> viewsMedian = Median.viewsPerDay(records,lbl_wnd);

        Map<Integer,Double> avgTweets = Average.tweetsPerDay(records,lbl_wnd);
        Map<Integer,Double> avgTweetsStd = StandardDeviation.tweetsPerDay(records,lbl_wnd,avgTweets);
        Map<Integer,Integer> tweetsMedian = Median.tweetsPerDay(records,lbl_wnd);

        JsonArray array = new JsonArray();
        for(int day :avgViews.keySet()){
            JsonObject object = new JsonObject();
            object.addProperty("day",day);

            int viewsMedianDay = viewsMedian.get(day);
            object.addProperty("views_median",viewsMedianDay);
            double avgViewsDay = avgViews.get(day);
            object.addProperty("avg_views",avgViewsDay);
            double avgViewsStdDay = avgViewsStd.get(day);
            object.addProperty("avg_views_std",String.format("%.2f",avgViewsStdDay));


            int tweetsMedianDay = tweetsMedian.get(day);
            object.addProperty("tweets_median",tweetsMedianDay);
            double avgTweetsDay = avgTweets.get(day);
            object.addProperty("avg_tweets",avgTweetsDay);
            double avgTweetsStdDay = avgTweetsStd.get(day);
            object.addProperty("avg_tweets_std",String.format("%.2f",avgTweetsStdDay));

            array.add(object);
        }
        return array;
    }

    public JsonArray getCategories() {
        JsonArray array = new JsonArray();
        Map<Integer,List<VideoRecord>> groups = videoRecords.values().stream().collect(
                Collectors.groupingBy( VideoRecord::getArtificial_category,Collectors.toList()));
        groups.entrySet().forEach(entry->{
            JsonObject object = new JsonObject();
            String name = DataInputHandler.getArtificialCategoryName(entry.getKey());
            int total = entry.getValue().size();
            object.addProperty("name",name);
            object.addProperty("total_videos",total);
            array.add(object);
        });

        return array;
    }
}
