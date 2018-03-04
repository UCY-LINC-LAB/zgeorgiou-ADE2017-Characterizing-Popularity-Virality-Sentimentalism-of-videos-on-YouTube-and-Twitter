package com.zgeorg03;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zgeorg03.analysis.Group;
import com.zgeorg03.analysis.Groups;
import com.zgeorg03.analysis.models.Stat;
import com.zgeorg03.analysis.sentiment.SentimentVideo;
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
        Map<String, SentimentVideo> sentimentVideos = groups.toSentimentVideos();
        JsonObject object = new JsonObject();

        //object.add("popular_viral_graphs",
         //       produce3GroupsSentiment(groups.getPopular_not_viral(),groups.getViral_not_popular(),groups.getPopular_viral(),sentimentVideos));

        object.addProperty("groups_venn",
                produceVenn(groups.getExperimentId(),
                        "groups_venn",
                        "Venn Diagram",
                        groups.getGroupsPercentages()));

        object.addProperty("views_average_daily_increase",
                convertAndProduceGroupsPlot(groups.getExperimentId(),
                        "views_average_daily_increase",
                        "Average daily increase in View count","Time(Days)","View Increase",
                        true,groups.getViewsAverageDailyIncrease()));

        object.addProperty("views_median_daily_increase",
                convertAndProduceGroupsPlot(groups.getExperimentId(),
                        "views_median_daily_increase",
                        "Median daily increase in View count","Time(Days)","View Increase",
                        true,groups.getViewsMedianDailyIncrease()));

        object.addProperty("tweets_average_daily_increase",
                convertAndProduceGroupsPlot(groups.getExperimentId(),
                        "tweets_average_daily_increase",
                        "Average number of tweet mentions","Time(Days)","Count",
                        false,groups.getTweetsAverageDailyIncrease()));

        object.addProperty("tweets_median_daily_increase",
                convertAndProduceGroupsPlot(groups.getExperimentId(),
                        "tweets_median_daily_increase",
                        "Median number of tweet mentions","Time(Days)","Count",
                        false,groups.getTweetsMedianDailyIncrease()));

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

        object.addProperty("median_possible_users_reached",
                convertAndProduceGroupsPlot(groups.getExperimentId(),
                        "median_possible_users_reached",
                        "Median number of (possible)users reached per day","Time(Days)","Ratio",
                        false,groups.getMedianUsersReached()));

        object.addProperty("videos_age_distribution",
                produceVideoDistributionGraph(groups.getExperimentId(),
                        "videos_age_distribution",
                        "Distribution of video age for different classes","Time(Days)","Count",
                        groups.getVideosDistribution()));

        object.addProperty("videos_duration",
                produceIntegerBar(groups.getExperimentId(),
                        "videos_duration",
                        "Average duration of the videos ","Classes","Time(seconds)",
                        groups.getAverageDuration()));

        object.addProperty("negative_sentiment",
                produceDoubleBar(groups.getExperimentId(),
                        "negative_sentiment",
                        "Average negative sentiment of the videos ","Classes","Value",
                        groups.getAverageNegativeSentiment()));

        object.addProperty("positive_sentiment",
                produceDoubleBar(groups.getExperimentId(),
                        "positive_sentiment",
                        "Average positive sentiment of the videos ","Classes","Value",
                        groups.getAveragePositiveSentiment()));

        object.addProperty("neutral_sentiment",
                produceDoubleBar(groups.getExperimentId(),
                        "neutral_sentiment",
                        "Average neutral sentiment of the videos ","Classes","Value",
                        groups.getAverageNeutralSentiment()));
        /**
        object.add("tweets_vs_sentiment",produceTweetsVsSentiment(groups));
        object.add("views_vs_sentiment",produceViewsVsSentiment(groups));
         **/
        return object;
    }

    private JsonElement produce3GroupsSentiment(Group g1, Group g2, Group g3, Map<String, SentimentVideo> sentimentVideos) {
        JsonObject result = new JsonObject();
        result.addProperty("views_vs_negative",get3GroupsGraph("views","negative",g1,g2,g3,sentimentVideos,false,true));
        result.addProperty("tweets_vs_negative",get3GroupsGraph("tweets","negative",g1,g2,g3,sentimentVideos,false,true));
        result.addProperty("friends_vs_negative",get3GroupsGraph("friends","negative",g1,g2,g3,sentimentVideos,false,true));
        result.addProperty("followers_vs_negative",get3GroupsGraph("followers","negative",g1,g2,g3,sentimentVideos,false,true));
        result.addProperty("likes_vs_negative",get3GroupsGraph("likes","negative",g1,g2,g3,sentimentVideos,false,true));

        result.addProperty("views_vs_positive",get3GroupsGraph("views","positive",g1,g2,g3,sentimentVideos,false,true));
        result.addProperty("tweets_vs_positive",get3GroupsGraph("tweets","positive",g1,g2,g3,sentimentVideos,false,true));
        result.addProperty("friends_vs_positive",get3GroupsGraph("friends","positive",g1,g2,g3,sentimentVideos,false,true));
        result.addProperty("followers_vs_positive",get3GroupsGraph("followers","positive",g1,g2,g3,sentimentVideos,false,true));
        result.addProperty("likes_vs_positive",get3GroupsGraph("likes","positive",g1,g2,g3,sentimentVideos,false,true));

        result.addProperty("views_vs_neutral",get3GroupsGraph("views","neutral",g1,g2,g3,sentimentVideos,false,true));
        result.addProperty("tweets_vs_neutral",get3GroupsGraph("tweets","neutral",g1,g2,g3,sentimentVideos,false,true));
        result.addProperty("friends_vs_neutral",get3GroupsGraph("friends","neutral",g1,g2,g3,sentimentVideos,false,true));
        result.addProperty("followers_vs_neutral",get3GroupsGraph("followers","neutral",g1,g2,g3,sentimentVideos,false,true));
        result.addProperty("likes_vs_neutral",get3GroupsGraph("likes","neutral",g1,g2,g3,sentimentVideos,false,true));

        result.addProperty("tweets_vs_views",get3GroupsGraph("views","tweets",g1,g2,g3,sentimentVideos,true,true));
        result.addProperty("tweets_vs_retweets",get3GroupsGraph("retweets","tweets",g1,g2,g3,sentimentVideos,true,true));
        result.addProperty("tweets_vs_likes",get3GroupsGraph("likes","tweets",g1,g2,g3,sentimentVideos,true,true));
        result.addProperty("tweets_vs_friends",get3GroupsGraph("friends","tweets",g1,g2,g3,sentimentVideos,true,true));
        result.addProperty("tweets_vs_followers",get3GroupsGraph("followers","tweets",g1,g2,g3,sentimentVideos,true,true));

        result.addProperty("views_vs_retweets",get3GroupsGraph("retweets","views",g1,g2,g3,sentimentVideos,true,true));
        result.addProperty("views_vs_likes",get3GroupsGraph("likes","views",g1,g2,g3,sentimentVideos,true,true));
        result.addProperty("views_vs_friends",get3GroupsGraph("friends","views",g1,g2,g3,sentimentVideos,true,true));
        result.addProperty("views_vs_followers",get3GroupsGraph("followers","views",g1,g2,g3,sentimentVideos,true,true));

        result.addProperty("likes_vs_retweets",get3GroupsGraph("retweets","likes",g1,g2,g3,sentimentVideos,true,true));
        result.addProperty("likes_vs_friends",get3GroupsGraph("friends","likes",g1,g2,g3,sentimentVideos,true,true));
        result.addProperty("likes_vs_followers",get3GroupsGraph("followers","likes",g1,g2,g3,sentimentVideos,true,true));

        result.addProperty("followers_vs_retweets",get3GroupsGraph("retweets","followers",g1,g2,g3,sentimentVideos,true,true));
        result.addProperty("followers_vs_friends",get3GroupsGraph("friends","followers",g1,g2,g3,sentimentVideos,true,true));

        result.addProperty("friends_vs_retweets",get3GroupsGraph("retweets","friends",g1,g2,g3,sentimentVideos,true,true));

        return result;
    }

    private JsonObject produce2GroupsSentiment(Group group1, Group  group2,Map<String,SentimentVideo> videos){
        JsonObject result = new JsonObject();
        result.addProperty("views_vs_negative",get2GroupsGraph("negative","views",group1,group2,videos));
        return result;
    }
    private String get3GroupsGraph(String axisX, String axisY, Group group1, Group group2,Group group3,Map<String,SentimentVideo> videos,boolean logY,boolean logX) {
        Map<Boolean,List<Double>> g1= getXYPoints(axisX,axisY,videos,group1);
        Map<Boolean,List<Double>> g2= getXYPoints(axisX,axisY,videos,group2);
        Map<Boolean,List<Double>> g3= getXYPoints(axisX,axisY,videos,group3);

        return producePointsGraph3("plots",
                "class3_"+axisY+"_"+axisX+"_popular_viral",
                axisY+"_"+axisX+"_popular_viral",
                axisX,axisY,
                g1.get(false),g1.get(true),
                g2.get(false),g2.get(true),
                g3.get(false),g3.get(true)
                ,logX,logY);
    }

    private String get2GroupsGraph(String axisX, String axisY, Group group1, Group group2,Map<String,SentimentVideo> videos) {
        Map<Boolean,List<Double>> g1= getXYPoints(axisX,axisY,videos,group1);
        Map<Boolean,List<Double>> g2= getXYPoints(axisX,axisY,videos,group2);

        return producePointsGraph2("plots",
                "class2_tweets_vs_sentiment_"+group1.getName()+"_"+group2.getName(),
                "tweets_vs_sentiment_"+group1.getName()+"_"+group2.getName(),
                axisX,axisY,
                g1.get(false),g1.get(true),
                g2.get(false),g2.get(true)
                ,true);
    }

    private JsonObject produceViewsVsSentiment(Groups groups) {
        Map<String, SentimentVideo> sentimentVideos = groups.toSentimentVideos();
        JsonObject result = new JsonObject();
        result.addProperty("popular",getViewsVsSentiment(groups.getExperimentId(),sentimentVideos,groups.getPopular()));
        result.addProperty("viral",getViewsVsSentiment(groups.getExperimentId(),sentimentVideos,groups.getViral()));
        result.addProperty("popular_viral",getViewsVsSentiment(groups.getExperimentId(),sentimentVideos,groups.getPopular_viral()));
        result.addProperty("popular_not_viral",getViewsVsSentiment(groups.getExperimentId(),sentimentVideos,groups.getPopular_not_viral()));
        result.addProperty("viral_not_popular",getViewsVsSentiment(groups.getExperimentId(),sentimentVideos,groups.getViral_not_popular()));
        result.addProperty("random",getViewsVsSentiment(groups.getExperimentId(),sentimentVideos,groups.getRandom()));
        result.addProperty("recent",getViewsVsSentiment(groups.getExperimentId(),sentimentVideos,groups.getRecent()));
        return result;
    }

    private JsonObject produceTweetsVsSentiment(Groups groups) {
        Map<String, SentimentVideo> sentimentVideos = groups.toSentimentVideos();
        JsonObject result = new JsonObject();
        result.addProperty("popular",getTweetsVsSentiment(groups.getExperimentId(),sentimentVideos,groups.getPopular()));
        result.addProperty("viral",getTweetsVsSentiment(groups.getExperimentId(),sentimentVideos,groups.getViral()));
        result.addProperty("popular_viral",getTweetsVsSentiment(groups.getExperimentId(),sentimentVideos,groups.getPopular_viral()));
        result.addProperty("popular_not_viral",getTweetsVsSentiment(groups.getExperimentId(),sentimentVideos,groups.getPopular_not_viral()));
        result.addProperty("viral_not_popular",getTweetsVsSentiment(groups.getExperimentId(),sentimentVideos,groups.getViral_not_popular()));
        result.addProperty("random",getTweetsVsSentiment(groups.getExperimentId(),sentimentVideos,groups.getRandom()));
        result.addProperty("recent",getTweetsVsSentiment(groups.getExperimentId(),sentimentVideos,groups.getRecent()));
        return result;
    }

    private String getTweetsVsSentiment(String experimentId,Map<String,SentimentVideo> sentimentVideos, Group group) {
        Map<Boolean,List<Double>> values= getXYPoints("negative","tweets",sentimentVideos,group);

        return producePointsGraph(experimentId, "tweets_vs_sentiment_"+group.getName(),
                "Tweets Vs Negative Sentiment for "+ group.getName(),"Negative Sentiment","Tweets",
                values.get(false),values.get(true),true);

    }
    private String getViewsVsSentiment(String experimentId,Map<String,SentimentVideo> sentimentVideos, Group group) {
        Map<Boolean,List<Double>> values= getXYPoints("negative","views",sentimentVideos,group);

        return producePointsGraph(experimentId, "views_vs_negative_"+group.getName(),
                "Views Vs Negative Sentiment for "+ group.getName(),"Negative Sentiment","Views",
                values.get(false),values.get(true),true);

    }

    private Map<Boolean,List<Double>> getXYPoints(String axisX, String axisY, Map<String,SentimentVideo> sentimentVideos, Group group) {
        Map<Boolean,List<Double>> map=new LinkedHashMap<>();
        List<Double> xx=new LinkedList<>();
        List<Double> yy=new LinkedList<>();
        group.stream().forEach(v ->{
            SentimentVideo video=sentimentVideos.get(v.getVideo_id());
            if(video!=null) {
                double x =getValue(video,axisX);
                double y =getValue(video,axisY);
                xx.add(x);
                yy.add(y);
            }
        });
        map.put(false,xx);
        map.put(true,yy);
        return map;

    }

    double getValue(SentimentVideo video,String feature){
        if(feature.equals("negative")) return video.getNegative();
        if (feature.equals("positive")) return  video.getPositive();
        if (feature.equals("neutral")) return video.getNeutral();
        if(feature.equals("views")) return video.getViews();
        if(feature.equals("tweets")) return video.getTweets();
        if(feature.equals("retweets")) return video.getRetweets();
        if(feature.equals("likes")) return video.getLikes();
        if(feature.equals("friends")) return video.getFriends();
        if(feature.equals("followers")) return video.getFollowers();

        return 0;
    }


    private String convertAndProduceGroupsPlot(String experimentId, String fileName, String titleBar, String xlabel, String ylabel, boolean logscale, Map<String,List<Double>> data){
        int min = data.values().stream().map(List::size).mapToInt(x->x).min().getAsInt();

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

    private String produceIntegerBar(String experimentId, String fileName, String titleBar, String xlabel, String ylabel, Map<String,Stat<Integer>> data){
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
        String colors = getColors(keys);

        List<String> input = Arrays.asList(
                "import numpy as np",
                "objects = "+objects,
                "y_pos = np.arange(len(objects))",
                "performance = " +performance,
                "error = " +err,
                "plt.bar(y_pos, performance,yerr=error,color="+colors+", align='center',alpha=0.5)",
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
    private String produceDoubleBar(String experimentId, String fileName, String titleBar, String xlabel, String ylabel, Map<String,Stat<Double>> data){
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
        String colors = getColors(keys);

        List<String> input = Arrays.asList(
                "import numpy as np",
                "objects = "+objects,
                "y_pos = np.arange(len(objects))",
                "performance = " +performance,
                "error = " +err,
                "plt.bar(y_pos, performance,yerr=error,color="+colors+", align='center',alpha=0.5)",
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

    private String produceDoubleBar(String experimentId, String fileName, String titleBar, String xlabel, String ylabel, List<Stat<Double>> values){
        String objects = IntStream.range(0,values.size()).boxed().map(x->x+"").collect(Collectors.joining(",","(",")"));
        String performance = values.stream().map(x->x.getAverage().toString()).collect(Collectors.joining(",","[","]"));
        String err = values.stream().map(x->(x.getStd()/2)+"").collect(Collectors.joining(",","[","]"));

        List<String> input = Arrays.asList(
                "import numpy as np",
                "objects = "+objects,
                "y_pos = np.arange(len(objects))",
                "performance = " +performance,
                "error = " +err,
                "plt.bar(y_pos,performance, yerr=error,align='edge',alpha=0.5)",
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
    private String getColors(List<String> keys) {
        List<String> colors = new LinkedList<>();
        for(String key:keys){
            if(key.equals("'Viral'"))
                colors.add("'b'");
            else if(key.equals("'Popular'"))
                colors.add("'g'");
            else if(key.equals("'Recent'"))
                colors.add("'c'");
            else if(key.equals("'Random'"))
                colors.add("'r'");
            else if(key.equals("'Popular & Viral'"))
                colors.add("'m'");
            else if(key.equals("'Popular & not Viral'"))
                colors.add("'k'");
            else
                colors.add("'y'");
        }
        return colors.stream().collect(Collectors.joining(",","[","]"));
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

    private String produceVenn(String experimentId, String fileName, String titleBar, List<Double> data){

        String sets = data.stream().map(x->String.format("%.2f",x)).collect(Collectors.joining(",","(",")"));
        List<String> input = Arrays.asList(
                "import numpy as np",
                "from matplotlib_venn import venn3, venn3_circles",
                "sets="+sets,
                "venn3(subsets=sets,set_labels=('Popular','Viral','Recent'))",
                "plt.title('"+titleBar+"')"
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

    public byte[] readClassificationPlot(String experiment, String plotName) {
        String toks[]=experiment.split("_");
        String l="_"+toks[toks.length-3]+toks[toks.length-2]+toks[toks.length-1];

        Path path = Paths.get(this.path.getAbsolutePath(),experiment,"classification_data",plotName+l+".png");
        try {
            return  Files.readAllBytes(path);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }

        return null;
    }
    private String producePointsGraph3(String experimentId, String fileName, String titleBar, String xlabel, String ylabel,List<Double>x1,List<Double>y1,List<Double> x2,List<Double>y2,
                                       List<Double> x3,List<Double> y3,boolean logX,boolean logY) {
        String xx1 = x1.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));
        String yy1 = y1.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));
        String xx2 = x2.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));
        String yy2 = y2.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));
        String xx3 = x3.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));
        String yy3 = y3.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));

        String logy = (logY)?"plt.yscale('log')":"";
        String logx = (logX)?"plt.xscale('log')":"";
        List<String> input = Arrays.asList(
                "import numpy as np",
                "x1 = " + xx1,
                "y1 = " + yy1,
                "x2 = " + xx2,
                "y2 = " + yy2,
                "x3 = " + xx3,
                "y3 = " + yy3,
                logy,
                logx,
                "plt.plot(x3,y3,'+',c='green',label='Popular and Viral')",
                "plt.plot(x2,y2,'+',c='blue',label='Viral')",
                "plt.plot(x1,y1,'+',c='red',label='Popular')",
                "plt.title('" + titleBar + "')",
                "plt.xlabel('" + xlabel + "')",
                "plt.ylabel('" + ylabel + "')",
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

    private String producePointsGraph2(String experimentId, String fileName, String titleBar, String xlabel, String ylabel,List<Double>x1,List<Double>y1,List<Double> x2,List<Double>y2,boolean logY) {
        String xx1 = x1.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));
        String yy1 = y1.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));
        String xx2 = x2.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));
        String yy2 = y2.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));

        String log = (logY)?"plt.yscale('log')":"";
        List<String> input = Arrays.asList(
                "import numpy as np",
                "x1 = " + xx1,
                "y1 = " + yy1,
                "x2 = " + xx2,
                "y2 = " + yy2,
                log,
                "plt.plot(x2,y2,'o',c='blue',label='Viral')",
                "plt.plot(x1,y1,'+',c='red',label='Popular')",
                "plt.title('" + titleBar + "')",
                "plt.xlabel('" + xlabel + "')",
                "plt.ylabel('" + ylabel + "')",
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

    private String producePointsGraph(String experimentId, String fileName, String titleBar, String xlabel, String ylabel,List<Double>x,List<Double>y,boolean logY) {
        String xx = x.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));
        String yy = y.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));

        String log = (logY)?"plt.yscale('log')":"";
        List<String> input = Arrays.asList(
                "import numpy as np",
                "x = " + xx,
                "y = " + yy,
                log,
                "plt.plot(x,y,'+')",
                "plt.title('" + titleBar + "')",
                "plt.xlabel('" + xlabel + "')",
                "plt.ylabel('" + ylabel + "')"
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
    public String produceSentimentBucketsBar(List<Stat<Double>> values) {
        return produceDoubleBar("0", "sentimentBar", "Sentiment ", "x", "y", values);
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
            this.input.add("plt.figure(figsize=(12, 9))");
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


    public File getPath() {
        return path;
    }
}
