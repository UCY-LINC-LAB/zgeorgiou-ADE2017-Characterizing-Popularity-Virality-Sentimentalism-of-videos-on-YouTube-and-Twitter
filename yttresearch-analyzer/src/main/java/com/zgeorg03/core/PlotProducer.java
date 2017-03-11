package com.zgeorg03.core;

import com.google.gson.JsonObject;
import com.zgeorg03.analysis.Groups;
import com.zgeorg03.analysis.models.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by zgeorg03 on 3/11/17.
 */
public class PlotProducer {
    private  static final Logger logger = LoggerFactory.getLogger(PlotProducer.class);
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    private final File path;


    public PlotProducer(String path) {
        this.path = Paths.get(path).toFile();
        if(!this.path.exists()||!this.path .isDirectory()){
            if(this.path.mkdirs())
                logger.info("Created:"+this.path.getAbsolutePath());
        }
    }

    public String producePLot(String exp, List<String> input,String plotName){

        Plot plot = new Plot(input, exp, plotName);
        try {
            return plot.call();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return "ERROR";
        }

    }

    public JsonObject produceHighLevelPlots(Groups groups) {
        JsonObject object = new JsonObject();
        object.addProperty("views_average_daily_increase",
                convertAndProduceGroupsPlot(groups.getExperimentId(),
                        "views_average_daily_increase",
                        "Average daily increase in View count","Time(Days)","View Increase",
                        true,groups.getViewsAverageDailyIncrease()));

        object.addProperty("tweets_average_daily_increase",
                convertAndProduceGroupsPlot(groups.getExperimentId(),
                        "tweets_average_daily_increase",
                        "Average number of tweet mentions","Time(Days)","Count",
                        false,groups.getTweetsAverageDailyIncrease()));

        object.addProperty("ratio_original_total_tweets",
                convertAndProduceGroupsPlot(groups.getExperimentId(),
                        "ratio_original_total_tweets",
                        "Average Ratio between Original and total Tweets","Time(Days)","Ratio",
                        false,groups.getRatioOriginalTotalTweets()));

        object.addProperty("average_possible_users_reached",
                convertAndProduceGroupsPlot(groups.getExperimentId(),
                        "average_possible_users_reached",
                        "Average number of (possible)users reached per day","Time(Days)","Ratio",
                        false,groups.getAverageUsersReached()));

        object.addProperty("videos_age_distribution",
                produceVideoDistributionGraph(groups.getExperimentId(),
                        "videos_age_distribution",
                        "Distribution of video age for different classes","Time(Days)","Count",
                        groups.getVideosDistribution()));

        object.addProperty("vides_duration",
                produceBar(groups.getExperimentId(),
                        "videos_duration",
                        "Average duration of the videos ","Classes","Time(seconds)",
                        groups.getAverageDuration()));

        return object;
    }


    private String convertAndProduceGroupsPlot(String experimentId, String fileName, String titleBar, String xlabel, String ylabel, boolean logscale, Map<String,List<Double>> data){
        int min = data.values().stream().map(x->x.size()).mapToInt(x->x).min().getAsInt();

        String x_axis = IntStream.range(1,min+1).boxed().map(x->x+"").collect(Collectors.joining(",","[","]"));
        String popular = data.get("popular").stream().limit(min).map(x -> x+"") .collect(Collectors.joining(",","[","]"));
        String viral = data.get("viral").stream().limit(min).map(x -> x+"") .collect(Collectors.joining(",","[","]"));
        String recent = data.get("recent").stream().limit(min).map(x -> x+"") .collect(Collectors.joining(",","[","]"));
        String random = data.get("random").stream().limit(min).map(x -> x+"") .collect(Collectors.joining(",","[","]"));
        String popular_viral = data.get("popular_viral").stream().limit(min).map(x -> x+"") .collect(Collectors.joining(",","[","]"));
        String popular_not_viral = data.get("popular_not_viral").stream().limit(min).map(x -> x+"") .collect(Collectors.joining(",","[","]"));
        String viral_not_popular = data.get("viral_not_popular").stream().limit(min).map(x -> x+"") .collect(Collectors.joining(",","[","]"));

        List<String> input = Arrays.asList(
                (logscale)?"plt.yscale('log')"  : "",
                "plt.title('"+titleBar+"')",
                "plt.xlabel('"+xlabel+"')",
                "plt.ylabel('"+ylabel+"')",
                "plt.plot("+x_axis+","+viral+",label='Viral',color='b',marker='.',linestyle='-')",
                "plt.plot("+x_axis+","+popular+",label='Popular',color='g',marker='D',linestyle='--')",
                "plt.plot("+x_axis+","+recent+",label='Recent',color='c',marker='o',linestyle=':')",
                "plt.plot("+x_axis+","+random+",label='Random',color='r',marker='+',linestyle='-.')",
                "plt.plot("+x_axis+","+popular_viral+",label='Popular & Viral',color='m',marker='^',linestyle='-')",
                "plt.plot("+x_axis+","+popular_not_viral+",label='Popular & Not Viral',color='k',marker='*',linestyle='-.')",
                "plt.plot("+x_axis+","+viral_not_popular+",label='Viral & Not Popular',color='y',marker='s',linestyle='--')",
                "plt.legend(loc='best',fancybox='True',framealpha=0.5)"
        );

        Plot plot = new Plot(input, experimentId, fileName);
        try {
            executorService.submit(plot);
            return plot.url;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return "ERROR";
        }
    }

    private String produceBar(String experimentId, String fileName, String titleBar, String xlabel, String ylabel, Map<String,Stat<Integer>> data){
        List<String> keys= new LinkedList<>();
        List<String> values = new LinkedList<>();
        List<String> err = new LinkedList<>();
        data.entrySet().stream().sorted((e1,e2)->e2.getValue().getAverage().compareTo(e1.getValue().getAverage()))
                .forEach(entry -> {
                    keys.add("'"+entry.getKey()+"'");
                    values.add(entry.getValue().getAverage()+"");
                    err.add((entry.getValue().getStd()/2)+"");
                });
        String objects = keys.stream().collect(Collectors.joining(",","(",")"));
        String performance = values.stream().collect(Collectors.joining(",","[","]"));

        List<String> input = Arrays.asList(
                "import numpy as np",
                "objects = "+objects,
                "y_pos = np.arange(len(objects))",
                "performance = " +performance,
                "error = " +err,
                "plt.bar(y_pos, performance,yerr=error, align='center',alpha=0.5)",
                "plt.xticks(y_pos,objects)",
                "plt.title('"+titleBar+"')",
                "plt.xlabel('"+xlabel+"')",
                "plt.ylabel('"+ylabel+"')"
        );

        Plot plot = new Plot(input, experimentId, fileName);
        try {
            executorService.submit(plot);
            return plot.url;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return "ERROR";
        }
    }

    private String produceVideoDistributionGraph(String experimentId,String fileName, String titleBar,String xlabel,String ylabel, Map<String, List<Map.Entry<Integer, Double>>> map){
        String popularValues = map.get("popular").stream().map(x -> String.format("%.5f", x.getValue())).collect(Collectors.joining(",", "np.cumsum([", "])"));
        String popularX = map.get("popular").stream().map(x -> x.getKey() + "").collect(Collectors.joining(",", "[", "]"));

        String viralValues = map.get("viral").stream().map(x -> String.format("%.5f", x.getValue())).collect(Collectors.joining(",", "np.cumsum([", "])"));
        String viralX = map.get("viral").stream().map(x -> x.getKey() + "").collect(Collectors.joining(",", "[", "]"));

        String randomValues = map.get("random").stream().map(x -> String.format("%.5f", x.getValue())).collect(Collectors.joining(",", "np.cumsum([", "])"));
        String randomX = map.get("random").stream().map(x -> x.getKey() + "").collect(Collectors.joining(",", "[", "]"));

        String viral_popular_values = map.get("popular_viral").stream().map(x -> String.format("%.5f", x.getValue())).collect(Collectors.joining(",", "np.cumsum([", "])"));
        String viral_popularX = map.get("popular_viral").stream().map(x -> x.getKey() + "").collect(Collectors.joining(",", "[", "]"));

        List<String> input = Arrays.asList(
                "import numpy as np",
                "plt.title('"+titleBar+"')",
                "plt.xlabel('"+xlabel+"')",
                "plt.ylabel('"+ylabel+"')",
                "plt.xscale('log')",
                "plt.ylim([0,1])",
                "plt.plot(" + popularX + "," + popularValues + ",label='Popular')",
                "plt.plot(" + viralX + "," + viralValues + ",label='Viral')",
                "plt.plot(" + randomX + "," + randomValues + ",label='Random')",
                "plt.plot(" + viral_popularX + "," + viral_popular_values + ",label='Popular & Viral')",
                "plt.legend(loc='best',fancybox='True',framealpha=0.5)"
        );
        Plot plot = new Plot(input, experimentId, fileName);
        try {
            executorService.submit(plot);
            return plot.url;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return "ERROR";
        }
    }
    public byte[] readPlot(String experimentId,String plotName) {
        Path path = Paths.get(this.path.getAbsolutePath(),experimentId,plotName+".png");
        try {
            return  Files.readAllBytes(path);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }

        return null;
    }


    /**
     * Produce a plot and return its file path
     */
    private class Plot implements Callable<String> {
        private  List<String> input;
        private final  String path;
        BufferedReader in;
        BufferedReader error;
        PrintWriter out; //Send text to the stdin of the process
        private final String url;

        public Plot(List<String> input, String experimentId, String plotName){
            File experimentPath = Paths.get(PlotProducer.this.path.getAbsolutePath(),experimentId).toFile();
            if(!experimentPath.exists()) {
                if(experimentPath.mkdirs())
                    logger.info("Experiment:"+experimentId+" created!");
            }
            path = Paths.get(experimentPath.getAbsolutePath(),plotName).toFile().getAbsolutePath()+".png";


            this.input = new LinkedList<>();
            this.input.add("import matplotlib as mpl");
            this.input.add("mpl.use('Agg')");
            this.input.add("import matplotlib.pyplot as plt");
            this.input.add("plt.figure(figsize=(12, 9), dpi=600)");
            this.input.addAll(input);
            this.input.add("plt.savefig('"+ path +"')");
            url = "/plots/"+experimentId+"/"+plotName;
        }

        @Override
        public String call() throws Exception {
            ProcessBuilder builder = new ProcessBuilder("python");
            try {
                Process process = builder.start();
                in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                out = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));
                input.forEach(line -> out.println(line));
                out.close();
                process.waitFor();
                List<String> errorLines = readLines(error);
                if (errorLines.size() != 0) {
                    errorLines.add(0, "error");
                    logger.error(errorLines.stream().collect(Collectors.joining("\n")));
                    return errorLines.stream().collect(Collectors.joining("\n"));
                }


                logger.info("Plot:" + url+ " produced");
                return url;
            } catch (IOException e) {
                logger.info("Plot:" + url+ " didn't produced:"+e.getMessage());
                return e.getMessage();
            } catch (InterruptedException e) {
                logger.info("Plot:" + url+ " didn't produced:"+e.getMessage());
                logger.error(e.getMessage());
                return e.getMessage();
            }

        }

        private List<String> readLines(BufferedReader reader) throws IOException {
            List<String> lines = new LinkedList<>();
            String input = "";
            while ((input = reader.readLine()) != null) {
                lines.add(input);
            }

            return lines;
        }
    }

}
