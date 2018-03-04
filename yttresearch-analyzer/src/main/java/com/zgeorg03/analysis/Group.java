package com.zgeorg03.analysis;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zgeorg03.analysis.models.Day;
import com.zgeorg03.analysis.models.Stat;
import com.zgeorg03.analysis.models.Video;
import com.zgeorg03.utils.Calculations;
import com.zgeorg03.utils.DateUtil;
import com.zgeorg03.utils.JsonModel;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 3/5/17.
 */
public class Group extends LinkedList<Video> implements JsonModel{

    private final String name;

    private final DaysStats daysStats;

    private final boolean showDailyStats;
    private final int offset;
    private final int lbl_wnd;
    public Group(String name, boolean showDailyStats, int offset, int lbl_wnd) {
        this.name = name;
        this.showDailyStats = showDailyStats;
        this.offset =offset;
        this.lbl_wnd = lbl_wnd;
        daysStats = new DaysStats();
    }

    public void addVideo(Video video){
        daysStats.add(video.getDays());
        this.add(video);
    }

    private List<Long> totalViewsList(){
        return this.stream().map(x->x.getDays().stream().skip(offset).limit(lbl_wnd-offset).mapToLong(Day::getViews_added).sum()).collect(Collectors.toList());
    }

    private List<Long> totalTweetsList(){
        return this.stream().map(x->x.getDays().stream().skip(offset).limit(lbl_wnd-offset).mapToLong(Day::getTweets_added).sum()).collect(Collectors.toList());
    }

    private List<Long> totalLikesList(){
        return this.stream().map(x->x.getDays().stream().skip(offset).limit(lbl_wnd-offset).mapToLong(Day::getLikes_added).sum()).collect(Collectors.toList());
    }

    private List<Long> totalDislikesList(){
        return this.stream().map(x->x.getDays().stream().skip(offset).limit(lbl_wnd-offset).mapToLong(Day::getDislikes_added).sum()).collect(Collectors.toList());
    }
    private List<Integer> durationList(){
        return this.stream().map(x->((int)x.getDuration()/1000)).collect(Collectors.toList());
    }
    private List<Double> negativeSentimentList(){
        return this.stream().filter(x->x.getComments_sentiment().isValid()).map(x->x.getComments_sentiment().getNeg().getAverage()).collect(Collectors.toList());
    }
    private List<Double> positiveSentimentList(){
        return this.stream().filter(x->x.getComments_sentiment().isValid()).map(x->x.getComments_sentiment().getPos().getAverage()).collect(Collectors.toList());
    }
    private List<Double> neutralSentimentList(){
        return this.stream().filter(x->x.getComments_sentiment().isValid()).map(x->x.getComments_sentiment().getNeu().getAverage()).collect(Collectors.toList());
    }
    private List<Double> compoundSentimentList(){
        return this.stream().filter(x->x.getComments_sentiment().isValid()).map(x->x.getComments_sentiment().getCompound().getAverage()).collect(Collectors.toList());
    }

    @Override
    public JsonObject toJson() {
        JsonObject object =  new JsonObject();
        object.addProperty("size",this.size());

        object.add("duration", Calculations.getStatsInt(durationList()).toJson());
        object.add("total_views", Calculations.getStatsLong(totalViewsList()).toJson());
        object.add("total_tweets", Calculations.getStatsLong(totalTweetsList()).toJson());
        object.add("total_likes", Calculations.getStatsLong(totalLikesList()).toJson());
        object.add("total_dislikes", Calculations.getStatsLong(totalDislikesList()).toJson());
        object.add("negative_sentiment", Calculations.getStatsDouble(negativeSentimentList()).toJson());
        object.add("positive_sentiment", Calculations.getStatsDouble(positiveSentimentList()).toJson());
        object.add("neutral_sentiment", Calculations.getStatsDouble(neutralSentimentList()).toJson());
        object.add("compound_sentiment", Calculations.getStatsDouble(compoundSentimentList()).toJson());
        if(showDailyStats)
            object.add("days", daysStats.toJson().get("array"));


        return object;
    }

    @Override
    public JsonObject toJson(Map<String, Integer> view) {
        return toJson();
    }

    public List<Double> getViewsAverageDailyIncrease(int skip) { return daysStats.getViewsAverageDailyIncrease(skip); }
    public List<Double> getViewsMedianDailyIncrease(int skip) { return daysStats.getViewsMedianDailyIncrease(skip); }
    public List<Double> getTweetsAverageDailyIncrease(int skip) { return daysStats.getTweetsAverageDailyIncrease(skip); }
    public List<Double> getTweetsMedianDailyIncrease(int skip) { return daysStats.getTweetsMedianDailyIncrease(skip); }
    public List<Double> getRatioOriginalTotalTweets(int skip) { return daysStats.getRatioOriginalTotalTweets(skip); }
    public List<Double> getAverageUsersReached(int skip) { return daysStats.getAverageUsersReached(skip); }
    public List<Double> getMedianUsersReached(int skip) { return daysStats.getMedianUsersReached(skip); }

    public Stat<Integer> getAverageDuration() { return Calculations.getStatsInt(durationList()); }
    public Stat<Double> getAverageNegativeSentiment() {
        return Calculations.getStatsDouble(negativeSentimentList());
    }
    public Stat<Double> getAveragePositiveSentiment() { return Calculations.getStatsDouble(positiveSentimentList()); }
    public Stat<Double> getAverageNeutralSentiment() { return Calculations.getStatsDouble(neutralSentimentList()); }

    public List<Map.Entry<Integer,Double>> getVideosAgeDistribution(){
        if(size()==0)
            return new LinkedList<>();
        Map<Integer,Integer> freq = new HashMap<>();
        for(Video video : this){
            long experiment_time =  video.getCollected_at();
            long video_time =  video.getPublished_at();
            long diff = experiment_time - video_time;
            int day = (int) (diff/DateUtil.dayInMillis);
            freq.compute(day,(k,v)->(v==null)?1:v+1);
        }
        int sum = freq.entrySet().stream().mapToInt(Map.Entry::getValue).sum();
        Map<Integer,Double> normalized = new HashMap<>();
        freq.entrySet().stream().forEach(x-> normalized.put(x.getKey(),x.getValue()/((double) sum)));
        return normalized.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).collect(Collectors.toList());
    }


    public JsonElement getInfo() {
        JsonObject object = new JsonObject();
        object.addProperty("total_videos",this.size());
        return object;
    }


    public String getName() {
        return name;
    }
}
