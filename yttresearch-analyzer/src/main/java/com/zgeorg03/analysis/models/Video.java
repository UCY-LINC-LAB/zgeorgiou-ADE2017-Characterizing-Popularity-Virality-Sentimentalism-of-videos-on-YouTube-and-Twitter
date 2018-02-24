package com.zgeorg03.analysis.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zgeorg03.analysis.SentimentAnalysis;
import com.zgeorg03.classification.records.DayRecord;
import com.zgeorg03.classification.records.VideoRecord;
import com.zgeorg03.utils.DateUtil;
import com.zgeorg03.utils.JsonModel;
import org.bson.Document;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 3/2/17.
 */
public class Video implements JsonModel{

    //Static
    private final String video_id;
    private final String title;
    private final String description;
    private final int category;
    private final int artificial_category;
    private final long published_at;
    private final long collected_at;
    private final long duration;

    private final SentimentJson comments_sentiment;
    private final List<Day> days;

    //Count these
    private final  long total_views;
    private final  long total_likes;
    private final  long total_dislikes;
    private final  long total_comments;
    private final  long total_tweets;
    private final  long total_original_tweets;
    private final  long total_retweets;
    private final  long total_channel_views;
    private final  long total_channel_comments;
    private final  long total_channel_subscribers;
    private final  long total_channel_videos;

    public String getCsvTitles() {
        return "#video_id\t"
                + "category\t"
                + "artificial_category\t"
                + "published_at_timestamp\t"
                + "collected_at_timestamp\t"
                + "duration\t"
                + "comments_sentiment_neg\t"
                + "comments_sentiment_neu\t"
                + "comments_sentiment_pos\t"
                + "comments_sentiment_compound\t"
                + days.stream().map(Day::getCsvTitle).collect(Collectors.joining("\t"))
                + "popular\t"+"viral"
                ;
    }
    public String getCsvForm(boolean isPopular,boolean isViral) {
        return video_id + "\t"
                + category +"\t"
                + artificial_category +"\t"
                + published_at +"\t"
                + collected_at +"\t"
                + duration +"\t"
                + ((comments_sentiment.isValid())?(
                        String.format("%.4f\t",comments_sentiment.getNeg().getAverage())+
                        String.format("%.4f\t",comments_sentiment.getNeu().getAverage())+
                        String.format("%.4f\t",comments_sentiment.getPos().getAverage())+
                        String.format("%.4f",comments_sentiment.getCompound().getAverage()))
                :
                ("0\t0\t0\t0"))
                + "\t"+ days.stream().map(Day::getCsvForm).collect(Collectors.joining("\t"))
                +"\t"+ ((isPopular)?1:0)
                +"\t"+ ((isViral)?1:0)
                ;
    }

    public double getAverageViewsPerDay() {
        return days.stream().skip(1).mapToLong(Day::getViews_added).average().getAsDouble();
    }
    public double getAverageTweetsPerDay() {
        return days.stream().skip(1).mapToLong(Day::getTweets_added).average().getAsDouble();
    }
    public double getAverageRetweetsPerDay() {
        return days.stream().skip(1).mapToLong(Day::getRetweets_added).average().getAsDouble();
    }
    public double getAverageLikesPerDay() {
        return days.stream().skip(1).mapToLong(Day::getLikes_added).average().getAsDouble();
    }
    public double getAverageFollowersPerDay() {
        return days.stream().skip(1).mapToDouble(x->x.getUser_followers_count().getAverage()).average().getAsDouble();
    }
    public double getAverageFriendsPerDay() {
        return days.stream().skip(1).mapToDouble(x->x.getUser_friends_count().getAverage()).average().getAsDouble();
    }


    public static class Builder {

        public Video create(Document document){
            String video_id = document.getString("_id");
            String title = document.getString("title");
            String description = document.getString("description");
            int category = document.getInteger("category");
            int artificial_category = document.getInteger("artificial_category");
            long published_at = document.getLong("published_at_timestamp");
            long collected_at = document.getLong("collected_at_timestamp");
            long duration = document.getLong("duration");
            long total_views = document.getLong("total_views");
            long total_likes = document.getLong("total_likes");
            long total_dislikes = document.getLong("total_dislikes");
            long total_comments = document.getLong("total_comments");
            long total_tweets = document.getLong("total_tweets");
            long total_original_tweets = document.getLong("total_original_tweets");
            long total_retweets = document.getLong("total_retweets");
            long total_channel_views = document.getLong("total_channel_views");
            long total_channel_comments = document.getLong("total_channel_comments");
            long total_channel_subscribers = document.getLong("total_channel_subscribers");
            long total_channel_videos = document.getLong("total_channel_videos");

            SentimentJson comments_sentiment;
            try {
                document.getString("comments_sentiment");
                comments_sentiment = new SentimentJson(true);
            }catch (ClassCastException ex){
                comments_sentiment  = SentimentAnalysis.parseSentiment((Document)document.get("comments_sentiment"));

            }
            List<Document> documents = (List<Document>) document.get("days");
            List<Day> days = new LinkedList<>();

            for(Document doc : documents) {
                days.add(Day.Builder.create(doc));
            }
            return  new Video(video_id, title, description, category, artificial_category, published_at, collected_at, duration,
                    comments_sentiment, total_views, total_likes, total_dislikes, total_comments, total_tweets, total_original_tweets,
                    total_retweets, total_channel_views, total_channel_comments, total_channel_subscribers, total_channel_videos,days);
        }
    }
    public Video(String video_id, String title, String description, int category, int artificial_category, long published_at, long collected_at, long duration,
                 SentimentJson comments_sentiment, long total_views, long total_likes, long total_dislikes, long total_comments, long total_tweets, long total_original_tweets,
                 long total_retweets, long total_channel_views, long total_channel_comments, long total_channel_subscribers, long total_channel_videos, List<Day> days){

        this.video_id = video_id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.artificial_category = artificial_category;
        this.published_at = published_at;
        this.collected_at = collected_at;
        this.duration = duration;
        this.comments_sentiment = comments_sentiment;
        this.total_views = total_views;
        this.total_likes = total_likes;
        this.total_dislikes = total_dislikes;
        this.total_comments = total_comments;
        this.total_tweets = total_tweets;
        this.total_original_tweets = total_original_tweets;
        this.total_retweets = total_retweets;
        this.total_channel_views = total_channel_views;
        this.total_channel_comments = total_channel_comments;
        this.total_channel_subscribers = total_channel_subscribers;
        this.total_channel_videos = total_channel_videos;
        this.days = days;
    }


    public VideoRecord getAsVideoRecord(){
        VideoRecord videoRecord = new
                VideoRecord( category,artificial_category,published_at,duration,
                (comments_sentiment.isValid())?comments_sentiment.getNeg().getAverage():0,
                (comments_sentiment.isValid())?comments_sentiment.getNeu().getAverage():0,
                (comments_sentiment.isValid())?comments_sentiment.getPos().getAverage():0,
                (comments_sentiment.isValid())?comments_sentiment.getCompound().getAverage():0);
        for(Day day : days)
            videoRecord.addDay(day.getAsDayRecord());
        return videoRecord;
    }
    @Override
    public JsonObject toJson() {
        JsonObject result = new JsonObject();
        result.addProperty("video_id",video_id);
        result.addProperty("title",title);
        result.addProperty("description", description);
        result.addProperty("category",category);
        result.addProperty("artificial_category",artificial_category);
        result.addProperty("published_at",DateUtil.toDate(published_at));
        result.addProperty("published_at_timestamp",published_at);
        result.addProperty("collected_at", DateUtil.toDate(collected_at));
        result.addProperty("collected_at_timestamp",collected_at);
        result.addProperty("duration",duration);
        result.addProperty("total_views",total_views);
        result.addProperty("total_likes",total_likes);
        result.addProperty("total_dislikes",total_dislikes);
        result.addProperty("total_comments",total_comments);
        result.addProperty("total_tweets",total_tweets);
        result.addProperty("total_original_tweets",total_original_tweets);
        result.addProperty("total_retweets",total_retweets);
        result.addProperty("total_channel_views",total_channel_views);
        result.addProperty("total_channel_comments",total_channel_comments);
        result.addProperty("total_channel_subscribers",total_channel_subscribers);
        result.addProperty("total_channel_videos",total_channel_videos);
        result.add("comments_sentiment",comments_sentiment.toJson());

        JsonArray jsonDays = new JsonArray();
        days.stream().map(Day::toJson).forEach(jsonDays::add);
        result.add("days",jsonDays);
        return result;
    }

    @Override
    public JsonObject toJson(Map<String, Integer> view) {
        return toJson();
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCategory() {
        return category;
    }

    public int getArtificial_category() {
        return artificial_category;
    }

    public long getPublished_at() {
        return published_at;
    }

    public long getCollected_at() {
        return collected_at;
    }

    public long getDuration() {
        return duration;
    }

    public List<Day> getDays() {
        return days;
    }

    public long getTotal_views() {
        return total_views;
    }

    public long getTotal_likes() {
        return total_likes;
    }

    public long getTotal_dislikes() {
        return total_dislikes;
    }

    public long getTotal_comments() {
        return total_comments;
    }

    public long getTotal_tweets() {
        return total_tweets;
    }

    public long getTotal_original_tweets() {
        return total_original_tweets;
    }

    public long getTotal_retweets() {
        return total_retweets;
    }

    public long getTotal_channel_views() {
        return total_channel_views;
    }

    public long getTotal_channel_comments() {
        return total_channel_comments;
    }

    public long getTotal_channel_subscribers() {
        return total_channel_subscribers;
    }

    public long getTotal_channel_videos() {
        return total_channel_videos;
    }

    public SentimentJson getComments_sentiment() {
        return comments_sentiment;
    }
}
