package com.zgeorg03.core.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zgeorg03.core.utils.JsonModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by zgeorg03 on 12/2/16.
 */
public class Day implements JsonModel {

    private final Date date;
    private final int views;
    private final int likes;
    private final int dislikes;
    private final int comments;
    private final int total_tweets;
    private final int original_tweets;
    private final int quotes;
    private final int retweets;
    private final int total_user_followers;
    private final int total_user_favorites;
    private final int tw_en_language;
    private final int tw_user_en_language;
    private final int tw_user_statuses;
    private final int tw_user_friends;
    private final int tw_user_verified;
    private final int hashtags;

    public int getViews() {
        return views;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public int getComments() {
        return comments;
    }

    public int getTotal_tweets() {
        return total_tweets;
    }

    public int getOriginal_tweets() {
        return original_tweets;
    }

    public int getQuotes() {
        return quotes;
    }

    public int getRetweets() {
        return retweets;
    }

    public Day(Date date, int views, int likes, int dislikes, int comments, int total_tweets, int original_tweets, int quotes, int retweets, int total_user_followers, int total_user_favorites, int tw_en_language, int tw_user_en_language, int tw_user_statuses, int tw_user_friends, int tw_user_verified, int hashtags) {
        this.date = date;
        this.views = views;
        this.likes = likes;
        this.dislikes = dislikes;
        this.comments = comments;
        this.total_tweets = total_tweets;
        this.original_tweets = original_tweets;
        this.quotes = quotes;
        this.retweets = retweets;
        this.total_user_followers = total_user_followers;
        this.total_user_favorites = total_user_favorites;
        this.tw_en_language = tw_en_language;
        this.tw_user_en_language = tw_user_en_language;
        this.tw_user_statuses = tw_user_statuses;
        this.tw_user_friends = tw_user_friends;
        this.tw_user_verified = tw_user_verified;
        this.hashtags = hashtags;
    }

    private static DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("date",fmt.format(date));
        object.addProperty("views",views);
        object.addProperty("likes",likes);
        object.addProperty("dislikes",dislikes);
        object.addProperty("comments",comments);
        object.addProperty("total_tweets",total_tweets);
        object.addProperty("original_tweets",original_tweets);
        object.addProperty("quotes", quotes);
        object.addProperty("retweets",retweets);

        object.addProperty("total_user_followers",total_user_followers);
        object.addProperty("total_user_favorites",total_user_favorites);
        object.addProperty("tw_en_language",tw_en_language);
        object.addProperty("tw_user_en_language",tw_user_en_language);
        object.addProperty("tw_user_statuses",tw_user_statuses);
        object.addProperty("tw_user_friends",tw_user_friends);
        object.addProperty("tw_user_verified",tw_user_verified);
        object.addProperty("hashtags",hashtags);
        return object;
    }

    @Override
    public JsonElement toJson(Map<String,Integer> map) {
        JsonObject object = new JsonObject();

        if(map.getOrDefault("views",0)==1)
            object.addProperty("views",views);

        if(map.getOrDefault("likes",0)==1)
            object.addProperty("likes",likes);

        if(map.getOrDefault("dislikes",0)==1)
            object.addProperty("dislikes",dislikes);

        if(map.getOrDefault("comments",0)==1)
            object.addProperty("comments",comments);

        if(map.getOrDefault("total_tweets",0)==1)
            object.addProperty("total_tweets",total_tweets);

        if(map.getOrDefault("original_tweets",0)==1)
            object.addProperty("original_tweets",original_tweets);

        if(map.getOrDefault("quotes",0)==1)
            object.addProperty("quotes", quotes);

        if(map.getOrDefault("retweets",0)==1)
            object.addProperty("retweets",retweets);

        if(map.getOrDefault("total_user_followers",0)==1)
            object.addProperty("total_user_followers",total_user_followers);

        if(map.getOrDefault("total_user_favorites",0)==1)
            object.addProperty("total_user_favorites",total_user_favorites);

        if(map.getOrDefault("tw_en_language",0)==1)
            object.addProperty("tw_en_language",tw_en_language);

        if(map.getOrDefault("tw_user_en_language",0)==1)
            object.addProperty("tw_user_en_language",tw_user_en_language);

        if(map.getOrDefault("tw_user_statuses",0)==1)
            object.addProperty("tw_user_statuses",tw_user_statuses);

        if(map.getOrDefault("tw_user_friends",0)==1)
            object.addProperty("tw_user_friends",tw_user_friends);

        if(map.getOrDefault("tw_user_verified",0)==1)
            object.addProperty("tw_user_verified",tw_user_verified);

        if(map.getOrDefault("hashtags",0)==1)
            object.addProperty("hashtags",hashtags);

        return object;
    }

    public int getTotal_user_followers() {
        return total_user_followers;
    }

    public int getTotal_user_favorites() {
        return total_user_favorites;
    }

    public int getTw_en_language() {
        return tw_en_language;
    }

    public int getTw_user_en_language() {
        return tw_user_en_language;
    }

    public int getTw_user_statuses() {
        return tw_user_statuses;
    }

    public int getTw_user_friends() {
        return tw_user_friends;
    }

    public int getTw_user_verified() {
        return tw_user_verified;
    }

    public int getHashtags() {
        return hashtags;
    }
}
