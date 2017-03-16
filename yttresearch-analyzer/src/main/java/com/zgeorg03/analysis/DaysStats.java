package com.zgeorg03.analysis;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zgeorg03.analysis.models.Day;
import com.zgeorg03.utils.Calculations;
import com.zgeorg03.utils.JsonModel;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 3/5/17.
 */
public class DaysStats extends HashMap<Integer, DayStat> implements JsonModel{

    private final int lbl_wnd;

    public DaysStats(int lbl_wnd) {
        this.lbl_wnd = lbl_wnd;
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();

        JsonArray  array = new JsonArray();
        this.entrySet().stream().sorted(Comparator.comparing(Entry::getKey))
                .forEachOrdered(entry->array.add(entry.getValue().toJson()));

        object.add("array",array);
        return object;
    }

    @Override
    public JsonObject toJson(Map<String, Integer> view) {
        return toJson();
    }

    public void add(List<Day> days) {
        for(Day d : days){
            int key = d.getDay();
            DayStat dayStat = get(key);
            if(dayStat==null) {
                dayStat = new DayStat(key);
                put(key,dayStat);
            }
            dayStat.addDay(d);
        }
    }

    public List<Double> getViewsAverageDailyIncrease(int skip) {

        return this.entrySet().stream().sorted(Comparator.comparing(Entry::getKey))
                .skip(skip)
                .map(entry-> Calculations.averageLong(entry.getValue().view_added))
                .collect(Collectors.toList());
    }

    public List<Double> getTweetsAverageDailyIncrease(int lbl_wnd) {
        return this.entrySet().stream().sorted(Comparator.comparing(Entry::getKey))
                .skip(lbl_wnd)
                .map(entry-> Calculations.averageLong(entry.getValue().tweets_added))
                .collect(Collectors.toList());
    }

    public List<Double> getRatioOriginalTotalTweets(int lbl_wnd) {
        return this.entrySet().stream().sorted(Comparator.comparing(Entry::getKey))
                .skip(lbl_wnd)
                .map(entry-> Calculations.averageDouble(entry.getValue().ratio_original_tweets_total_tweets))
                .collect(Collectors.toList());
    }
    public List<Double> getAverageUsersReached(int lbl_wnd) {
        return this.entrySet().stream().sorted(Comparator.comparing(Entry::getKey))
                .skip(lbl_wnd)
                .map(entry-> Calculations.averageDouble(entry.getValue().user_followers))
                .collect(Collectors.toList());
    }
}
