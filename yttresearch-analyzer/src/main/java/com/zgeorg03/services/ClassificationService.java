package com.zgeorg03.services;

import com.google.gson.JsonObject;
import com.zgeorg03.classification.ClassificationManager;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.services.helpers.Service;

/**
 * Created by zgeorg03 on 3/17/17.
 */
public class ClassificationService extends Service {

    private final ClassificationManager classificationManager;

    public ClassificationService(DBServices dbServices, ClassificationManager classificationManager) {
        super(dbServices);
        this.classificationManager = classificationManager;
    }


    public JsonObject getClassification(String id) {
        return classificationManager.getClassificationResults(id);
    }
}

