package com.zgeorg03.core.controllers.helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zgeorg03.core.controllers.IndexController;
import com.zgeorg03.core.utils.JsonResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by zgeorg03 on 2/20/17.
 * A generic controller
 */
public abstract class Controller {



    private Method findMethod(String name,Class<? extends GetRequest> clazz){
        for (Method method :clazz.getMethods()) {
            if (method.getName().equals(name))
                return method;
        }
        return null;
    }
}
