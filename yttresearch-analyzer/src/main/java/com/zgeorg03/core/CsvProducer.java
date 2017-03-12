package com.zgeorg03.core;

import com.google.gson.JsonObject;
import com.zgeorg03.analysis.Groups;
import com.zgeorg03.analysis.models.Stat;
import com.zgeorg03.analysis.models.Video;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by zgeorg03 on 3/11/17.
 */
public class CsvProducer {
    private  static final Logger logger = LoggerFactory.getLogger(CsvProducer.class);

    private final File path;

    public CsvProducer(String path) {
        this.path = Paths.get(path,"csv").toFile();
        if(!this.path.exists()||!this.path .isDirectory()){
            if(this.path.mkdirs())
                logger.info("Created:"+this.path.getAbsolutePath());
        }
    }

    public String writeCsv(long id,List<Video> videos){
        File file = Paths.get(path.getAbsolutePath(),id+".csv").toFile();
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            videos.stream().forEach( v-> pw.println(v.getCsvForm()));
            pw.close();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
            return "Failed to write to csv";
        }

        return "/csv/"+id;
    }

    public byte[] readCsv(String id) {
        Path path = Paths.get(this.path.getAbsolutePath(),id+".csv");
        try {
            return  Files.readAllBytes(path);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }

        return null;
    }
}
