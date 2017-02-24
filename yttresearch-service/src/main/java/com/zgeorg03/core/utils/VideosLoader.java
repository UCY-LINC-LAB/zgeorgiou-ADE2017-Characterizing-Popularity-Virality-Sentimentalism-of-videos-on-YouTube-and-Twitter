package com.zgeorg03.core.utils;

import com.zgeorg03.core.models.VideoRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by zgeorg03 on 2/20/17.
 * The purpose of this class is to load videos without the need of restart.
 * It constantly monitors the directory
 */
public class VideosLoader  implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(VideosLoader.class);
    private final File root;
    private final int sleepTime;
    private final VideoRecords videoRecords;

    private final Map<Integer,Boolean> experiments = new TreeMap<>();

    private final VideoDurationCollector videoDurationCollector;

    public VideosLoader(String path,int durationBatchSize, int sleepTime) throws Exception {
        this.root = Paths.get(path).toFile();
        this.sleepTime = sleepTime;
        if(!this.root.exists() || !this.root.canRead())
            throw new Exception(this.root.getAbsolutePath()+" may not exist or no permissions given");
        if(!this.root.isDirectory())
            throw new Exception(this.root.getAbsolutePath()+" is not a directory");
        this.videoRecords = new VideoRecords();

        videoDurationCollector = new VideoDurationCollector(videoRecords,path, durationBatchSize);




    }

    @Override
    public void run() {
        logger.info("VideosLoader started...");
        while (true){

            update();
            videoDurationCollector.getDurations();


            try {
                TimeUnit.SECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                logger.info("VideosLoader interrupted");;
            }

        }
    }

    /**
     * This helper method check
     */
    private void update() {
       for(File fp : root.listFiles((dir, name) -> name.endsWith(".csv"))){
           int id=0;
           try {
               id = Integer.parseInt(fp.getName().split("-")[0]);
               if(!experiments.containsKey(id)){
                   DataInputHandler.loadVideosFromFile(videoRecords,fp,id);
                   experiments.put(id,true);
               }
           }catch (NumberFormatException ex){
               logger.info("Invalid format: "+ fp.getName());
               continue;
           } catch (IOException e) {
               logger.info("Invalid format inside the file: "+ fp.getName());
               continue;
           }

       }
    }

    public VideoRecords getVideoRecords() {
        return videoRecords;
    }
}
