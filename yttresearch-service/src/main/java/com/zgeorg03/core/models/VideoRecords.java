package com.zgeorg03.core.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 2/20/17.
 */
public class VideoRecords extends ConcurrentHashMap<String,VideoRecord>{
    private static final Logger logger = LoggerFactory.getLogger(VideoRecords.class);


    /**
     * Get min days of a video
     * @param artificialCategory
     * @return
     */
    public int getMinDays(int artificialCategory) {
        OptionalInt result = values().stream().filter(x -> (artificialCategory == 0) ? true : x.getArtificial_category() == artificialCategory)
                .mapToInt(x -> x.getDayList().size()).min();
        if (result.isPresent())
            return result.getAsInt();
        else return 0;
    }

    /**
     * Get the N most popular during the label window
     * @param labeling_wnd_start
     */
    public List<VideoRecord> getMostPopularInLabelWindow(int labeling_wnd_start, int limit, int artificialCategory){
        return this.entrySet().stream().filter(x->(artificialCategory==0)?true:x.getValue().getArtificial_category()==artificialCategory).sorted( (x,y) -> {
            int x1 = x.getValue().getDayList().stream().skip(labeling_wnd_start-1).mapToInt(day -> day.getViews()).sum();
            int x2 = y.getValue().getDayList().stream().skip(labeling_wnd_start-1).mapToInt(day -> day.getViews()).sum();
            return (x1 < x2) ? 1 : (x1 > x2) ? -1 : 0;
        }) .map( entry -> entry.getValue()).limit((limit==-1)?Long.MAX_VALUE:limit).collect(Collectors.toList());
    }

    /**
     * Get the N most viral during the label window
     * @param labeling_wnd_start
     */
    public List<VideoRecord> getMostViralInLabelWindow(int labeling_wnd_start, int limit, int artificialCategory){
        return this.entrySet().stream().filter(x->(artificialCategory==0)?true:x.getValue().getArtificial_category()==artificialCategory).sorted( (x,y) -> {
            int x1 = x.getValue().getDayList().stream().skip(labeling_wnd_start-1).mapToInt(day -> day.getTotal_tweets()).sum();
            int x2 = y.getValue().getDayList().stream().skip(labeling_wnd_start-1).mapToInt(day -> day.getTotal_tweets()).sum();
            return (x1 < x2) ? 1 : (x1 > x2) ? -1 : 0;
        }) .map( entry -> entry.getValue()).limit((limit==-1)?Long.MAX_VALUE:limit).collect(Collectors.toList());
    }

    /**
     * Get N random videos
     * @param limit
     * @param rnd
     * @param artificialCategory
     * @return
     */
    public List<VideoRecord> getRandomVideos(int limit,Random rnd,int artificialCategory){
        List<VideoRecord> all;
        if(artificialCategory!=0)
            all = values().stream().filter(x->x.getArtificial_category()==artificialCategory).collect(Collectors.toList());
        else
            all= new ArrayList<>(values());

        return rnd.ints(0,all.size()).mapToObj(x-> all.get(x)).limit(limit).collect(Collectors.toList());
    }

    /**
     * Get N random but recent videos
     * @param limit
     * @param range
     * @param rnd
     * @param artificialCategory
     * @return
     */
    public List<VideoRecord> getRecentVideos(int limit, int range, Random rnd, int artificialCategory){
        List<VideoRecord> all;
        if(artificialCategory!=0)
            all = values().stream().filter(x->x.getArtificial_category()==artificialCategory).collect(Collectors.toList());
        else
            all= new ArrayList<>(values());
        return rnd.ints(0,all.size()).mapToObj(x-> all.get(x)).filter(x-> isRecent(x,range)).limit(limit).collect(Collectors.toList());

    }

    private static final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Check if the a video is in the region of the specified day
     * @param video
     * @param range
     * @return
     */
    public static boolean isRecent(VideoRecord video,int range) {
        try {
            Date videoDate = fmt.parse(video.getDate_uploaded());
            long videoTime = videoDate.getTime();
            long experimentDay = video.getExperiment_timestamp();
            return experimentDay - videoTime <= range * 24 *60 *60 *1000;
        } catch (ParseException e) {
            logger.error("Cannot parse: "+ video.getVideo_id());
            return false;
        }
    }


    public void writeDuration(PrintWriter pw) {
        List<VideoRecord> withDuration = values().stream().filter(video -> video.hasDuration()).collect(Collectors.toList());
        int count = withDuration.size();
        if (count != 0) {

            logger.info("Writing "+ count + " videos");
        }
        pw.print(count+"\n");
        withDuration.forEach(videoRecord -> {
            pw.print(videoRecord.getVideo_id()+"\t"+videoRecord.getDuration()+"\n");
        });
    }
}
