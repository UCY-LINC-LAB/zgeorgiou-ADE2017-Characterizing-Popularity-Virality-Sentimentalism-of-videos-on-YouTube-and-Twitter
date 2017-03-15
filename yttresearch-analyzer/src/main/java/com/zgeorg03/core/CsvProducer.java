package com.zgeorg03.core;

import com.zgeorg03.analysis.models.Video;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by zgeorg03 on 3/11/17.
 */
public class CsvProducer {
    private  static final Logger logger = LoggerFactory.getLogger(CsvProducer.class);

    private final File root;
    private final File path;

    public CsvProducer(String path) {
        this.path = Paths.get(path,"csv").toFile();
        if(!this.path.exists()||!this.path .isDirectory()){
            if(this.path.mkdirs())
                logger.info("Created:"+this.path.getAbsolutePath());
        }
        this.root = Paths.get(path).toFile();
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

    public byte[] readExperimentCsv(String id, String title) {
        Path path = Paths.get(this.root.getAbsolutePath(),id,title+".csv");
        try {
            return  Files.readAllBytes(path);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }

        return null;
    }

    public class WriteCSV implements Callable<String>{

        private final long id;
        private final List<Video> videos;

        public WriteCSV(long id, List<Video> videos) {
            this.id = id;
            this.videos = videos;
        }

        @Override
        public String call() throws Exception {
            File file = Paths.get(path.getAbsolutePath(),id+".csv").toFile();
            try {
                PrintWriter pw = new PrintWriter(new FileWriter(file));
                pw.println(videos.get(0).getCsvTitles());
                videos.stream().forEach( v-> pw.println(v.getCsvForm()));
                pw.close();
                logger.info("Successful write to csv:"+id);
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage());
                return "Failed to write to csv";
            }

            return "/csv/"+id;
        }
    }
    public class WriteCSVAnalysis implements Callable<String>{

        private final String experimentId;
        private final List<Video> videos;

        public WriteCSVAnalysis(String experimentId,  List<Video> videos) {
            this.experimentId = experimentId;
            this.videos = videos;
        }

        @Override
        public String call() throws Exception {
            File dir = Paths.get(root.getAbsolutePath(),experimentId).toFile();
            if(!dir.exists()){
                if(dir.mkdirs())
                    logger.info("Creating dir:"+dir.getAbsolutePath());
            }
            File file = Paths.get(root.getAbsolutePath(),experimentId,"videos_features.csv").toFile();
            try {
                PrintWriter pw = new PrintWriter(new FileWriter(file));
                pw.println(videos.get(0).getCsvTitles());
                videos.stream().forEach( v-> pw.println(v.getCsvForm()));
                pw.close();
                logger.info("Successful write to csv:videos_features.csv");
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage());
                return "Failed to write to csv";
            }

            return "/"+experimentId+"/videos_features.csv";
        }
    }
}
