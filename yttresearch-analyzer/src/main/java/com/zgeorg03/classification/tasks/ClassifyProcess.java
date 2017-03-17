package com.zgeorg03.classification.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by zgeorg03 on 3/17/17.
 */
public class ClassifyProcess implements Callable<List<String>> {
    private Logger logger = LoggerFactory.getLogger(ClassifyProcess.class);

    BufferedReader in;
    BufferedReader error;

    private final String scriptPath;
    private final String path;

    private final int train_wnd;
    private final int offset;
    private final int lbl_wnd;
    private final String youtube_features_binary;
    private final String twitter_features_binary;

    public ClassifyProcess(String scriptPath, String path, int train_wnd, int offset, int lbl_wnd, String youtube_features_binary, String twitter_features_binary) {
        this.scriptPath = scriptPath;
        this.path = path;
        this.train_wnd = train_wnd;
        this.offset = offset;
        this.lbl_wnd = lbl_wnd;
        this.youtube_features_binary = youtube_features_binary;
        this.twitter_features_binary = twitter_features_binary;
    }

    private String getArguments(){
        return " " +path
                + " " + train_wnd
                + " " + offset
                + " " + lbl_wnd
                + " " + youtube_features_binary
                + " " + twitter_features_binary
                ;
    }

    @Override
    public List<String> call() throws Exception {
        System.out.println("python "+scriptPath+getArguments());
        ProcessBuilder builder = new ProcessBuilder("bash","-c","python "+scriptPath+getArguments());
        try {
            Process process = builder.start();
            in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            process.waitFor();
            List<String> inputLines = readLines(in);
            List<String> errorLines = readLines(error);
            if (errorLines.size() != 0) {
                errorLines.add(0, "Error");
                return errorLines;
            }
            return inputLines;
        }catch (Exception ex){
            logger.error(ex.getLocalizedMessage());
            return Arrays.asList("Error",ex.getLocalizedMessage());
        }


    }

    private List<String> readLines(BufferedReader reader) throws IOException {
        List<String> lines = new LinkedList<>();
        String input = "";
        while ((input = reader.readLine()) != null)
            lines.add(input);

        return lines;
    }
}
