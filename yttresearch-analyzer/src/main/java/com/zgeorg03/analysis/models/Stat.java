package com.zgeorg03.analysis.models;

import com.google.gson.JsonObject;
import com.zgeorg03.utils.BsonModel;
import com.zgeorg03.utils.JsonModel;
import org.bson.Document;

import java.util.Map;

/**
 * Created by zgeorg03 on 3/4/17.
 */
public class Stat<T> implements JsonModel,BsonModel{
    private final Double average;
    private final T median;
    private final Double std;

    public Stat(Double average, T median, Double std) {
        this.average = average;
        this.median = median;
        this.std = std;
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("average",average);
        if(median instanceof  Long || median instanceof Integer || median instanceof Double || median instanceof Float )
            object.addProperty("median", (Number) median);
        else if(median instanceof Boolean)
            object.addProperty("median", (Boolean) median);
        else
            object.addProperty("median", "Incorrect type");
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

    @Override
    public Document toBson() {
        Document object = new Document();
        object.append("average",average);
        object.append("median",  median);
        object.append("std",std);
        return object;
    }
}