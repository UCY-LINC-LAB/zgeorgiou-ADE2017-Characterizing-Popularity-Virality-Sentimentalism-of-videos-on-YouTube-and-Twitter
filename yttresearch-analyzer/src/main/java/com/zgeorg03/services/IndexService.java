package com.zgeorg03.services;

import com.zgeorg03.database.DBServices;
import com.zgeorg03.services.helpers.Service;

/**
 * Created by zgeorg03 on 3/2/17.
 */
public class IndexService extends Service {

    public IndexService(DBServices dbServices) {
        super(dbServices);
    }




    /**
     * Get the number of videos that are finished but not processed
     * @return
     */
    public int getFinishedButNotProcessedVideosCount(){
        return dbServices.getFinishedButNotProcessedVideosCount();

    }

    /**
     * Retrieve the database name
     * @return
     */
    public String getDatabaseName(){
        return dbServices.getDatabaseName();
    }
}
