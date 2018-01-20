package com.zgeorg03.services;

import com.zgeorg03.CsvProducer;
import com.zgeorg03.PlotProducer;

/**
 * Created by zgeorg03 on 3/11/17.
 */
public class PlotsService{
    private final PlotProducer plotProducer;
    private final CsvProducer csvProducer;

    public PlotsService(PlotProducer plotProducer, CsvProducer csvProducer) {
        this.plotProducer = plotProducer;
        this.csvProducer = csvProducer;
    }

    public byte[]readPlot(String experimentId,String plotName){
        return plotProducer.readPlot(experimentId,plotName);
    }


    public byte[]readExperimentCsv(String id,String title){
        return csvProducer.readExperimentCsv(id,title);
    }

    public byte[] readClassificationPlot(String experiment, String plotName) {

        return plotProducer.readClassificationPlot(experiment,plotName);
    }
}
