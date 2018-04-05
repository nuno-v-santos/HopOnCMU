
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

        post("/register", ((request, response) -> {

            //todo : send json responses
            try {
                JsonElement body = new JsonParser().parse(request.body());
                String ticket = body.getAsJsonObject().get("ticket").getAsString();
                String username = body.getAsJsonObject().get("username").getAsString();

                if (username.length() < 3) {
                    return "short name";
                }

                Ticket ticketObject = Ticket.where("number = " + ticket).get(0);

                //username already taken
                if (User.where("username= '" + username + "'").size() > 0) {
                    return "username taken";
                }

                //ticket não existe
                if (ticketObject == null) {
                    return "no such ticket";
                }

                //ticked já foi obtido
                if (ticketObject.getTaken() == 1) {
                    return "ticket taken";
                }


                User newUser = new User();
                newUser.setUsername(username);
                newUser.save();
                ticketObject.setTaken(1);
                ticketObject.save();
                newUser.setTicket(ticketObject);
                //todo generate token session
                //todo: read the random number
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }));

    }
}
