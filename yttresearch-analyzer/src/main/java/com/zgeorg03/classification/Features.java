package com.zgeorg03.classification;

import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 3/12/17.
 */
public class Features extends TreeMap<String,Double> {


    public boolean add(String key,Double value){
       return this.put(key,value)==null;
    }

    @Override
    public String toString(){

        return  this.values().stream().map(d->String.format("%.4f",d)).collect(Collectors.joining("\t"));
    }
}
