package com.zgeorg03.rawvideos;

import com.zgeorg03.analysis.SentimentAnalysis;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.rawvideos.models.RawVideo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by zgeorg03 on 3/4/17.
 */
public class FinishedVideosMonitor implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(FinishedVideosMonitor.class);
    private final DBServices dbServices;
    private int maxVideos;

    private final SentimentAnalysis sentimentAnalysis;
    private final ExecutorService executorService;

    public FinishedVideosMonitor(DBServices dbServices, int maxVideos, ExecutorService executors, SentimentAnalysis sentimentAnalysis) {
        this.dbServices = dbServices;
        this.maxVideos = maxVideos;
        this.sentimentAnalysis = sentimentAnalysis;
        this.executorService = executors;
    }


    @Override
    public void run() {
        logger.info("Finished Videos Monitor started...");
        while(true) {
            List<String> notProcessed = dbServices.getFinishedButNotProcessedVideos(maxVideos);

            if (notProcessed.isEmpty()) {
                logger.info("All videos are processed. Going to sleep...");
                try { TimeUnit.HOURS.sleep(3); } catch (InterruptedException e) { logger.error("Interrupted");}
            }

            List<Future<Integer>> results = new LinkedList<Future<Integer>>();
            for(String video:notProcessed){
                ProcessVideoTask task = new ProcessVideoTask(dbServices,video,sentimentAnalysis);
                results.add(executorService.submit(task));
            }
            int count =0;
            for(Future<Integer> future:results){
                try {
                    int i = future.get();
                    count++;
                    if(count%100==0)
                    logger.info("Finished Videos batch:"+count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            try { TimeUnit.SECONDS.sleep(60); } catch (InterruptedException e) { logger.error("Interrupted");}
        }
    }
}
