package com.zgeorg03.core.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zgeorg03.core.utils.JsonModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 12/2/16.
 */
public class VideoRecord implements JsonModel {


    private final int experiment_id;
    private final String video_id;
    private final long experiment_timestamp;
    private final String date_uploaded;
    private final int category;
    private final int artificial_category;
    private final int uploaders_uploads;
    private final int uploaders_subscribers;
    private final int totalViews;
    private final int totalLikes;
    private final int totalDislikes;
    private final int totalComments;
    private final int totalTweets ;
    private final int totalOriginalTweets;
    private final int totalQuotes;
    private final int totalRetweets;
    private final int totalUserFollowers;
    private final int totalUserFavorites;
    private final int totalTweetsInEnglish;
    private final int totalUsersInEnglish;
    private final int totalUserStatuses;
    private final int totalUserFriends;
    private final int totalUsersVerified;
    private final int toalHashtags;
    private final List<Day> dayList ;
    private final String experimentDate;

    //TODO Duration & Comments
    private long duration=-1; // Duration in millis


    public VideoRecord(int experiment_id, String video_id, long experiment_timestamp, String date_uploaded, int category, int artificial_category, int uploaders_uploads, int uploaders_subscribers, int totalViews, int totalLikes, int totalDislikes, int totalComments, int totalTweets, int totalOriginalTweets, int totalQuotes, int totalRetweets, int totalUserFollowers, int totalUserFavorites, int totalTweetsInEnglish, int totalUsersInEnglish, int totalUserStatuses, int totalUserFriends, int totalUsersVerified, int toalHashtags, List<Day> dayList) {
        this.experiment_id = experiment_id;
        this.video_id = video_id;
        this.experiment_timestamp = experiment_timestamp;
        this.date_uploaded = date_uploaded;
        this.category = category;
        this.artificial_category = artificial_category;
        this.uploaders_uploads = uploaders_uploads;
        this.uploaders_subscribers = uploaders_subscribers;
        this.totalViews = totalViews;
        this.totalLikes = totalLikes;
        this.totalDislikes = totalDislikes;
        this.totalComments = totalComments;
        this.totalTweets = totalTweets;
        this.totalOriginalTweets = totalOriginalTweets;
        this.totalQuotes = totalQuotes;
        this.totalRetweets = totalRetweets;
        this.totalUserFollowers = totalUserFollowers;
        this.totalUserFavorites = totalUserFavorites;
        this.totalTweetsInEnglish = totalTweetsInEnglish;
        this.totalUsersInEnglish = totalUsersInEnglish;
        this.totalUserStatuses = totalUserStatuses;
        this.totalUserFriends = totalUserFriends;
        this.totalUsersVerified = totalUsersVerified;
        this.toalHashtags = toalHashtags;
        this.dayList = dayList;
        experimentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(experiment_timestamp));
    }

    public int getTotalTweetsInEnglish() {
        return totalTweetsInEnglish;
    }

    public int getTotalUsersInEnglish() {
        return totalUsersInEnglish;
    }

    public int getExperiment_id() {
        return experiment_id;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getDate_uploaded() {
        return date_uploaded;
    }

    public int getCategory() {
        return category;
    }

    public int getUploaders_uploads() {
        return uploaders_uploads;
    }

    public int getUploaders_subscribers() {
        return uploaders_subscribers;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public int getTotalDislikes() {
        return totalDislikes;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public int getTotalTweets() {
        return totalTweets;
    }

    public int getTotalOriginalTweets() {
        return totalOriginalTweets;
    }

    public int getTotalQuotes() {
        return totalQuotes;
    }

    public int getTotalRetweets() {
        return totalRetweets;
    }

    public List<Day> getDayList() {
        return dayList;
    }

    @Override
    public String toString() {
        return "VideoRecord{" +
                "experiment_id=" + experiment_id +
                ", video_id='" + video_id + '\'' +
                ", experiment_timestamp=" + experiment_timestamp +
                ", date_uploaded='" + date_uploaded + '\'' +
                ", category=" + category +
                ", uploaders_uploads=" + uploaders_uploads +
                ", uploaders_subscribers=" + uploaders_subscribers +
                ", totalViews=" + totalViews +
                ", totalLikes=" + totalLikes +
                ", totalDislikes=" + totalDislikes +
                ", totalComments=" + totalComments +
                ", totalTweets=" + totalTweets +
                ", totalOriginalTweets=" + totalOriginalTweets +
                ", totalQuotes=" + totalQuotes +
                ", totalRetweets=" + totalRetweets +
                ", totalUserFollowers=" + totalUserFollowers +
                ", dayList=" + dayList +
                ", experimentDate='" + experimentDate + '\'' +
                '}';
    }

    private String dayListToString(){
       return dayList.stream().map(Day::toString).collect(Collectors.joining(",","[","]"));
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        JsonArray dayArray = new JsonArray();
        dayList.forEach(x -> dayArray.add(x.toJson()));
        object.addProperty("video_id",video_id);
        object.addProperty("experiment_id",experiment_id);
        object.addProperty("experiment_date",experimentDate);
        object.addProperty("date_uploaded",date_uploaded);
        object.addProperty("category",category);
        object.addProperty("uploaders_uploads",uploaders_uploads);
        object.addProperty("uploaders_subscribers",uploaders_subscribers);
        object.addProperty("totalViews",totalViews);
        object.addProperty("totalLikes",totalLikes);
        object.addProperty("totalDislikes",totalDislikes);
        object.addProperty("totalComments",totalComments);
        object.addProperty("totalTweets",totalTweets);
        object.addProperty("totalOriginalTweets",totalOriginalTweets);
        object.addProperty("totalQuotes", totalQuotes);
        object.addProperty("totalRetweets",totalRetweets);


        object.addProperty("totalUserFollowers",totalUserFollowers);
        object.addProperty("totalUserFavorites",totalUserFavorites);
        object.addProperty("totalTweetsInEnglish",totalTweetsInEnglish);
        object.addProperty("totalUsersInEnglish",totalUsersInEnglish);
        object.addProperty("totalUserStatuses",totalUserStatuses);
        object.addProperty("totalUserFriends",totalUserFriends);
        object.addProperty("totalUsersVerified",totalUsersVerified);
        object.addProperty("totalHashtags",toalHashtags);
        object.addProperty("duration",duration);
        object.add("dayList",dayArray);
        return object;
    }

    @Override
    public JsonElement toJson(Map<String, Integer> map) {
        JsonObject object = new JsonObject();

        int img = map.getOrDefault("img",-1);
        if(img!=-1)
            object.addProperty("img","http://img.youtube.com/vi/"+video_id+"/0.jpg");

        if(map.getOrDefault("video_id",1)==1)
            object.addProperty("video_id",video_id);

        object.addProperty("url","/videos/"+video_id);

        if(map.getOrDefault("experiment_id",0)==1)
            object.addProperty("experiment_id",experiment_id);

        if(map.getOrDefault("experiment_date",0)==1)
            object.addProperty("experiment_date",experimentDate);

        if(map.getOrDefault("date_uploaded",0)==1)
            object.addProperty("date_uploaded",date_uploaded);

        if(map.getOrDefault("category",0)==1)
            object.addProperty("category",category);

        if(map.getOrDefault("uploaders_uploads",0)==1)
            object.addProperty("uploaders_uploads",uploaders_uploads);

        if(map.getOrDefault("uploaders_subscribers",0)==1)
            object.addProperty("uploaders_subscribers",uploaders_subscribers);

        int lblWnd = map.getOrDefault("totalViewsInLabelWnd",-1);
        if(lblWnd!=-1)
            object.addProperty("totalViewsInLabelWnd",getTotalViewsInLblWnd(lblWnd));

        if(map.getOrDefault("totalViews",0)==1)
            object.addProperty("totalViews",totalViews);

        if(map.getOrDefault("totalLikes",0)==1)
            object.addProperty("totalLikes",totalLikes);

        if(map.getOrDefault("totalDislikes",0)==1)
            object.addProperty("totalDislikes",totalDislikes);

        if(map.getOrDefault("totalComments",0)==1)
            object.addProperty("totalComments",totalComments);

        lblWnd = map.getOrDefault("totalTweetsInLabelWnd",-1);
        if(lblWnd!=-1)
            object.addProperty("totalTweetsInLabelWnd",getTotalTweetsInLblWnd(lblWnd));

        if(map.getOrDefault("totalTweets",0)==1)
            object.addProperty("totalTweets",totalTweets);

        if(map.getOrDefault("totalOriginalTweets",0)==1)
            object.addProperty("totalOriginalTweets",totalOriginalTweets);

        if(map.getOrDefault("totalQuotes",0)==1)
            object.addProperty("totalQuotes", totalQuotes);

        if(map.getOrDefault("totalRetweets",0)==1)
            object.addProperty("totalRetweets",totalRetweets);


        if(map.getOrDefault("totalUserFollowers",0)==1)
            object.addProperty("totalUserFollowers",totalUserFollowers);

        if(map.getOrDefault("totalUserFavorites",0)==1)
            object.addProperty("totalUserFavorites",totalUserFavorites);

        if(map.getOrDefault("totalTweetsInEnglish",0)==1)
            object.addProperty("totalTweetsInEnglish",totalTweetsInEnglish);

        if(map.getOrDefault("totalUsersInEnglish",0)==1)
            object.addProperty("totalUsersInEnglish",totalUsersInEnglish);

        if(map.getOrDefault("totalUserStatuses",0)==1)
            object.addProperty("totalUserStatuses",totalUserStatuses);

        if(map.getOrDefault("totalUserFriends",0)==1)
            object.addProperty("totalUserFriends",totalUserFriends);

        if(map.getOrDefault("totalUsersVerified",0)==1)
            object.addProperty("totalUsersVerified",totalUsersVerified);

        if(map.getOrDefault("totalHashtags",0)==1)
            object.addProperty("totalHashtags",toalHashtags);

        if(map.getOrDefault("duration",0)==1)
            object.addProperty("duration",duration);

        if(map.getOrDefault("dayList",0)==1) {
            JsonArray dayArray = new JsonArray();
            dayList.forEach(x -> dayArray.add(x.toJson()));
            object.add("dayList", dayArray);
        }
        return object;
    }

    private long getTotalViewsInLblWnd(int lblWnd) {
        return dayList.stream().skip(lblWnd-1).mapToInt(day -> day.getViews()).sum();
    }

    private long getTotalTweetsInLblWnd(int lblWnd) {
        return dayList.stream().skip(lblWnd-1).mapToInt(day -> day.getTotal_tweets()).sum();
    }

    public long getExperiment_timestamp() {
        return experiment_timestamp;
    }

    public int getTotalUserFollowers() {
        return totalUserFollowers;
    }

    public int getTotalUserFavorites() {
        return totalUserFavorites;
    }

    public int getTotalUsersVerified() {
        return totalUsersVerified;
    }

    public int getToalHashtags() {
        return toalHashtags;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  VideoRecord)
            return this.getVideo_id().equals(((VideoRecord)obj).getVideo_id());
        return false;
    }

    public int getArtificial_category() {
        return artificial_category;
    }

    public boolean hasDuration() {
        return duration!=-1;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
