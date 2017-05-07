package com.zgeorg03.core;

import java.io.*;

/**
 * Created by zgeorg03 on 4/10/17.
 */
public class App {

    public static void main (String args[]) throws IOException {

        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_All_2-5_2_0_7/classification_data/",
                "twitter").writeAll("all");
        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_All_2-5_2_0_7/classification_data/",
                "youtube").writeAll("all");
        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_All_2-5_2_0_7/classification_data/",
                "both").writeAll("all");

        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_Music_2-5_2_0_7/classification_data/",
                "twitter").writeAll("music");
        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_Music_2-5_2_0_7/classification_data/",
                "youtube").writeAll("music");
        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_Music_2-5_2_0_7/classification_data/",
                "both").writeAll("music");

        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_Games_2-5_2_0_7/classification_data/",
                "twitter").writeAll("games");
        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_Games_2-5_2_0_7/classification_data/",
                "youtube").writeAll("games");
        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_Games_2-5_2_0_7/classification_data/",
                "both").writeAll("games");

        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_Entertainment_2-5_2_0_7/classification_data/",
                "twitter").writeAll("entertainment");
        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_Entertainment_2-5_2_0_7/classification_data/",
                "youtube").writeAll("entertainment");
        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_Entertainment_2-5_2_0_7/classification_data/",
                "both").writeAll("entertainment");

        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_News_Politics_2-5_2_0_7/classification_data/",
                "twitter").writeAll("news_politics");
        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_News_Politics_2-5_2_0_7/classification_data/",
                "youtube").writeAll("news_politics");
        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_News_Politics_2-5_2_0_7/classification_data/",
                "both").writeAll("news_politics");


        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_People_Blogs_2-5_2_0_7/classification_data/",
                "twitter").writeAll("people_blogs");
        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_People_Blogs_2-5_2_0_7/classification_data/",
                "youtube").writeAll("people_blogs");
        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_People_Blogs_2-5_2_0_7/classification_data/",
                "both").writeAll("people_blogs");

        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_Others_2-5_2_0_7/classification_data/",
                "twitter").writeAll("others");
        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_Others_2-5_2_0_7/classification_data/",
                "youtube").writeAll("others");
        new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_Others_2-5_2_0_7/classification_data/",
                "both").writeAll("others");

    }


}
