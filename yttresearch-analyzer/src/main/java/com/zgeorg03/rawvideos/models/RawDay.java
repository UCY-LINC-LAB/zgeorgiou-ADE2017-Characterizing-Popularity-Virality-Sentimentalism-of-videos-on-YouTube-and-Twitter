package com.zgeorg03.rawvideos.models;

import com.zgeorg03.analysis.SentimentAnalysis;
import com.zgeorg03.analysis.models.Stat;
import com.zgeorg03.utils.BsonModel;
import com.zgeorg03.utils.Calculations;
import com.zgeorg03.utils.DateUtil;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 3/3/17.
 */
public class RawDay implements BsonModel{
    private final Logger logger = LoggerFactory.getLogger(RawDay.class);

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

    private  long tweets_added;
    private  long original_tweets_added;
    private  long retweets_added;
    private  long tweets_favorites_added;
    private  long tweets_possibly_sensitive_added;
    private  long users_verified_count;

    private ArrayList<Integer> list_user_days_created_before_video  = new ArrayList<>();
    private ArrayList<Long> list_user_followers_count= new ArrayList<>();
    private ArrayList<Long> list_user_friends_count= new ArrayList<>();
    private ArrayList<Long> list_user_statuses_count= new ArrayList<>();

    private List<String> list_english_text= new ArrayList<>();


    private Map<String,Integer> language = new HashMap<>();
    private Map<String,Integer> hashtags = new HashMap<>();
    private final SentimentAnalysis sentimentAnalysis;

    public RawDay(int day, String date, long views_added, long likes_added, long dislikes_added, long favorites_added, long comments_added, long channel_views_added, long channel_comments_added, long channel_subscribers_added, long channel_videos_added, SentimentAnalysis sentimentAnalysis){
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
        this.sentimentAnalysis = sentimentAnalysis;
    }

    @Override
    public String toString() {
        return "RawDay{" +
                "views_added=" + views_added +
                ", likes_added=" + likes_added +
                ", dislikes_added=" + dislikes_added +
                ", favorites_added=" + favorites_added +
                ", comments_added=" + comments_added +
                ", channel_views_added=" + channel_views_added +
                ", channel_comments_added=" + channel_comments_added +
                ", channel_subscribers_added=" + channel_subscribers_added +
                ", channel_videos_added=" + channel_videos_added +
                ", tweets_added=" + tweets_added +
                ", original_tweets_added=" + original_tweets_added +
                ", retweets_added=" + retweets_added +
                '}';
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



    @Override
    public Document toBson() {
        Document result = new Document();
        result.append("day",day);
        result.append("date",date);
        result.append("views_added",views_added);
        result.append("likes_added", likes_added);
        result.append("dislikes_added",dislikes_added);
        result.append("favorites_added",favorites_added);
        result.append("comments_added",comments_added);
        result.append("channel_views_added",channel_views_added);
        result.append("channel_comments_added",channel_comments_added);
        result.append("channel_subscribers_added",channel_subscribers_added);
        result.append("channel_videos_added",channel_videos_added);
        result.append("tweets_added",tweets_added);
        result.append("original_tweets_added",original_tweets_added);
        result.append("retweets_added",retweets_added);
        result.append("tweets_favorited_added",tweets_favorites_added);
        result.append("tweets_possibly_sensitive_added",tweets_possibly_sensitive_added);
        result.append("tweets_hashtags_added",hashtags.values().stream().mapToLong(i->i).sum());
        result.append("tweets_in_english_added",(long)list_english_text.size());
        result.append("users_verified_count",users_verified_count);

        double avg_user_days_created_before_video = Calculations.averageInt(list_user_days_created_before_video);
        result.append("average_user_days_created_before_video",avg_user_days_created_before_video);
        result.append("median_user_days_created_before_video", Calculations.medianInt(list_user_days_created_before_video));
        result.append("std_user_days_created_before_video", Calculations.stdInt(list_user_days_created_before_video,avg_user_days_created_before_video));

        double avg_user_followers_count = Calculations.averageLong(list_user_followers_count);
        result.append("average_user_followers_count",avg_user_followers_count);
        result.append("median_user_followers_count", Calculations.medianLong(list_user_followers_count));
        result.append("std_user_followers_count", Calculations.stdLong(list_user_followers_count,avg_user_followers_count));

        double avg_user_friends_count = Calculations.averageLong(list_user_friends_count);
        result.append("average_user_friends_count",avg_user_friends_count);
        result.append("median_user_friends_count", Calculations.medianLong(list_user_friends_count));
        result.append("std_user_friends_count", Calculations.stdLong(list_user_friends_count,avg_user_friends_count));

        result.append("user_statuses_count",Calculations.getStatsLong(list_user_statuses_count).toBson());
        try {
            if(list_english_text.isEmpty())
                result.append("tweets_sentiment","Not enough tweets");
            else
                result.append("tweets_sentiment",sentimentAnalysis.run(list_english_text).toBson());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            result.append("tweets_sentiment","Couldn't be calculated");
        }


        List<Document> language = this.language.entrySet().stream().sorted((e1,e2)-> e2.getValue().compareTo(e1.getValue())).map(entry -> new Document(entry.getKey(),entry.getValue())).limit(10).collect(Collectors.toList());
        result.append("language",language);

        List<Document> hashtags = this.hashtags.entrySet().stream().sorted((e1,e2)->e2.getValue().compareTo(e1.getValue())).map(entry -> new Document(entry.getKey(),entry.getValue())).limit(10).collect(Collectors.toList());
        result.append("hashtags",hashtags);
        return result;
    }

    public void setTweetStuff(long video_created_at, String lang, String text, boolean is_favorited, boolean is_possibly_sensitive, boolean is_retweet, long user_created_at, long user_followers_count, long user_friends_count, long user_favorites_count, long user_listed_count, long user_statuses_count, boolean user_verified, String user_lang, List<String> hashtags) {

        tweets_added++;
        if(is_retweet)
            retweets_added++;
        else
            original_tweets_added++;
        if(is_favorited)
            tweets_favorites_added++;
        if(is_possibly_sensitive)
            tweets_possibly_sensitive_added++;
        if(user_verified)
            users_verified_count++;


        //If its english
        if(lang.equalsIgnoreCase("en"))
            list_english_text.add(text);


        //Compute the number of days that the user was created before video
        int user_created_days_before_video = (int) ((video_created_at - user_created_at)/ DateUtil.dayInMillis);
        list_user_days_created_before_video.add(user_created_days_before_video);
        list_user_followers_count.add(user_followers_count);
        list_user_friends_count.add(user_friends_count);
        list_user_statuses_count.add(user_statuses_count);

        language.compute(lang,(k,v)->(v==null)?1:v+1);
        for(String hashtag:hashtags) {
            hashtag = hashtag.trim().toLowerCase();
            this.hashtags.compute(hashtag, (k, v) -> (v == null) ? 1 : v + 1);
        }

    }

}
