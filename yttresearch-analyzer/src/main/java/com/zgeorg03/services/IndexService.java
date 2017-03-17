package com.zgeorg03.services;

import com.zgeorg03.core.CsvProducer;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.services.helpers.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * Created by zgeorg03 on 3/2/17.
 */
public class IndexService extends Service {

    private Logger logger  = LoggerFactory.getLogger(IndexService.class);
    private final CsvProducer csvProducer;
    private final ExecutorService executorService;
    public IndexService(DBServices dbServices, CsvProducer csvProducer, ExecutorService executorService) {
        super(dbServices);
        this.csvProducer = csvProducer;
        this.executorService = executorService;
    }




    /**
     * Get the number of videos that are finished but not processed
     * @return
     */
    public int getFinishedButNotProcessedVideosCount(){
        return dbServices.getFinishedButNotProcessedVideosCount();

    }

    /**
     * Get the number of videos that are finished and processed
     * @return
     */
    public int getFinishedAndProcessedVideosCount(){
        return dbServices.getFinishedAndProcessedVideosCount();

    }

    /**
     * Retrieve the database name
     * @return
     */
    public String getDatabaseName(){
        return dbServices.getDatabaseName();
    }
}
