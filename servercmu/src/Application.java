
/*
 * Simple Spark web application
 *
 */

import EndPoints.*;
import utils.FreemarkerEngine;

import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

import static spark.Spark.*;

public class Application {

    public static void main(String[] args) {

        // Configure Spark
        port(1234);
        staticFiles.externalLocation("src/resources/public/");

        // Configure freemarker engine
        FreemarkerEngine engine = new FreemarkerEngine("src/resources/templates");

        // Set up endpoints
        get("/", (request, response) -> {
            return engine.render(null, "index.html");
        });
        TicketEndPoints.mount(engine);
        UserEndPoints.mount(engine);
        TourEndPoints.mount(engine);
        SessionEndPoints.mount(engine);
        MonumentEndPoints.mount(engine);
        QuizEndPoints.mount(engine);
        QuestionEndPoints.mount(engine);
        AnswerEndPoints.mount(engine);
        QuizresponseEndPoints.mount(engine);
    }
}
