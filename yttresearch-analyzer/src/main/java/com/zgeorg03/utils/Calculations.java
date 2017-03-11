package com.zgeorg03.utils;

import com.zgeorg03.analysis.models.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 3/4/17.
 */
public class Calculations {

    /**
     * Return average of an Integer list
     * @param list
     * @return
     */
    public static double averageInt(List<Integer> list){
        if(list.size()==0)
            return 0;
        return list.stream().mapToInt(x->x).average().getAsDouble();
    }

    /**
     * Return average of a Long list
     * @param list
     * @return
     */
    public static double averageLong(List<Long> list){
        if(list.size()==0)
            return 0;
        return list.stream().mapToLong(x->x).average().getAsDouble();
    }

    /**
     * Return average of a double list
     * @param list
     * @return
     */
    public static double averageDouble(List<Double> list){
        if(list.size()==0)
            return 0;
        return list.stream().mapToDouble(x->x).average().getAsDouble();
    }
    /**
     * Return median of an Integer list
     * @param list
     * @return
     */
    public static int medianInt(List<Integer> list){
        if(list.size()==0)
            return 0;
        List<Integer> sorted = list.stream().sorted().collect(Collectors.toList());
        return sorted.get(sorted.size()/2);
    }

    /**
     * Return median of a Long list
     * @param list
     * @return
     */
    public static Long medianLong(List<Long> list){
        if(list.size()==0)
            return 0L;
        List<Long> sorted = list.stream().sorted().collect(Collectors.toList());
        return sorted.get(sorted.size()/2);
    }

    /**
     * Return median of a Double list
     * @param list
     * @return
     */
    public static Double medianDouble(List<Double> list){
        if(list.size()==0)
            return 0.0;
        List<Double> sorted = list.stream().sorted().collect(Collectors.toList());
        return sorted.get(sorted.size()/2);
    }

    /**
     * Return std of an Integer list
     * @param list
     * @return
     */
    public static double stdInt(List<Integer> list,double avg){
        if(list.size()==0)
            return 0;
        return Math.sqrt(list.stream().mapToDouble(v -> (v-avg)*(v-avg)).sum()/list.size());
    }
    /**
     * Return std of an Integer list
     * @param list
     * @return
     */
    public static double stdLong(List<Long> list,double avg){
        if(list.size()==0)
            return 0;
        return Math.sqrt(list.stream().mapToDouble(v -> (v-avg)*(v-avg)).sum()/list.size());
    }
    /**
     * Return std of a Double list
     * @param list
     * @return
     */
    public static double stdDouble(List<Double> list,double avg){
        if(list.size()==0)
            return 0;
        return Math.sqrt(list.stream().mapToDouble(v -> (v-avg)*(v-avg)).sum()/list.size());
    }

    public static Stat<Long> getStatsLong(List<Long> list){
        double average = Calculations.averageLong(list);
        long median = Calculations.medianLong(list);
        double std = Calculations.stdLong(list,average);
        return new Stat<>(average,median,std);
    }
    public static Stat<Double> getStatsDouble(List<Double> list){
        double average = Calculations.averageDouble(list);
        double median = Calculations.medianDouble(list);
        double std = Calculations.stdDouble(list,average);
        return new Stat<>(average,median,std);
    }
    public static Stat<Integer> getStatsInt(List<Integer> list){
        double average = Calculations.averageInt(list);
        int median = Calculations.medianInt(list);
        double std = Calculations.stdInt(list,average);
        return new Stat<>(average,median,std);
    }

}

