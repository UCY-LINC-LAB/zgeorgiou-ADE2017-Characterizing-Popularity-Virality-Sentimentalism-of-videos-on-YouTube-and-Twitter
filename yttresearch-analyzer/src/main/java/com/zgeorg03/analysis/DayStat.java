package com.zgeorg03.analysis;

import com.google.gson.JsonObject;
import com.zgeorg03.analysis.models.Day;
import com.zgeorg03.utils.Calculations;
import com.zgeorg03.utils.JsonModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by zgeorg03 on 3/5/17.
 */
public class DayStat implements JsonModel{

    List<Long> view_added = new LinkedList<Long>();
    List<Long> tweets_added = new LinkedList<Long>();
    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.add("views_added",Calculations.getStatsLong(view_added).toJson());
        object.add("tweets_added",Calculations.getStatsLong(tweets_added).toJson());

        return object;
    }

    @Override
    public JsonObject toJson(Map<String, Integer> view) {
        return toJson();
    }

    public void addDay(Day d) {
        view_added.add(d.getViews_added());
        tweets_added.add(d.getTweets_added());
    }
}
