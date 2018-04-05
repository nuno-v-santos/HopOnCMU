package EndPoints;

import Model.Answer;
import Model.Question;
import com.google.gson.Gson;
import utils.FreemarkerEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class AnswerEndPoints{

    public static void mount(FreemarkerEngine engine){

        // Set up Person endpoints
        get("/answer/list", (request, response) -> {
        
            List<Answer> list = Answer.all();
            HashMap<String, Object> model = new HashMap<>();
            model.put("list", list);
            return engine.render(model, "/answer/list.html");
            
        });
        
        get("/answer/get", (request, response) -> {
        
            Answer answer = Answer.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("answer", answer);
            return engine.render(model, "/answer/get.html");
        
        });
        
        get("/answer/update", (request, response) -> {
        
            Answer answer = Answer.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("answer", answer);
                    model.put("answerQuestion", answer.getQuestion() == null? new Question() : answer.getQuestion());
                    model.put("question", Question.all());
            return engine.render(model, "/answer/update.html");
        
        });
        
        post("/answer/update", (request, response) -> {
            System.out.println(request.contentType()); // What type of data am I sending?
            System.out.println(request.queryParams()); // What are the params sent?
            System.out.println(request.raw()); // What's the raw data sent?

            Answer answer = Answer.get(Integer.parseInt(request.queryParams("id")));
                answer.setAnswer(request.queryParams("answer"));
                answer.setCorrect(Integer.parseInt(!request.queryParams("correct").isEmpty() ? request.queryParams("correct") : "0"));
            answer.save();
                    answer.setQuestion(Question.get(Integer.parseInt(request.queryParams("questionId"))));
            response.redirect("/answer/list?id=" + request.queryParams("id"));

            return "";
        });
        
        get("/answer/delete", (req, res) -> {
        
            try {
            Answer.get(Integer.parseInt(req.queryParams("id"))).delete();
            } catch (Exception e) {

            }

            res.redirect("/answer/list");
            return "";
        });
        
        get("/answer/create", (req, res) -> {
            return engine.render(null, "/answer/create.html");
        });
        
        post("/answer/create", (req, res) -> {
            Answer answer = new Answer();
                answer.setAnswer(req.queryParams("answer"));
                answer.setCorrect(Integer.parseInt(!req.queryParams("correct").isEmpty() ? req.queryParams("correct") : "0"));
            answer.save();
            res.redirect("/answer/list");
            return "";
        });
        
        get("/api/answer", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(Answer.all());
        });
        
        get("/api/answer/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(Answer.get(Integer.parseInt(req.params("id"))));
        });
        
        post("/api/answer", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            Answer answer = gson.fromJson(req.body(), Answer.class);
            answer.save();
            return gson.toJson(answer);
        });
        
        put("/api/answer/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            Answer answer = gson.fromJson(req.body(), Answer.class);
            answer.setId((Integer.parseInt(req.params("id"))));
            answer.save();
            return gson.toJson(answer);
        });
        
        delete("/api/answer/:id", (req, res) -> {
        
            res.type("application/json");
            Answer answer = Answer.get((Integer.parseInt(req.params("id"))));
            if (answer != null) {
            answer.delete();
            return true;
            }

            return false;
        
        });


    }
}
