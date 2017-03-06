package com.zgeorg03.analysis.models;

import com.google.gson.JsonObject;
import com.zgeorg03.utils.JsonModel;
import com.zgeorg03.utils.BsonModel;
import org.bson.Document;

import java.util.Map;

/**
 * Created by zgeorg03 on 3/7/17.
 */
public class SentimentBson implements BsonModel {
    private final Stat<Double> neg;
    private final Stat<Double> pos;
    private final Stat<Double> neu;
    private final Stat<Double> compound;

    public SentimentBson(Stat<Double> neg, Stat<Double> pos, Stat<Double> neu, Stat<Double> compound) {
        this.neg = neg;
        this.pos = pos;
        this.neu = neu;
        this.compound = compound;
    }

    @Override
    public Document toBson() {
        Document object = new Document();
        object.append("neg",neg.toBson());
        object.append("pos",pos.toBson());
        object.append("neu",neu.toBson());
        object.append("compound",compound.toBson());
        return object;
    }

}
