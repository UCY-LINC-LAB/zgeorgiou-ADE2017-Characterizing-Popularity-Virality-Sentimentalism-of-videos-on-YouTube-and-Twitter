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
public class StandardDeviation {

    /**
     *
     * @param videoRecords
     * @param lbl_wnd
     * @return
     */
    public static Map<Integer,Double> viewsPerDay(List<VideoRecord> videoRecords,int lbl_wnd,Map<Integer,Double> avg){
        if(videoRecords.size()==0)
            return new HashMap<>();

        List<List<Day>> list = videoRecords.stream().map(VideoRecord::getDayList).collect(Collectors.toList());
        int min = list.stream().mapToInt(x -> x.size()).min().getAsInt();
        int records = list.size();
        double accumulator[] = new double[min];
        for(List<Day> days : list){
            for(int i=lbl_wnd-1;i<min;i++){
                try{
                    final Day day = days.get(i);
                    int views = day.getViews();
                    double avgViews = avg.get(i);
                    accumulator[i]+= (views-avgViews) * (views-avgViews);
                }catch (IndexOutOfBoundsException ex){ }
            }
        }
        Map<Integer,Double>res = new HashMap<>();
        if(records==0)
            return res;
        for(int i=lbl_wnd-1;i<min;i++)
            res.put(i, Math.sqrt(accumulator[i]/records));
        return res;
    }

    /**
     *
     * @param videoRecords
     * @param lbl_wnd
     * @return
     */
    public static Map<Integer,Double> tweetsPerDay(List<VideoRecord> videoRecords,int lbl_wnd,Map<Integer,Double> avg){
        if(videoRecords.size()==0)
            return new HashMap<>();

        List<List<Day>> list = videoRecords.stream().map(VideoRecord::getDayList).collect(Collectors.toList());
        int min = list.stream().mapToInt(x -> x.size()).min().getAsInt();
        int records = list.size();
        double accumulator[] = new double[min];
        for(List<Day> days : list){
            for(int i=lbl_wnd-1;i<min;i++){
                try{
                    final Day day = days.get(i);
                    int tweets = day.getTotal_tweets();
                    double avgTweets = avg.get(i);
                    accumulator[i]+= (tweets-avgTweets) * (tweets-avgTweets);
                }catch (IndexOutOfBoundsException ex){ }
            }
        }
        Map<Integer,Double>res = new HashMap<>();
        if(records==0)
            return res;
        for(int i=lbl_wnd-1;i<min;i++)
            res.put(i, Math.sqrt(accumulator[i]/records));
        return res;
    }
}
