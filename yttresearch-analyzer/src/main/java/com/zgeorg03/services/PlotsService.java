package com.zgeorg03.services;

import com.zgeorg03.core.PlotProducer;

/**
 * Created by zgeorg03 on 3/11/17.
 */
public class PlotsService{
    private final PlotProducer plotProducer;

    public PlotsService(PlotProducer plotProducer) {
        this.plotProducer = plotProducer;
    }

    public byte[]readPlot(String experimentId,String plotName){
        return plotProducer.readPlot(experimentId,plotName);
    }
}
