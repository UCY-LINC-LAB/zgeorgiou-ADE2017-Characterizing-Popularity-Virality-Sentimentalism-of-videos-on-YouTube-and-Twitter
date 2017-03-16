package com.zgeorg03.classification.features;


import com.zgeorg03.classification.records.VideoData;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Giorgos on 02/01/2017.
 */
public class LabelingFeatures {

    private Map<String, VideoData> videosMap = new HashMap<>();
    private List<VideoData> mostPopular ;
    private List<VideoData> mostViral ;
    private List<Integer> labeledPopular;
    private List<Integer> labeledViral;
    private List<Integer> labeledPopularAndViral;
    private float labPercentage;

    public LabelingFeatures(Map<String, VideoData> videosMap, float labPer) {
        this.labPercentage = labPer;
        this.videosMap = videosMap;
        this.labeledPopular = new LinkedList<>();
        this.labeledViral = new LinkedList<>();
        this.labeledPopularAndViral = new LinkedList<>();
        int percentage = (int)(videosMap.size()*labPercentage);
        mostPopular = getSortedByTotalViews(false,percentage);
        mostViral = getSortedByTotalTweets(false,percentage);
    }


    public void labelKnown(VideoData video){
        if(mostPopular.contains(video)){
            labeledPopular.add(1);
        }else{
            labeledPopular.add(0);
        }

        if(mostViral.contains(video)){
            labeledViral.add(1);
        }else{
            labeledViral.add(0);
        }
    }

    public List<VideoData> getMostPopular() {
        return mostPopular;
    }

    public List<VideoData> getMostViral() {
        return mostViral;
    }

    public List<Integer> getLabeledPopular() {
        return labeledPopular;
    }

    public List<Integer> getLabeledViral() {
        return labeledViral;
    }

    public List<Integer> getLabeledPopularAndViral() {
        return labeledPopularAndViral;
    }

    public Map<String, VideoData> getVideosMap() {
        return videosMap;
    }

    public void setVideosMap(Map<String, VideoData> videosMap) {
        this.videosMap = videosMap;
    }

    public List<VideoData> getSortedByTotalViews(boolean ascending, int limit){
        return videosMap.entrySet().stream().sorted((e1,e2)->{
            long x1 = e1.getValue().getTotalViews();
            long x2 = e2.getValue().getTotalViews();
            int comp= (x1<x2)?-1 : (x1>x2)? 1 : 0;
            return (ascending) ? comp : comp*-1;
        }) .map( entry -> entry.getValue()).limit((limit==-1)?Long.MAX_VALUE:limit).collect(Collectors.toList());
    }

    public List<VideoData> getSortedByTotalTweets(boolean ascending, int limit){
        return videosMap.entrySet().stream().sorted((e1,e2)->{
            long x1 = e1.getValue().getTotalTweets();
            long x2 = e2.getValue().getTotalTweets();
            int comp= (x1<x2)?-1 : (x1>x2)? 1 : 0;
            return (ascending) ? comp : comp*-1;
        }) .map( entry -> entry.getValue()).limit((limit==-1)?Long.MAX_VALUE:limit).collect(Collectors.toList());
    }
}
