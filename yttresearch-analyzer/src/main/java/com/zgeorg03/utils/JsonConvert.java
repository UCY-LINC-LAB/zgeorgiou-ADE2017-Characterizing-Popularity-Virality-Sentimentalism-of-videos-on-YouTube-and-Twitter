package com.zgeorg03.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;

/**
 * Created by zgeorg03 on 3/3/17.
 */
public class JsonConvert {
    private static final Gson gson = new Gson();

    public static Document gsonToBson(JsonElement element){
        String json = gson.toJson(element);
        return Document.parse(json);

    }

    public static JsonElement bsonToGson(Document document){
        return gson.toJsonTree(document.toJson());

    }
}
