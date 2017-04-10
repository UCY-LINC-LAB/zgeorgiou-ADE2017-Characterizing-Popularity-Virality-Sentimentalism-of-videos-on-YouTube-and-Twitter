package com.zgeorg03.core;

import java.io.*;

/**
 * Created by zgeorg03 on 4/10/17.
 */
public class App {

    public static void main (String args[]) throws IOException {

        LoadFeatures features = new LoadFeatures("/home/zgeorg03/Thesis/Results/130K/exp_e_2-5_2_0_7/classification_data/");

        PrintWriter pw = new PrintWriter(new FileWriter(new File("features.csv")));
        System.out.println(features);
        pw.print(features);
        pw.close();


    }


}
