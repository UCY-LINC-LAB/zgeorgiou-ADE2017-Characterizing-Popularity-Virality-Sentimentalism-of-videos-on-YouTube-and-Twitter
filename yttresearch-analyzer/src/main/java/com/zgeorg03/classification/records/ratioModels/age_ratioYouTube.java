package com.zgeorg03.classification.records.ratioModels;


import com.zgeorg03.classification.records.YoutubeFeatures;

import java.util.Date;

/**
 * Created by Giorgos on 02/01/2017.
 * This object is used to relate a feature value to its age.
 * We count the age in days
 */
public class age_ratioYouTube {
    private int day;
    private YoutubeFeatures youtubeFeatures;
    private float ageRatioViews;
    private float ageRatioLikes;
    private float ageRatioDislikes;
    private float ageRatioComments;
    private String ytBinary;

    public age_ratioYouTube(int day, YoutubeFeatures youtubeFeatures) {
        this.day = day;
        this.youtubeFeatures = youtubeFeatures;
        this.ageRatioComments=0;
        this.ageRatioViews=0;
        this.ageRatioLikes=0;
        this.ageRatioDislikes=0;
        calculateAgeRatioViews();
        calculateAgeRatioLikes();
        calculateAgeRatioDislikes();
        calculateAgeRatioComments();
    }

    public void calculateAgeRatioViews(){
        long millsPassed = System.currentTimeMillis() - youtubeFeatures.getYt_uploaded();
        int daysPassed = (int)(millsPassed/(1000*60*60*24));
        long views_n = youtubeFeatures.getFeaturesForDay(day).getYt_views();

        if(daysPassed!=0){
            ageRatioViews= views_n/(float)daysPassed;
        }
    }

    public void calculateAgeRatioLikes(){
        long millsPassed = System.currentTimeMillis() - youtubeFeatures.getYt_uploaded();
        int daysPassed = (int)(millsPassed/(1000*60*60*24));
        long likes_n = youtubeFeatures.getFeaturesForDay(day).getYt_likes();

        if(daysPassed!=0){
            ageRatioLikes = likes_n/(float)daysPassed;
        }
    }

    public void calculateAgeRatioDislikes(){
        long millsPassed = System.currentTimeMillis() - youtubeFeatures.getYt_uploaded();
        int daysPassed = (int)(millsPassed/(1000*60*60*24));
        long dislikes_n = youtubeFeatures.getFeaturesForDay(day).getYt_dislikes();

        if(daysPassed!=0){
            ageRatioDislikes = dislikes_n/(float)daysPassed;
        }
    }

    public void calculateAgeRatioComments(){
        long millsPassed = System.currentTimeMillis() - youtubeFeatures.getYt_uploaded();
        int daysPassed = (int)(millsPassed/(1000*60*60*24));
        long comments_n = youtubeFeatures.getFeaturesForDay(day).getYt_comments();

        if(daysPassed!=0){
            ageRatioComments= comments_n/(float)daysPassed;
        }
    }

    public float getAgeRatioViews(){
        return ageRatioViews;
    }

    public float getAgeRatioLikes(){
        return ageRatioLikes;
    }

    public float getAgeRatioDislikes(){
        return ageRatioDislikes;
    }

    public float getAgeRatioComments(){
        return ageRatioComments;
    }

    public void setYtBinary(String ytBinary) {
        this.ytBinary = ytBinary;
    }

    @Override
    public String toString() {
        String ageFeatures = "";

        if(ytBinary.charAt(23)=='1'){ageFeatures = ageFeatures.concat(ageRatioViews + "," );}
        if(ytBinary.charAt(24)=='1'){ageFeatures = ageFeatures.concat(ageRatioLikes + "," );}
        if(ytBinary.charAt(25)=='1'){ageFeatures = ageFeatures.concat(ageRatioDislikes + "," );}
        if(ytBinary.charAt(26)=='1'){ageFeatures = ageFeatures.concat(ageRatioComments + "," ); }

        return ageFeatures;

    }
}
