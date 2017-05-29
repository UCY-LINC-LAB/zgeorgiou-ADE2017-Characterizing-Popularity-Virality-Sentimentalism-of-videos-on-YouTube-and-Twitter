package com.zgeorg03.analysis.models;

import com.google.gson.JsonObject;
import com.zgeorg03.utils.BsonModel;
import com.zgeorg03.utils.JsonModel;
import org.bson.Document;

import java.util.Map;

/**
 * Created by zgeorg03 on 3/7/17.
 */
public class SentimentJson implements JsonModel {
    private final Stat<Double> neg;
    private final Stat<Double> pos;
    private final Stat<Double> neu;
    private final Stat<Double> compound;
    private final boolean notEnoughData;

    public SentimentJson(Stat<Double> neg, Stat<Double> pos, Stat<Double> neu, Stat<Double> compound) {
        this.neg = neg;
        this.pos = pos;
        this.neu = neu;
        this.compound = compound;
        notEnoughData = neg.isNotANumber() || pos.isNotANumber() || neu.isNotANumber() || compound.isNotANumber();
    }
    public SentimentJson(boolean notEnoughData) {
        this.neg = null;
        this.pos = null;
        this.neu = null;
        this.compound = null;
        this.notEnoughData = true;
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        if(notEnoughData) {
            object.addProperty("msg", "Not enough data");
            return  object;
        }
        object.add("neg",neg.toJson());
        object.add("pos",pos.toJson());
        object.add("neu",neu.toJson());
        object.add("compound",compound.toJson());
        return object;
    }

    @Override
    public JsonObject toJson(Map<String, Integer> view) {
        return null;
    }

    public Stat<Double> getCompound() {
        return compound;
    }

    public Stat<Double> getNeg() {
        return neg;
    }

    public Stat<Double> getNeu() {
        return neu;
    }

    public Stat<Double> getPos() {
        return pos;
    }

    public boolean isValid() {
        return  !notEnoughData;
    }
}
