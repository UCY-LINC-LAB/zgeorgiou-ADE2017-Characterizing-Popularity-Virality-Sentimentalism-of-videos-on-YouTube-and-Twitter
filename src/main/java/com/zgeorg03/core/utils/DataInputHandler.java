package com.zgeorg03.core.utils;
import com.zgeorg03.core.models.Day;
import com.zgeorg03.core.models.VideoRecord;
import com.zgeorg03.core.models.VideoRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by zgeorg03 on 12/4/16.
 */
public class DataInputHandler {

    private static Logger logger = LoggerFactory.getLogger(DataInputHandler.class);

    /**
     * Parse a csv file to load videos. It also checks for duplication
     * @param videoRecords
     * @param fp
     * @param experiment_id
     * @throws IOException
     */
    public static void loadVideosFromFile(final VideoRecords videoRecords, File fp, int experiment_id) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(fp));
        String line ="";
        int duplicates=0;
        int added=0;
        int smallPeriod=0;
        while((line=bf.readLine())!=null){
            if(line.startsWith("#"))
                continue;
            String toks[] = line.split("\t");

            String video_id = toks[0];
            if(videoRecords.containsKey(video_id)) {
                logger.info("Video is already found!");
                duplicates++;
                continue;
            }

            long experiment_timestamp = Long.parseLong(toks[1]);
            String date_uploaded = toks[2];
            int category = Integer.parseInt(toks[3]);
            int artificial_category = getArtificialCategory(category);
            int uploaders_uploads = Integer.parseInt(toks[4]);
            int uploaders_subscribers = Integer.parseInt(toks[5]);
            int tw_user_followers = Integer.parseInt(toks[6]); // TODO Check this
            int days = Integer.parseInt(toks[7]);
            int j=8;

            List<Day> dayList = new ArrayList<>();
            int totalViews =0;
            int totalLikes = 0;
            int totalDislikes = 0;
            int totalComments = 0;
            int totalTweets = 0;
            int totalOriginalTweets = 0;
            int totalQuotes = 0;
            int totalRetweets = 0;
            int totalUserFollowers = 0;
            int totalUserFavorites = 0;
            int totalTweetsInEnglish=0;
            int totalUsersInEnglish=0;
            int totalUserStatuses=0;
            int totalUserFriends=0;
            int totalUsersVerified=0;
            int totalHashtags=0;
            for(int i=0;i<days;i++){
                j++;
                int views = Integer.parseInt(toks[j++]);
                int likes = Integer.parseInt(toks[j++]);
                int dislikes = Integer.parseInt(toks[j++]);
                int comments = Integer.parseInt(toks[j++]);
                int total_tweets = Integer.parseInt(toks[j++]);
                int original_tweets = Integer.parseInt(toks[j++]);
                int quotes = Integer.parseInt(toks[j++]);
                int retweets = Integer.parseInt(toks[j++]);
                int total_user_followers = Integer.parseInt(toks[j++]);
                int total_user_favorites = Integer.parseInt(toks[j++]);
                int tw_en_language = Integer.parseInt(toks[j++]);
                int tw_user_en_language = Integer.parseInt(toks[j++]);
                int tw_user_statuses = Integer.parseInt(toks[j++]);
                int tw_user_friends = Integer.parseInt(toks[j++]);
                int tw_user_verified = Integer.parseInt(toks[j++]);
                int hashtags = Integer.parseInt(toks[j++]);
                Date date = new Date(experiment_timestamp+ ((24*60*60*1000)*i));
                Day day = new Day(date, views, likes, dislikes, comments, total_tweets, original_tweets, quotes, retweets, total_user_followers, total_user_favorites, tw_en_language, tw_user_en_language, tw_user_statuses, tw_user_friends, tw_user_verified, hashtags);
                dayList.add(day);
                totalViews += views;
                totalLikes +=likes;
                totalDislikes += dislikes;
                totalComments += comments;
                totalTweets +=total_tweets;
                totalOriginalTweets +=original_tweets;
                totalQuotes +=quotes;
                totalRetweets +=retweets;
                totalUserFollowers +=total_user_followers;
                totalUserFavorites +=total_user_favorites;
                totalTweetsInEnglish+=tw_en_language;
                totalUsersInEnglish+=tw_user_en_language;
                totalUserStatuses+= tw_user_statuses;
                totalUserFriends+= tw_user_friends;
                totalUsersVerified+= tw_user_verified;
                totalHashtags+= hashtags;
            }

            VideoRecord videoRecord = new VideoRecord(experiment_id, video_id, experiment_timestamp, date_uploaded, category,
                    artificial_category, uploaders_uploads, uploaders_subscribers, totalViews, totalLikes, totalDislikes,
                    totalComments, totalTweets, totalOriginalTweets, totalQuotes, totalRetweets,
                    totalUserFollowers, totalUserFavorites, totalTweetsInEnglish, totalUsersInEnglish,
                    totalUserStatuses, totalUserFriends, totalUsersVerified, totalHashtags, dayList);


            if(dayList.size()<14){
                smallPeriod++;
                continue;
            }
            videoRecords.put(video_id, videoRecord);
            added++;
        }
        logger.info("Experiment: "+experiment_id+":\tDuplicates: " + duplicates +"\t<14 days: " +smallPeriod+"\tAdded: "+added);
        bf.close();
    }


    /**
     * Get artificial category
     * //TODO Ensure that all these are the categories
     * @param category
     * @return
     */
    public static int getArtificialCategory(int category){
        if(category == 10) // Music
            return 1;
        if(category == 20) // Games
            return 2;
        if(category == 22) // People & Blogs
            return 3;
        if(category == 1 || category  == 30 ||
                category == 23 || category == 24 || category >=30) // Comedy & Entertainment & Films
            return 4;
        if(category == 25) // News & Politics
            return 5;

        return 6;//Others
    }

    /**
     * //TODO Spam may needed or not?//
     * @param videoRecord
     * @return
     */
    private static boolean isSpam(VideoRecord videoRecord) {

        long totalEnglish = videoRecord.getTotalTweetsInEnglish();
        long totalEnglishUsers = videoRecord.getTotalUsersInEnglish();
        int users_verified = videoRecord.getTotalUsersVerified();
        long totalViews = videoRecord.getTotalViews();
        long totalTweets = videoRecord.getTotalTweets();
        long totalRetweets = videoRecord.getTotalRetweets();
        if(totalTweets!=0) {
            float ratio = totalRetweets /(float) totalTweets;

            if ( totalEnglish==0 && totalEnglishUsers<100 && users_verified==0 && totalViews < 2000 && ratio < 0.005f) {

                if(totalTweets<500)
                    return false;
                List<Day> days = videoRecord.getDayList();
                for (int i = 0; i < days.size(); i++) {
                    Day day = days.get(i);
                    if (day.getTotal_tweets() == 0)
                        return true;
                }

            }
        }


        return false;
    }

}
