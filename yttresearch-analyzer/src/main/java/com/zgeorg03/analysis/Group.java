package com.zgeorg03.analysis;

import com.google.gson.JsonObject;
import com.zgeorg03.analysis.models.Video;
import com.zgeorg03.utils.Calculations;
import com.zgeorg03.utils.JsonModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 3/5/17.
 */
public class Group extends LinkedList<Video> implements JsonModel{

    private final String name;

    private final DaysStats daysStats;

    private final boolean showDailyStats;
    private final int lbl_wnd;
    public Group(String name, boolean showDailyStats,int lbl_wnd) {
        this.name = name;
        this.showDailyStats = showDailyStats;
        this.lbl_wnd=lbl_wnd;
        daysStats = new DaysStats(lbl_wnd);
    }

    public void addVideo(Video video){
        daysStats.add(video.getDays());
        this.add(video);
    }

    private List<Long> totalViewsList(){
        return this.stream().map(x->x.getDays().stream().skip(lbl_wnd).mapToLong(y->y.getViews_added()).sum()).collect(Collectors.toList());
    }

    private List<Long> totalTweetsList(){
        return this.stream().map(x->x.getDays().stream().skip(lbl_wnd).mapToLong(y->y.getTweets_added()).sum()).collect(Collectors.toList());
    }
    private List<Integer> durationList(){
        return this.stream().map(x->((int)x.getDuration()/1000)).collect(Collectors.toList());
    }

    @Override
    public JsonObject toJson() {
        JsonObject object =  new JsonObject();
        object.addProperty("size",this.size());

        object.add("duration", Calculations.getStatsInt(durationList()).toJson());
        object.add("total_views", Calculations.getStatsLong(totalViewsList()).toJson());
        object.add("total_tweets", Calculations.getStatsLong(totalTweetsList()).toJson());
        if(showDailyStats)
            object.add("days", daysStats.toJson());


        return object;
    }

    @Override
    public JsonObject toJson(Map<String, Integer> view) {
        return toJson();
    }

}
