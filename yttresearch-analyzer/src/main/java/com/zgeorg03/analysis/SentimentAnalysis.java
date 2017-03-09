package com.zgeorg03.analysis;

import com.zgeorg03.analysis.models.SentimentBson;
import com.zgeorg03.analysis.models.SentimentJson;
import com.zgeorg03.analysis.models.Stat;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 3/6/17.
 */
public class SentimentAnalysis {
    private final static  Logger logger = LoggerFactory.getLogger(SentimentAnalysis.class);
    private final File sentimentFile;

    public SentimentAnalysis(String sentimentFile) throws Exception {
        this.sentimentFile = new File(sentimentFile);

        if(!this.sentimentFile.exists())
            throw new Exception("SentimentBson file doesn't exist");

        if(!this.sentimentFile.canExecute())
            throw new Exception("SentimentBson file doesn't have execute permissions");
    }

    public SentimentBson run(List<String> list) throws Exception {
        List<String> res =  new SentimentProcess(list).call();
        if(res.get(0).equals("error"))
            throw new Exception(res.stream().collect(Collectors.joining(",")));
        double average_neg = Double.parseDouble(res.get(0).split("=")[1]);
        double median_neg = Double.parseDouble(res.get(1).split("=")[1]);
        double std_neg = Double.parseDouble(res.get(2).split("=")[1]);

        double average_pos = Double.parseDouble(res.get(3).split("=")[1]);
        double median_pos = Double.parseDouble(res.get(4).split("=")[1]);
        double std_pos = Double.parseDouble(res.get(5).split("=")[1]);

        double average_neu = Double.parseDouble(res.get(6).split("=")[1]);
        double median_neu = Double.parseDouble(res.get(7).split("=")[1]);
        double std_neu = Double.parseDouble(res.get(8).split("=")[1]);

        double average_compound = Double.parseDouble(res.get(9).split("=")[1]);
        double median_compound = Double.parseDouble(res.get(10).split("=")[1]);
        double std_compound = Double.parseDouble(res.get(11).split("=")[1]);

        Stat<Double> neg = new Stat<>(average_neg,median_neg, std_neg);
        Stat<Double>  pos= new Stat<>(average_pos,median_pos, std_pos);
        Stat<Double> neu = new Stat<>(average_neu,median_neu, std_neu);
        Stat<Double> compound = new Stat<>(average_compound,median_compound, std_compound);

        return new SentimentBson(neg,pos,neu,compound);
    }

    public static SentimentJson parseSentiment(Document document) {
        Stat<Double> neg = new Stat<Double>((double) 0,(Document)document.get("neg"));
        Stat<Double> pos = new Stat<Double>((double) 0,(Document)document.get("pos"));
        Stat<Double> neu = new Stat<Double>((double) 0,(Document)document.get("neu"));
        Stat<Double> compound = new Stat<Double>((double) 0,(Document)document.get("compound"));
        return new SentimentJson(neg,pos,neu,compound);
    }
    public class SentimentProcess implements Callable<List<String>>{
        private final List<String> list;
        BufferedReader in;
        BufferedReader error;
        PrintWriter out; //Send text to the stdin of the process

        public SentimentProcess(List<String> list) {
            this.list = list;
        }

        @Override
        public List<String> call() throws Exception {
            ProcessBuilder builder = new ProcessBuilder("python",sentimentFile.getAbsolutePath());
            try {
                Process process = builder.start();
                in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                out = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));
                list.forEach(line -> out.println(line));
                out.close();
                process.waitFor();
                List<String> inputLines = readLines(in);
                List<String> errorLines = readLines(error);
                if(errorLines.size()!=0) {
                    errorLines.add(0,"error");
                    return errorLines;
                }


                return inputLines;
            } catch (IOException e) {
                logger.error(e.getMessage());
                List<String> list = new LinkedList<>();
                list.add(0,"error");
                return list;
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                List<String> list = new LinkedList<>();
                list.add(0,"error");
                return list;
            }

        }
    }

    private List<String> readLines(BufferedReader reader) throws IOException {
        List<String> lines = new LinkedList<>();
        String input ="";
        while((input=reader.readLine())!=null){
            lines.add(input);
        }

        return lines;
    }
}
