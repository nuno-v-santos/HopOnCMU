package EndPoints;
import Model.Monument;
import Model.Question;
import Model.Quiz;
import Model.QuizResponse;
import com.google.gson.Gson;
import utils.FreemarkerEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class QuizEndPoints{

    public static void mount(FreemarkerEngine engine){

        // Set up Person endpoints
        get("/quiz/list", (request, response) -> {
        
            List<Quiz> list = Quiz.all();
            HashMap<String, Object> model = new HashMap<>();
            model.put("list", list);
            return engine.render(model, "/quiz/list.html");
            
        });
        
        get("/quiz/get", (request, response) -> {
        
            Quiz quiz = Quiz.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("quiz", quiz);
            return engine.render(model, "/quiz/get.html");
        
        });
        
        get("/quiz/update", (request, response) -> {
        
            Quiz quiz = Quiz.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("quiz", quiz);
                        model.put("quizMonument", quiz.getMonument() != null ? quiz.getMonument() : new Monument() );
                        model.put("monument", Monument.all());
                    model.put("quizQuestion", quiz.getQuestions());
                    model.put("question", Question.all());
                    model.put("quizQuizResponse", quiz.getQuizResponses());
                    model.put("quizresponse", QuizResponse.all());
            return engine.render(model, "/quiz/update.html");
        
        });
        
        post("/quiz/update", (request, response) -> {
            System.out.println(request.contentType()); // What type of data am I sending?
            System.out.println(request.queryParams()); // What are the params sent?
            System.out.println(request.raw()); // What's the raw data sent?

            Quiz quiz = Quiz.get(Integer.parseInt(request.queryParams("id")));
                quiz.setName(request.queryParams("name"));
            quiz.save();
                        if(Integer.parseInt(request.queryParams("monumentId")) != -1){
                        if(Monument.get(Integer.parseInt(request.queryParams("monumentId"))).getQuiz() != null)
                        Monument.get(Integer.parseInt(request.queryParams("monumentId"))).setQuiz(null);
                        }
                        if(quiz.getMonument() != null)
                        quiz.setMonument(null);
                        quiz.setMonument(Monument.get(Integer.parseInt(request.queryParams("monumentId"))));
            response.redirect("/quiz/list?id=" + request.queryParams("id"));

            return "";
        });
        
        get("/quiz/delete", (req, res) -> {
        
            try {
            Quiz.get(Integer.parseInt(req.queryParams("id"))).delete();
            } catch (Exception e) {

            }

            res.redirect("/quiz/list");
            return "";
        });
        
        get("/quiz/create", (req, res) -> {
            return engine.render(null, "/quiz/create.html");
        });
        
        post("/quiz/create", (req, res) -> {
            Quiz quiz = new Quiz();
                quiz.setName(req.queryParams("name"));
            quiz.save();
            res.redirect("/quiz/list");
            return "";
        });
        
        post("/quiz/addQuestion", (request, response) -> {

            Question.get(Integer.parseInt(request.queryParams("add"))).setQuiz(Quiz.get(Integer.parseInt(request.queryParams("id"))));

            response.redirect("/quiz/update?id=" + request.queryParams("id"));
            return "";
        });

        get("/quiz/removeQuestion", (request, response) -> {
            Question.get(Integer.parseInt(request.queryParams("rmv"))).setQuiz(null);
            response.redirect("/quiz/update?id=" + request.queryParams("id"));
            return "";
        });
        post("/quiz/addQuizResponse", (request, response) -> {

            QuizResponse.get(Integer.parseInt(request.queryParams("add"))).setQuiz(Quiz.get(Integer.parseInt(request.queryParams("id"))));

            response.redirect("/quiz/update?id=" + request.queryParams("id"));
            return "";
        });

        get("/quiz/removeQuizResponse", (request, response) -> {
            QuizResponse.get(Integer.parseInt(request.queryParams("rmv"))).setQuiz(null);
            response.redirect("/quiz/update?id=" + request.queryParams("id"));
            return "";
        });
        get("/api/quiz", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(Quiz.all());
        });
        
        get("/api/quiz/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(Quiz.get(Integer.parseInt(req.params("id"))));
        });
        
        post("/api/quiz", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            Quiz quiz = gson.fromJson(req.body(), Quiz.class);
            quiz.save();
            return gson.toJson(quiz);
        });
        
        put("/api/quiz/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            Quiz quiz = gson.fromJson(req.body(), Quiz.class);
            quiz.setId((Integer.parseInt(req.params("id"))));
            quiz.save();
            return gson.toJson(quiz);
        });
        
        delete("/api/quiz/:id", (req, res) -> {
        
            res.type("application/json");
            Quiz quiz = Quiz.get((Integer.parseInt(req.params("id"))));
            if (quiz != null) {
            quiz.delete();
            return true;
            }

            return false;
        
        });


    }
}
