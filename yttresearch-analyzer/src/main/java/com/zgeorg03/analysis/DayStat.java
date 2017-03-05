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

    List<Long> view_added = new LinkedList<>();

    List<Long> tweets_added = new LinkedList<>();
    List<Long> retweets_added = new LinkedList<>();
    List<Double> ratio_original_tweets_total_tweets = new LinkedList<>();

    List<Long> likes_added = new LinkedList<>();
    List<Long> dislikes_added = new LinkedList<>();
    List<Double> ratio_likes = new LinkedList<>();

    List<Double> user_followers = new LinkedList<>();
    List<Double> average_user_friends = new LinkedList<>();
    List<Double> average_user_days_created_before_video = new LinkedList<>();

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.add("views_added",Calculations.getStatsLong(view_added).toJson());
        object.add("tweets_added",Calculations.getStatsLong(tweets_added).toJson());
        object.add("retweets_added",Calculations.getStatsLong(retweets_added).toJson());
        object.add("ratio_original_tweets_total_tweets",Calculations.getStatsDouble(ratio_original_tweets_total_tweets).toJson());
        object.add("likes_added",Calculations.getStatsLong(likes_added).toJson());
        object.add("dislikes_added",Calculations.getStatsLong(dislikes_added).toJson());
        object.add("ratio_likes",Calculations.getStatsDouble(ratio_likes).toJson());
        object.add("average_user_followers",Calculations.getStatsDouble(user_followers).toJson());
        object.add("average_user_friends",Calculations.getStatsDouble(average_user_friends).toJson());
        object.add("average_user_days_created_before_video",Calculations.getStatsDouble(average_user_days_created_before_video).toJson());

        return object;
    }

    @Override
    public JsonObject toJson(Map<String, Integer> view) {
        return toJson();
    }

    public void addDay(Day d) {
        view_added.add(d.getViews_added());
          tweets_added.add(d.getTweets_added());
        retweets_added.add(d.getRetweets_added());
        if(d.getTweets_added()!=0)
            ratio_original_tweets_total_tweets.add(d.getOriginal_tweets_added()/(double)d.getTweets_added());
        likes_added.add(d.getLikes_added());
        dislikes_added.add(d.getDislikes_added());

        if(d.getDislikes_added()!=0)
            ratio_likes.add(d.getLikes_added()/(double)(d.getLikes_added()+d.getDislikes_added()));
        user_followers.add(d.getUser_followers_count().getAverage());
        average_user_friends.add(d.getUser_friends_count().getAverage());
        average_user_days_created_before_video.add(d.getUser_days_created_before_video().getAverage());

    }
}
