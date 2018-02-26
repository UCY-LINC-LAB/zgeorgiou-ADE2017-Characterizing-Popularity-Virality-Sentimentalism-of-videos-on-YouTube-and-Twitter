package com.zgeorg03.rawvideos;

import com.zgeorg03.analysis.SentimentAnalysis;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.rawvideos.models.RawVideo;

import java.util.concurrent.Callable;

public class ProcessVideoTask implements Callable<Integer>{
    private final DBServices dbServices;
    private final String videoId;
    private final SentimentAnalysis sentimentAnalysis;

    public ProcessVideoTask(DBServices dbServices, String videoId, SentimentAnalysis sentimentAnalysis) {
        this.dbServices = dbServices;
        this.videoId = videoId;
        this.sentimentAnalysis = sentimentAnalysis;
    }


    @Override
    public Integer call() throws Exception {
        ProcessVideo processVideo = new ProcessVideo(dbServices, videoId, sentimentAnalysis);
        RawVideo rawVideo = processVideo.getVideo();
        dbServices.getProcessVideoDBService().addOrReplaceProcessedVideo(rawVideo);
        dbServices.getProcessVideoDBService().setVideoAsProcessed(videoId);
        return 1;
    }
}
