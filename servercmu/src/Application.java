
/*
 * Simple Spark web application
 *
 */

import EndPoints.*;
import Model.Ticket;
import Model.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import utils.FreemarkerEngine;

import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

import javax.xml.bind.DatatypeConverter;

import static spark.Spark.*;

public class Application {


    public static void main(String[] args) {



        // Configure Spark
        port(1234);
        staticFiles.externalLocation("src/resources/public/");

        secure("src/keystore.jks", "mestrado", null , null);

        // Configure freemarker engine
        FreemarkerEngine engine = new FreemarkerEngine("src/resources/templates");


        options("/*", (request,response)->{

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if(accessControlRequestMethod != null){
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });


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
        AuthEndPoints.mount();
        AndroidEndPoints.mount();


        try {
            System.out.println(Crypto.decrypt(DatatypeConverter.parseHexBinary("0c7e1eb614ef817c4d6b3331d3b64984072d5806370a748f2295dd570b6fd8cf84d24d140c5660c8b7887c4da3e4c14b"),"E09E1145-1863-4402-B063-55EE72B4"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
