package com.zgeorg03.classification.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 2/3/17.
 */
public class ClassifyTasksMonitor implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(ClassifyTasksMonitor.class);

    private final ExecutorService executorService;
    private final ExecutorCompletionService<List<String>> executorCompletionService;
    private final String path;
    public ClassifyTasksMonitor(String path, int exitCode, String error) {
        executorService = Executors.newFixedThreadPool(8);
        executorCompletionService = new ExecutorCompletionService(executorService);
        this.path = path;
        //this.exitCode = exitCode;
        //this.error = error;
    }

    //TODO Add a new Task
    public void add(){
        logger.info("Submitting classification task ");

        //executorCompletionService.submit(
         //       new ClassifyProcess(scriptPath,path,train_wnd,offset,lbl_wnd,youtube_features_binary,twitter_features_binary) );

    }


    @Override
    public void run() {
        while(true){
            Future<List<String>> taskFuture = null;
            try {
                taskFuture = executorCompletionService.take();
                List<String> task = taskFuture.get();
                logger.info("Task: " +task.stream().collect(Collectors.joining("\n")));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

    }






}
