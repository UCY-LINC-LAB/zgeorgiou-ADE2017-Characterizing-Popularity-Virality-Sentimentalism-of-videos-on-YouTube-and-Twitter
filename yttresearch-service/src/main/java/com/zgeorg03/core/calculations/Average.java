package com.zgeorg03.core.calculations;

import com.zgeorg03.core.models.Day;
import com.zgeorg03.core.models.VideoRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 2/21/17.
 */
public class Average {

    /**
     *
     * @param videoRecords
     * @param lbl_wnd
     * @return Map for each day
     */
    public static Map<Integer,Double> ViewsPerDay(List<VideoRecord> videoRecords, int lbl_wnd){
        if(videoRecords.size()==0)
            return new HashMap<>();

        List<List<Day>> list = videoRecords.stream().map(VideoRecord::getDayList).collect(Collectors.toList());
        int min = list.stream().mapToInt(x -> x.size()).min().getAsInt();
        int records = list.size();
        long accumulator[] = new long[min];
        for(List<Day> days : list){
            for(int i=lbl_wnd-1;i<min;i++){
                try{
                    final Day day = days.get(i);
                    accumulator[i]+=day.getViews();
                }catch (IndexOutOfBoundsException ex){ }
            }
        }
        Map<Integer,Double>res = new HashMap<>();
        if(records==0)
            return res;
        for(int i=lbl_wnd-1;i<min;i++)
            res.put(i, accumulator[i]/(double)records);
        return res;
    }

    /**
     *
     * @param videoRecords
     * @return Average number of total views
     */
    public static double views(List<VideoRecord> videoRecords){
        if(videoRecords.size()==0)
            return  0;

        return videoRecords.stream().mapToLong(VideoRecord::getTotalViews).average().getAsDouble();
    }
    /**
     *
     * @param videoRecords
     * @return Average number of total tweets
     */
    public static double tweets(List<VideoRecord> videoRecords){
        if(videoRecords.size()==0)
            return  0;

        return videoRecords.stream().mapToLong(VideoRecord::getTotalTweets).average().getAsDouble();
    }
    /**
     *
     * Average tweets per day
     * @param videoRecords
     * @param lbl_wnd
     * @return Map for each day
     */
    public static Map<Integer,Double> tweetsPerDay(List<VideoRecord> videoRecords, int lbl_wnd){
        if(videoRecords.size()==0)
            return new HashMap<>();

        List<List<Day>> list = videoRecords.stream().map(VideoRecord::getDayList).collect(Collectors.toList());
        int min = list.stream().mapToInt(x -> x.size()).min().getAsInt();
        int records = list.size();
        long accumulator[] = new long[min];
        for(List<Day> days : list){
            for(int i=lbl_wnd-1;i<min;i++){
                try{
                    final Day day = days.get(i);
                    accumulator[i]+=day.getTotal_tweets();
                }catch (IndexOutOfBoundsException ex){ }
            }
        }
        Map<Integer,Double>res = new HashMap<>();
        if(records==0)
            return res;
        for(int i=lbl_wnd-1;i<min;i++)
            res.put(i, accumulator[i]/(double)records);
        return res;
    }
}
