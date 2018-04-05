package EndPoints;

import Model.Quiz;
import Model.QuizResponse;
import Model.User;
import com.google.gson.Gson;
import utils.FreemarkerEngine;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class QuizresponseEndPoints {

    public static void mount(FreemarkerEngine engine) {

        // Set up Person endpoints
        get("/quizresponse/list", (request, response) -> {

            List<QuizResponse> list = QuizResponse.all();
            HashMap<String, Object> model = new HashMap<>();
            model.put("list", list);
            return engine.render(model, "/quizresponse/list.html");

        });

        get("/quizresponse/get", (request, response) -> {

            QuizResponse quizresponse = QuizResponse.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("quizresponse", quizresponse);
            return engine.render(model, "/quizresponse/get.html");

        });

        get("/quizresponse/update", (request, response) -> {

            QuizResponse quizresponse = QuizResponse.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("quizresponse", quizresponse);
            model.put("quizresponseQuiz", quizresponse.getQuiz() == null ? new Quiz() : quizresponse.getQuiz());
            model.put("quiz", Quiz.all());
            model.put("quizresponseUser", quizresponse.getUser() == null ? new User() : quizresponse.getUser());
            model.put("user", User.all());
            return engine.render(model, "/quizresponse/update.html");

        });

        post("/quizresponse/update", (request, response) -> {
            System.out.println(request.contentType()); // What type of data am I sending?
            System.out.println(request.queryParams()); // What are the params sent?
            System.out.println(request.raw()); // What's the raw data sent?

            QuizResponse quizresponse = QuizResponse.get(Integer.parseInt(request.queryParams("id")));
            quizresponse.setScore(Integer.parseInt(!request.queryParams("score").isEmpty() ? request.queryParams("score") : "0"));
            quizresponse.setDate(new Date(request.queryParams("date")));
            quizresponse.save();
            quizresponse.setQuiz(Quiz.get(Integer.parseInt(request.queryParams("quizId"))));
            quizresponse.setUser(User.get(Integer.parseInt(request.queryParams("userId"))));
            response.redirect("/quizresponse/list?id=" + request.queryParams("id"));

            return "";
        });

        get("/quizresponse/delete", (req, res) -> {

            try {
                QuizResponse.get(Integer.parseInt(req.queryParams("id"))).delete();
            } catch (Exception e) {

            }

            res.redirect("/quizresponse/list");
            return "";
        });

        get("/quizresponse/create", (req, res) -> {
            return engine.render(null, "/quizresponse/create.html");
        });

        post("/quizresponse/create", (req, res) -> {
            QuizResponse quizresponse = new QuizResponse();
            quizresponse.setScore(Integer.parseInt(!req.queryParams("score").isEmpty() ? req.queryParams("score") : "0"));
            quizresponse.setDate(new Date(req.queryParams("date")));
            quizresponse.save();
            res.redirect("/quizresponse/list");
            return "";
        });

        get("/api/quizresponse", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(QuizResponse.all());
        });

        get("/api/quizresponse/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(QuizResponse.get(Integer.parseInt(req.params("id"))));
        });

        post("/api/quizresponse", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            QuizResponse quizresponse = gson.fromJson(req.body(), QuizResponse.class);
            quizresponse.save();
            return gson.toJson(quizresponse);
        });

        put("/api/quizresponse/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            QuizResponse quizresponse = gson.fromJson(req.body(), QuizResponse.class);
            quizresponse.setId((Integer.parseInt(req.params("id"))));
            quizresponse.save();
            return gson.toJson(quizresponse);
        });

        delete("/api/quizresponse/:id", (req, res) -> {

            res.type("application/json");
            QuizResponse quizresponse = QuizResponse.get((Integer.parseInt(req.params("id"))));
            if (quizresponse != null) {
                quizresponse.delete();
                return true;
            }

            return false;

        });


    }
}
