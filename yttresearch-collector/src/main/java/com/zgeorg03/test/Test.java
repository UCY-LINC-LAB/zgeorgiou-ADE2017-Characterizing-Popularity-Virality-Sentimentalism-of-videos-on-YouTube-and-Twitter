package com.zgeorg03.test;

import com.mongodb.ServerAddress;
import com.zgeorg03.database.DBConnection;
import com.zgeorg03.database.DBServices;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    public static void main(String args[]){

        //Core Jobs
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        DBConnection dbConnection = new DBConnection("yttresearch-test",new ServerAddress("10.16.3.12"));
        DBServices dbServices = new DBServices(dbConnection);
        StatusMonitor statusMonitor = new StatusMonitor(dbServices);


        executorService.execute(statusMonitor);

    }
}

