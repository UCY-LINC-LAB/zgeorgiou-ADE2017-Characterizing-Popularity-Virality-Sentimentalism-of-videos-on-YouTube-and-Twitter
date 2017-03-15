package com.zgeorg03.controllers;

import com.zgeorg03.controllers.helpers.GetRequest;
import com.zgeorg03.controllers.helpers.JsonResult;
import com.zgeorg03.controllers.helpers.Parameter;
import com.zgeorg03.controllers.helpers.ParseParameters;
import com.zgeorg03.services.PlotsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by zgeorg03 on 3/11/17.
 */
public class PlotsController {
    private  static final Logger logger = LoggerFactory.getLogger(PlotsController.class);

    public PlotsController(PlotsService plotsService) {

        new GetRequest("/plots/:experiment/:plotName") {

            @Parameter(description = "Experiment Id", required = true)
            String experiment;

            @Parameter(description = "Plot name", required = true)
            String plotName;

            @Override
            public Object execute(Request request, Response response, JsonResult result) {

                byte[] data = plotsService.readPlot(experiment, plotName);
                if (data == null) {
                    result.addError("Plot not found!");
                    return result.build();
                }

                HttpServletResponse raw = response.raw();
                response.header("Content-Disposition", "attachment; filename=" + plotName + ".png");
                ///response.type("application/force-download");
                response.type("image/png");
                try {
                    raw.getOutputStream().write(data);
                    raw.getOutputStream().flush();
                    raw.getOutputStream().close();
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                }
                return raw;
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {
                experiment = ParseParameters.parseStringParam(request, result, ":experiment", "", x -> true, "Error with experiment");
                plotName = ParseParameters.parseStringParam(request, result, ":plotName", "", x -> true, "Error with plotName");

            }
        };

        new GetRequest("/csv/:id"){

            @Parameter(description = "Csv Id", required = true)
            String id;


            @Override
            public Object execute(Request request, Response response, JsonResult result) {

                byte[] data = plotsService.readCsv(id);
                if (data == null) {
                    result.addError("Csv not found!");
                    return result.build();
                }

                HttpServletResponse raw = response.raw();
                response.header("Content-Disposition", "attachment; filename=" + id + ".csv");
                ///response.type("application/force-download");
                response.type("text/csv");
                try {
                    raw.getOutputStream().write(data);
                    raw.getOutputStream().flush();
                    raw.getOutputStream().close();
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                }
                return raw;
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {
                id = ParseParameters.parseStringParam(request, result, ":id", "", x -> true, "Error with id");

            }
        };

        new GetRequest("/:experiment/videos_features"){

            @Parameter(description = "Experiment", required = true)
            String experiment;


            @Override
            public Object execute(Request request, Response response, JsonResult result) {

                byte[] data = plotsService.readExperimentCsv(experiment,"videos_features");
                if (data == null) {
                    result.addError("Csv not found!");
                    return result.build();
                }

                HttpServletResponse raw = response.raw();
                response.header("Content-Disposition", "attachment; filename=" + "videos_features.csv");
                ///response.type("application/force-download");
                response.type("text/csv");
                try {
                    raw.getOutputStream().write(data);
                    raw.getOutputStream().flush();
                    raw.getOutputStream().close();
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                }
                return raw;
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {
                experiment = ParseParameters.parseStringParam(request, result, ":experiment", "", x -> true, "Error with experiment");

            }
        };
    }
}
