package EndPoints;

import Model.QuizResponse;
import Model.Session;
import Model.Ticket;
import Model.User;
import com.google.gson.Gson;
import utils.FreemarkerEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class UserEndPoints {

    public static void mount(FreemarkerEngine engine) {

        // Set up Person endpoints
        get("/user/list", (request, response) -> {

            List<User> list = User.all();
            HashMap<String, Object> model = new HashMap<>();
            model.put("list", list);
            return engine.render(model, "/user/list.html");

        });

        get("/user/get", (request, response) -> {

            User user = User.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("user", user);
            return engine.render(model, "/user/get.html");

        });

        get("/user/update", (request, response) -> {

            User user = User.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("user", user);
            model.put("userSession", user.getSession() != null ? user.getSession() : new Session());
            model.put("session", Session.all());
            model.put("userTicket", user.getTicket() != null ? user.getTicket() : new Ticket());
            model.put("ticket", Ticket.all());
            model.put("userQuizResponse", user.getQuizResponses());
            model.put("quizresponse", QuizResponse.all());
            return engine.render(model, "/user/update.html");

        });

        post("/user/update", (request, response) -> {
            System.out.println(request.contentType()); // What type of data am I sending?
            System.out.println(request.queryParams()); // What are the params sent?
            System.out.println(request.raw()); // What's the raw data sent?

            User user = User.get(Integer.parseInt(request.queryParams("id")));
            user.setUsername(request.queryParams("username"));
            user.save();
            if (Integer.parseInt(request.queryParams("sessionId")) != -1) {
                if (Session.get(Integer.parseInt(request.queryParams("sessionId"))).getUser() != null)
                    Session.get(Integer.parseInt(request.queryParams("sessionId"))).setUser(null);
            }
            if (user.getSession() != null)
                user.setSession(null);

            user.setSession(Session.get(Integer.parseInt(request.queryParams("sessionId"))));
            if (Integer.parseInt(request.queryParams("ticketId")) != -1) {
                if (Ticket.get(Integer.parseInt(request.queryParams("ticketId"))).getUser() != null)
                    Ticket.get(Integer.parseInt(request.queryParams("ticketId"))).setUser(null);
            }
            if (user.getTicket() != null)
                user.setTicket(null);

            user.setTicket(Ticket.get(Integer.parseInt(request.queryParams("ticketId"))));
            response.redirect("/user/list?id=" + request.queryParams("id"));

            return "";
        });

        get("/user/delete", (req, res) -> {

            try {
                User.get(Integer.parseInt(req.queryParams("id"))).delete();
            } catch (Exception e) {

            }

            res.redirect("/user/list");
            return "";
        });

        get("/user/create", (req, res) -> {
            return engine.render(null, "/user/create.html");
        });

        post("/user/create", (req, res) -> {
            User user = new User();
            user.setUsername(req.queryParams("username"));
            user.save();
            res.redirect("/user/list");
            return "";
        });

        post("/user/addQuizResponse", (request, response) -> {

            QuizResponse.get(Integer.parseInt(request.queryParams("add"))).setUser(User.get(Integer.parseInt(request.queryParams("id"))));

            response.redirect("/user/update?id=" + request.queryParams("id"));
            return "";
        });

        get("/user/removeQuizResponse", (request, response) -> {
            QuizResponse.get(Integer.parseInt(request.queryParams("rmv"))).setUser(null);
            response.redirect("/user/update?id=" + request.queryParams("id"));
            return "";
        });
        get("/api/user", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(User.all());
        });

        get("/api/user/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(User.get(Integer.parseInt(req.params("id"))));
        });

        post("/api/user", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            User user = gson.fromJson(req.body(), User.class);
            user.save();
            return gson.toJson(user);
        });

        put("/api/user/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            User user = gson.fromJson(req.body(), User.class);
            user.setId((Integer.parseInt(req.params("id"))));
            user.save();
            return gson.toJson(user);
        });

        delete("/api/user/:id", (req, res) -> {

            res.type("application/json");
            User user = User.get((Integer.parseInt(req.params("id"))));
            if (user != null) {
                user.delete();
                return true;
            }

            return false;

        });


    }
}
