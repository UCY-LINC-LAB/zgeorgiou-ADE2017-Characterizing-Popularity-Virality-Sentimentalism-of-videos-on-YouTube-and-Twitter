package com.zgeorg03.utils;

import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Created by zgeorg03 on 3/3/17.
 */
public interface JsonModel {

    JsonObject toJson();

    JsonObject toJson(Map<String,Integer> view);
}
