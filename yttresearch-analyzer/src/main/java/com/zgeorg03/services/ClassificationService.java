package com.zgeorg03.services;

import com.google.gson.JsonObject;
import com.zgeorg03.classification.ClassManager;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.services.helpers.Service;

/**
 * Created by zgeorg03 on 3/17/17.
 */
public class ClassificationService extends Service {

    private final ClassManager classManager;

    public ClassificationService(DBServices dbServices, ClassManager classManager) {
        super(dbServices);
        this.classManager = classManager;
    }


    public JsonObject getClassification(String id) {
        return classManager.getClassificationResults(id);
    }
}

