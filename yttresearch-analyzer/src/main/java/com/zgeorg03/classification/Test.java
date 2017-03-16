package com.zgeorg03.classification;


import com.zgeorg03.analysis.models.Video;
import com.zgeorg03.classification.records.VideoData;
import com.zgeorg03.classification.records.VideoRecord;

import java.util.function.Predicate;

/**
 * Created by zgeorg03 on 3/15/17.
 */
public class Test {


    public Test() {
        int t_window=1;
        int offset=0;
        int l_window=0;
        int split_days=14;
        float labPer=0.025f;
        String ytFeatures = "1111111111111111111111111111111";
        String twFeatures = "1111111111111111111111111111111111111111111";
        FeatureManager featureManager = new FeatureManager(t_window, offset, l_window, split_days, labPer, ytFeatures, twFeatures);


        Video video = null;
        VideoRecord record = video.getAsVideoRecord();
        VideoData videoData = new VideoData(video,featureManager.mapYouTubeFeatures(record,15),
                featureManager.mapTwitterFeatures(record,15));
        long oneDay = 86400000;

        Predicate<VideoData> splitPredicate = (v) -> ((v.getCollected_at()-v.getYoutubeFeatures().getYt_uploaded())/oneDay < split_days);



        //featureManager.populate(video,featureManager.mapTwitterFeatures(record,15),);

        /**
         * Zacharias Example
         * 1 featureManager = new FeatureManager(t_window,offset,l_window,split_days,labPer,ytFeatures,twFeatures);
         * 2 Create VideoMaps instead of CSV
         * 3 featureManager.createFeatures();
         *
         * 2 -> pseudokwdikas:
         * This is where the mapping happens:
         *
         * For all our videos do :
         *      VideoRecord vr = new VideoRecord(..);
         *      for all the days of the video do:
         *          DayRecord dr = new DayRecord();
         *          dr.setViews(..)
         *          dr.set(...)
         *          ...
         *          vr.addDay(dr)
         *     VideoData video = new VideoData(published,collected,videoId,
         *                  featureManager.mapYouTubeFeatures(vr,15),featureManager.mapTwitterFeatures(vr,15));
         *
         *     Add to maps and arraylists (recent or not)
         *
         * featureManager.populate(videosMap,videosMapRecent,uniqueVideos,uniqueVideosRecent);
         * featureManager.createFeatures();
         */
    }

}
