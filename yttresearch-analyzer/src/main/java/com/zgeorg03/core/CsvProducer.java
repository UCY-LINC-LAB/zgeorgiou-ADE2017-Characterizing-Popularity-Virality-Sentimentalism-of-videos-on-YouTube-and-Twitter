package com.zgeorg03.core;

import com.zgeorg03.analysis.models.Video;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Created by zgeorg03 on 3/11/17.
 */
public class CsvProducer {
    private  static final Logger logger = LoggerFactory.getLogger(CsvProducer.class);

    private final File root;

    public CsvProducer(String path) {
        this.root = Paths.get(path).toFile();
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

    public class WriteCSVAnalysis implements Callable<String>{

        private final String experimentId;
        private final List<Video> videos;

        private final Set<String> popular;
        private final Set<String> viral;

        public WriteCSVAnalysis(String experimentId, List<Video> videos, Set<String> popular, Set<String> viral) {
            this.experimentId = experimentId;
            this.videos = videos;
            this.popular = popular;
            this.viral = viral;
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
                videos.stream().forEach( v-> {
                    boolean isPopular = popular.contains(v.getVideo_id());
                    boolean isViral = viral.contains(v.getVideo_id());
                    pw.println(v.getCsvForm(isPopular,isViral));

                });
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
