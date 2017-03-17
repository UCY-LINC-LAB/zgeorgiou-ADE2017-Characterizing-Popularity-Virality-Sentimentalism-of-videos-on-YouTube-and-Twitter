package com.zgeorg03.classification.features;


import com.zgeorg03.classification.records.VideoData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

/**
 * Created by Giorgos on 04/01/2017.
 * Creating Classifier features for each video
 * For 1 day training enter window = 0
 */
public class CreateFeatures {

    private final Map<String, VideoData> videosMap;
    private final Set<String> mostPopular;
    private final Set<String> mostViral;

    private int t_window;
    private int offset;
    private int l_window;
    private boolean recent;
    private PrintWriter pw;
    private PrintWriter lab;
    private PrintWriter pw_twitter;
    private float labPer;
    private String ytBinary;
    private String twBinary;

    public CreateFeatures(File path, boolean recent, int t, int o, int l, Map<String, VideoData> videosMap, Set<String> mostPopular, Set<String> mostViral){
        this.recent = recent;
        this.videosMap = videosMap;
        this.mostPopular = mostPopular;
        this.mostViral = mostViral;
        try {
            if(!recent){
                pw = new PrintWriter((new FileWriter(Paths.get(path.getAbsolutePath(),"yt_train_all_"+t+""+o+""+l+".txt").toFile())));
                lab = new PrintWriter((new FileWriter(Paths.get(path.getAbsolutePath(),"labeling_"+t+""+o+""+l+".txt").toFile())));
                pw_twitter = new PrintWriter(new FileWriter(Paths.get(path.getAbsolutePath(),"tw_train_all_"+t+""+o+""+l+".txt").toFile()));
            }else{
                pw = new PrintWriter((new FileWriter(Paths.get(path.getAbsolutePath(),"yt_train_all_recent_"+t+""+o+""+l+".txt").toFile())));
                lab = new PrintWriter((new FileWriter(Paths.get(path.getAbsolutePath(),"labeling_recent_"+t+""+o+""+l+".txt").toFile())));
                pw_twitter = new PrintWriter(new FileWriter(Paths.get(path.getAbsolutePath(),"tw_train_all_recent_"+t+""+o+""+l+".txt").toFile()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLabPer(float labPer) {
        this.labPer = labPer;
    }

    public void create(){
        /**
         * Getting Training data
         */

        videosMap.values().stream().sorted().forEachOrdered(video -> {
            TrainingFeatures training = new TrainingFeatures(video,t_window,ytBinary,twBinary);
            pw.println(training.getAllYoutubeFeatures());
            pw.flush();
            pw_twitter.println(training.getAllTwitterFeatures());
            pw_twitter.flush();
        });


        /**
         * Getting Labeling data
         */
        LabelingFeatures label = new LabelingFeatures(videosMap,mostPopular,mostViral);
        videosMap.values().stream().sorted().forEachOrdered(video -> {
            label.labelKnown(video.getVideo_id());
        });

        lab.println(label.getLabeledPopular());
        lab.println(label.getLabeledViral());
        lab.flush();

        pw.close();
        pw_twitter.close();
        lab.close();
    }

    public void setT_window(int t_window) {
        this.t_window = t_window;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setL_window(int l_window) {
        this.l_window = l_window;
    }

    public void setYtBinary(String ytBinary) {
        this.ytBinary = ytBinary;
    }

    public void setTwBinary(String twBinary) {
        this.twBinary = twBinary;
    }
}
