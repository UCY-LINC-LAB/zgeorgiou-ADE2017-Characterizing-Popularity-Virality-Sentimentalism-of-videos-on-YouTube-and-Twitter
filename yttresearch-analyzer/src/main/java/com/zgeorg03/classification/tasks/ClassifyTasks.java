package com.zgeorg03.classification.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 2/3/17.
 */
public class ClassifyTasks implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(ClassifyTasks.class);

    private final ExecutorCompletionService<List<String>> executorCompletionService;
    private final File gbdt;
    @SuppressWarnings("unchecked")
    public ClassifyTasks(String path){
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        executorCompletionService = new ExecutorCompletionService(executorService);
        File path1 = Paths.get(path).toFile();
        this.gbdt = Paths.get(path,"classify","GBDT.py").toFile();
        if(!gbdt.exists())
            logger.error("Classifer: GBDT.py doesn't exist in"+ path1.getAbsolutePath());


    }

    //TODO Add a new Task
    public void addTask(String path,int train_wnd,int offset,int lbl_wnd,String youtube_features_binary,String twitter_features_binary){
        logger.info("Submitting classification task...");
        executorCompletionService.submit(
                new ClassifyProcess(gbdt.getPath(),path,train_wnd,offset,lbl_wnd,youtube_features_binary,twitter_features_binary) );

    }


    @Override
    public void run() {
        logger.info("ClassifyTasks started...");
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
