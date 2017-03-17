package com.zgeorg03.classification.features;


import com.zgeorg03.analysis.models.Video;
import com.zgeorg03.classification.records.VideoData;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Giorgos on 02/01/2017.
 */
public class LabelingFeatures {

    private Map<String, VideoData> videosMap = new HashMap<>();
    private Set<String> mostPopular ;
    private Set<String> mostViral ;
    private List<Integer> labeledPopular;
    private List<Integer> labeledViral;

    public LabelingFeatures(Map<String, VideoData> videosMap,Set<String>mostPopular,Set<String>mostViral){
        this.videosMap = videosMap;
        this.labeledPopular = new LinkedList<>();
        this.labeledViral = new LinkedList<>();
        this.mostPopular = mostPopular;
        this.mostViral = mostViral;
    }


    public void labelKnown(String video){
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

    public List<Integer> getLabeledPopular() {
        return labeledPopular;
    }

    public List<Integer> getLabeledViral() {
        return labeledViral;
    }

}

