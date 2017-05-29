package com.zgeorg03.analysis.models;

import com.google.gson.JsonObject;
import com.zgeorg03.utils.BsonModel;
import com.zgeorg03.utils.JsonModel;
import org.bson.Document;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by zgeorg03 on 3/4/17.
 */
public class Stat<T> implements JsonModel,BsonModel{
    private final Double average;
    private final T median;
    private final Double std;

    private final boolean error;

    public Stat(Double average, T median, Double std) {
        this.average = average;
        this.median = median;
        this.std = std;
        error = average.isInfinite() || average.isNaN() || std.isNaN() || std.isInfinite();
    }
    public Stat(T type,Document document){

        this.average = document.getDouble("average");
            if(type instanceof  Long){
                this.median = (T) document.getLong("median");
            }else if(type instanceof  Integer){
                this.median = (T) document.getInteger("median");
            }else if(type instanceof  Double|| type instanceof  Float)
                this.median = (T) document.getDouble("median");
            else {
                LoggerFactory.getLogger(Stat.class).error("Type:::"+type);
                this.median = type;
            }
        this.std = document.getDouble("std");

        error = average.isInfinite() || average.isNaN() || std.isNaN() || std.isInfinite();
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("average",average);
        if(average.isInfinite()||average.isNaN()) {
                    new Throwable().printStackTrace();
            LoggerFactory.getLogger(Stat.class).error("Should not happen, Infinite or NaN");
        }
        if(median instanceof  Long)
            object.addProperty("median", ((Number) median).longValue());
        else if(median instanceof Integer )
            object.addProperty("median", ((Number) median).intValue());
        else if(median instanceof Double )
            object.addProperty("median", ((Number) median).doubleValue());
        else if(median instanceof Float )
            object.addProperty("median", ((Number) median).floatValue());
        else {
            LoggerFactory.getLogger(Stat.class).error("Should not happen Type:::"+median);
            object.addProperty("median", 0);
        }
        object.addProperty("std",std);

        return object;
    }

    @Override
    public JsonObject toJson(Map<String, Integer> view) {
        return toJson();
    }

    public Double getAverage() {
        return average;
    }

    public Double getStd() {
        return std;
    }

    @Override
    public Document toBson() {
        Document object = new Document();
        object.append("average",average);
        object.append("median",  median);
        object.append("std",std);
        return object;
    }

    public boolean isNotANumber() {
        if(average.isInfinite()||average.isNaN())
            return true;
        return std.isInfinite() || average.isNaN();
    }
}
