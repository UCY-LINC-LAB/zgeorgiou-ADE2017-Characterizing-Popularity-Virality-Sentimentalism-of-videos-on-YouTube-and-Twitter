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
public class Median {

    /**
     *
     * @param videoRecords
     * @param lbl_wnd
     * @return Map for each day
     */
    public static Map<Integer,Integer> viewsPerDay(List<VideoRecord> videoRecords, int lbl_wnd){
        if(videoRecords.size()==0)
            return new HashMap<>();

        Map<Integer,Integer> res = new HashMap<>();
        int min = videoRecords.stream().mapToInt(x -> x.getDayList().size()).min().getAsInt();

        for(int i=lbl_wnd-1;i<min;i++) {
            int finalI = i;
            List<Integer> list = videoRecords.stream().mapToInt(x -> x.getDayList().get(finalI).getViews()).sorted().boxed().collect(Collectors.toList());
            int median = list.size()/2;
            res.put(i,list.get(median));
        }

        return res;
    }

    /**
     *
     * @param videoRecords
     * @param lbl_wnd
     * @return Map for each day
     */
    public static Map<Integer,Integer> tweetsPerDay(List<VideoRecord> videoRecords, int lbl_wnd){
        if(videoRecords.size()==0)
            return new HashMap<>();

        Map<Integer,Integer> res = new HashMap<>();
        int min = videoRecords.stream().mapToInt(x -> x.getDayList().size()).min().getAsInt();

        for(int i=lbl_wnd-1;i<min;i++) {
            int finalI = i;
            List<Integer> list = videoRecords.stream().mapToInt(x -> x.getDayList().get(finalI).getTotal_tweets()).sorted().boxed().collect(Collectors.toList());
            int median = list.size()/2;
            res.put(i,list.get(median));
        }

        return res;
    }

}
