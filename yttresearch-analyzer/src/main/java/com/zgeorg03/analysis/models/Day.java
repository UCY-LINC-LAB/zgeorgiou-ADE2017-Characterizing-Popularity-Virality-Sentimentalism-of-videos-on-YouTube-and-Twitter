package com.zgeorg03.analysis.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zgeorg03.utils.JsonModel;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zgeorg03 on 3/3/17.
 */
public class Day implements JsonModel{
    private final int day;
    private final String date;
    private final long views_added;
    private final long likes_added;
    private final long dislikes_added;
    private final long favorites_added;
    private final long comments_added;
    private final long channel_views_added;
    private final long channel_comments_added;
    private final long channel_subscribers_added;
    private final long channel_videos_added;
    private final long tweets_added;
    private final long original_tweets_added;
    private final long retweets_added;
    private final long tweets_favorited_added;
    private final long tweets_possibly_sensitive_added;

    private final Stat<Integer> user_days_created_before_video;
    private final Stat<Long> user_followers_count;
    private final Stat<Long> user_friends_count;

    private final SentimentJson tweets_sentiment;
    private final Map<String,Integer> language;
    private final Map<String,Integer> hashtags;

    /**
    private final long avg_user_favorites_count;

     **/
    public static class Builder{

        public static Day create(Document document){
            int day = document.getInteger("day");
            String date = document.getString("date");
            long views_added = document.getLong("views_added");
            long likes_added = document.getLong("likes_added");
            long dislikes_added = document.getLong("dislikes_added");
            long favorites_added = document.getLong("favorites_added");
            long comments_added = document.getLong("comments_added");
            long channel_views_added =  document.getLong("channel_views_added");
            long channel_comments_added =  document.getLong("channel_comments_added");
            long channel_subscribers_added =  document.getLong("channel_subscribers_added");
            long channel_videos_added =  document.getLong("channel_videos_added");
            long tweets_added =  document.getLong("tweets_added");
            long original_tweets_added =  document.getLong("original_tweets_added");
            long retweets_added =  document.getLong("retweets_added");
            long tweets_favorited_added =  document.getLong("tweets_favorited_added");
            long tweets_possibly_sensitive_added =  document.getLong("tweets_possibly_sensitive_added");

            double average_user_days_created_before_video =  document.getDouble("average_user_days_created_before_video");
            int median_user_days_created_before_video =  document.getInteger("median_user_days_created_before_video");
            double std_user_days_created_before_video =  document.getDouble("std_user_days_created_before_video");
            Stat<Integer> user_days_created_before_video = new Stat<>(average_user_days_created_before_video,median_user_days_created_before_video,std_user_days_created_before_video);

            double average_user_followers_count =  document.getDouble("average_user_followers_count");
            long median_user_followers_count =  document.getLong("median_user_followers_count");
            double std_user_followers_count =  document.getDouble("std_user_followers_count");
            Stat<Long> user_followers_count = new Stat<>(average_user_followers_count,median_user_followers_count,std_user_followers_count);

            double average_user_friends_count =  document.getDouble("average_user_friends_count");
            long median_user_friends_count =  document.getLong("median_user_friends_count");
            double std_user_friends_count =  document.getDouble("std_user_friends_count");
            Stat<Long> user_friends_count = new Stat<>(average_user_friends_count,median_user_friends_count,std_user_friends_count);

            SentimentJson tweets_sentiment;
            try {
                document.getString("tweets_sentiment");
                tweets_sentiment = new SentimentJson(true);
            }catch (ClassCastException ex){
                tweets_sentiment  = parseSentiment((Document)document.get("tweets_sentiment"));

            }

            List<Document> languageList = (List<Document>) document.get("language");
            Map<String,Integer> language = new HashMap<>();
            languageList.forEach(doc-> doc.keySet().forEach(key -> language.put(key,doc.getInteger(key))));

            List<Document> hashtagsList = (List<Document>) document.get("hashtags");
            Map<String,Integer> hashtags = new HashMap<>();
            hashtagsList.forEach(doc-> doc.keySet().forEach(key -> hashtags.put(key,doc.getInteger(key))));

            return new Day(day, date,views_added,likes_added,dislikes_added,favorites_added,comments_added,
                    channel_views_added,channel_comments_added,channel_subscribers_added,channel_videos_added,tweets_added,
                    original_tweets_added,retweets_added,tweets_favorited_added,tweets_possibly_sensitive_added, user_days_created_before_video, user_followers_count, user_friends_count, tweets_sentiment, language, hashtags);
        }

        private static SentimentJson parseSentiment(Document document) {
            Stat<Double> neg = new Stat<Double>((double) 0,(Document)document.get("neg"));
            Stat<Double> pos = new Stat<Double>((double) 0,(Document)document.get("pos"));
            Stat<Double> neu = new Stat<Double>((double) 0,(Document)document.get("neu"));
            Stat<Double> compound = new Stat<Double>((double) 0,(Document)document.get("compound"));
            return new SentimentJson(neg,pos,neu,compound);
        }
    }
    private Day(int day, String date, long views_added, long likes_added, long dislikes_added, long favorites_added, long comments_added,
                long channel_views_added, long channel_comments_added, long channel_subscribers_added, long channel_videos_added, long tweets_added, long original_tweets_added,
                long retweets_added, long tweets_favorited_added, long tweets_possibly_sensitive_added
            , Stat<Integer> user_days_created_before_video, Stat<Long> user_followers_count, Stat<Long> user_friends_count, SentimentJson tweets_sentiment, Map<String, Integer> language, Map<String, Integer> hashtags){
        this.day = day;
        this.date = date;
        this.views_added = views_added;
        this.likes_added = likes_added;
        this.dislikes_added = dislikes_added;
        this.favorites_added = favorites_added;
        this.comments_added = comments_added;
        this.channel_views_added = channel_views_added;
        this.channel_comments_added = channel_comments_added;
        this.channel_subscribers_added = channel_subscribers_added;
        this.channel_videos_added = channel_videos_added;
        this.tweets_added = tweets_added;
        this.original_tweets_added = original_tweets_added;
        this.retweets_added = retweets_added;
        this.tweets_favorited_added = tweets_favorited_added;
        this.tweets_possibly_sensitive_added = tweets_possibly_sensitive_added;
        this.user_days_created_before_video = user_days_created_before_video;
        this.user_followers_count = user_followers_count;
        this.user_friends_count = user_friends_count;
        this.tweets_sentiment = tweets_sentiment;
        this.language = language;
        this.hashtags = hashtags;
    }

    public long getViews_added() {
        return views_added;
    }

    public long getLikes_added() {
        return likes_added;
    }

    public long getDislikes_added() {
        return dislikes_added;
    }

    public long getComments_added() {
        return comments_added;
    }

    public long getTweets_added() {
        return tweets_added;
    }

    public long getOriginal_tweets_added() {
        return original_tweets_added;
    }

    public long getRetweets_added() {
        return retweets_added;
    }

    public long getFavorites_added() {
        return favorites_added;
    }

    public long getChannel_views_added() {
        return channel_views_added;
    }

    public long getChannel_comments_added() {
        return channel_comments_added;
    }

    public long getChannel_subscribers_added() {
        return channel_subscribers_added;
    }

    public long getChannel_videos_added() {
        return channel_videos_added;
    }

    public String getDate() {
        return date;
    }

    public Stat<Long> getUser_followers_count() {
        return user_followers_count;
    }

    public Stat<Long> getUser_friends_count() {
        return user_friends_count;
    }

    public Stat<Integer> getUser_days_created_before_video() {
        return user_days_created_before_video;
    }

    @Override
    public JsonObject toJson() {
        JsonObject result = new JsonObject();
        result.addProperty("date",date);
        result.addProperty("views_added",views_added);
        result.addProperty("likes_added", likes_added);
        result.addProperty("dislikes_added",dislikes_added);
        result.addProperty("favorites_added",favorites_added);
        result.addProperty("comments_added",comments_added);
        result.addProperty("channel_views_added",channel_views_added);
        result.addProperty("channel_comments_added",channel_comments_added);
        result.addProperty("channel_subscribers_added",channel_subscribers_added);
        result.addProperty("channel_videos_added",channel_videos_added);
        result.addProperty("tweets_added",tweets_added);
        result.addProperty("original_tweets_added",original_tweets_added);
        result.addProperty("retweets_added",retweets_added);
        result.addProperty("tweets_favorited_added", tweets_favorited_added);
        result.addProperty("tweets_possibly_sensitive_added",tweets_possibly_sensitive_added);

        result.add("user_days_created_before_video",user_days_created_before_video.toJson());
        result.add("user_followers_count",user_followers_count.toJson());
        result.add("user_friends_count",user_friends_count.toJson());
        result.add("tweets_sentiment",tweets_sentiment.toJson());

        JsonObject language = new JsonObject();
        result.add("language", fromMapToArray(this.language));

        JsonObject hashtags = new JsonObject();
        result.add("hashtags", fromMapToArray(this.hashtags));
        return result;
    }

    @Override
    public JsonObject toJson(Map<String, Integer> view) {
        return toJson();
    }


    private JsonArray fromMapToArray(Map<String,Integer> map){
        JsonArray array = new JsonArray();
        map.entrySet().stream().sorted((e1,e2)->e2.getValue().compareTo(e1.getValue())).forEachOrdered(entry->{
            JsonObject object = new JsonObject();
            object.addProperty(entry.getKey(),entry.getValue());
            array.add(object);
        });
        return array;
    }

    public SentimentJson getTweets_sentiment() {
        return tweets_sentiment;
    }

    public int getDay() {
        return day;
    }
}
