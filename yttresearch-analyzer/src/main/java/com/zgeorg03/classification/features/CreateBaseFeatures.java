package com.zgeorg03.classification.features;


import com.zgeorg03.classification.records.VideoData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Giorgos on 04/01/2017
 * Baseline features for comparison
 * yt_views cumulative
 * tw_orig_tweets cumulative
 */
public class CreateBaseFeatures {

    private Map<String, VideoData> videosMap;
    private int t_window;
    private int offset;
    private int l_window;
    private PrintWriter pw;
    private PrintWriter pw_twitter;
    private boolean recent;
    private float labPer;

    public CreateBaseFeatures(File path, boolean recent, int t, int o, int l){
        this.recent=recent;
        try {
            if(!recent){
                pw = new PrintWriter((new FileWriter(Paths.get(path.getAbsolutePath(),"yt_train_base_"+t+""+o+""+l+".txt").toFile())));
                pw_twitter = new PrintWriter((new FileWriter(Paths.get(path.getAbsolutePath(),"tw_train_base_"+t+""+o+""+l+".txt").toFile())));
            }else{
                pw = new PrintWriter((new FileWriter(Paths.get(path.getAbsolutePath(),"yt_train_base_recent_"+t+""+o+""+l+".txt").toFile())));
                pw_twitter = new PrintWriter((new FileWriter(Paths.get(path.getAbsolutePath(),"tw_train_base_recent_"+t+""+o+""+l+".txt").toFile())));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void create(){
        /**
         * Getting Training data of 90%
         */
        videosMap.values().stream().sorted().forEachOrdered(video -> {
            TrainingBaseFeatures training = new TrainingBaseFeatures(video,t_window);
            pw.println(training.getBaseYoutubeFeatures());
            pw.flush();
            pw_twitter.println(training.getBaseTwitterFeatures());
            pw_twitter.flush();
        });

        pw.close();
        pw_twitter.close();
    }

    public void setLabPer(float labPer) {
        this.labPer = labPer;
    }

    public void setVideosMap(Map<String, VideoData> videosMap) {
        this.videosMap = videosMap;
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
}
