package com.zgeorg03.controllers.helpers;

import com.google.gson.*;

/**
 * Created by zgeorg03 on 12/3/16.
 */
public class JsonResult {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private JsonArray errors = new JsonArray();
    private JsonObject data = new JsonObject();

    public JsonResult addError(String error){ errors.add(error); return this;}
    public JsonResult addElement(String key, JsonElement value){ data.add(key,value); return this; }
    public JsonResult addString(String key, String value){ data.addProperty(key,value); return this; }

    public void setData(JsonObject data){
        this.data=data;
    }
    public String build(){
        JsonObject result = new JsonObject();
        result.add("error", errors);
        result.add("data", data);
        return gson.toJson(result);

    }

    public boolean hasError(){
        return errors.size()!=0;
    }
}
