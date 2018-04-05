package EndPoints;
import Model.Answer;
import Model.Question;
import Model.Quiz;
import com.google.gson.Gson;
import utils.FreemarkerEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class QuestionEndPoints{

    public static void mount(FreemarkerEngine engine){

        // Set up Person endpoints
        get("/question/list", (request, response) -> {
        
            List<Question> list = Question.all();
            HashMap<String, Object> model = new HashMap<>();
            model.put("list", list);
            return engine.render(model, "/question/list.html");
            
        });
        
        get("/question/get", (request, response) -> {
        
            Question question = Question.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("question", question);
            return engine.render(model, "/question/get.html");
        
        });
        
        get("/question/update", (request, response) -> {
        
            Question question = Question.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("question", question);
                    model.put("questionQuiz", question.getQuiz() == null? new Quiz() : question.getQuiz());
                    model.put("quiz", Quiz.all());
                    model.put("questionAnswer", question.getAnswers());
                    model.put("answer", Answer.all());
            return engine.render(model, "/question/update.html");
        
        });
        
        post("/question/update", (request, response) -> {
            System.out.println(request.contentType()); // What type of data am I sending?
            System.out.println(request.queryParams()); // What are the params sent?
            System.out.println(request.raw()); // What's the raw data sent?

            Question question = Question.get(Integer.parseInt(request.queryParams("id")));
                question.setQuestion(request.queryParams("question"));
            question.save();
                    question.setQuiz(Quiz.get(Integer.parseInt(request.queryParams("quizId"))));
            response.redirect("/question/list?id=" + request.queryParams("id"));

            return "";
        });
        
        get("/question/delete", (req, res) -> {
        
            try {
            Question.get(Integer.parseInt(req.queryParams("id"))).delete();
            } catch (Exception e) {

            }

            res.redirect("/question/list");
            return "";
        });
        
        get("/question/create", (req, res) -> {
            return engine.render(null, "/question/create.html");
        });
        
        post("/question/create", (req, res) -> {
            Question question = new Question();
                question.setQuestion(req.queryParams("question"));
            question.save();
            res.redirect("/question/list");
            return "";
        });
        
        post("/question/addAnswer", (request, response) -> {

            Answer.get(Integer.parseInt(request.queryParams("add"))).setQuestion(Question.get(Integer.parseInt(request.queryParams("id"))));

            response.redirect("/question/update?id=" + request.queryParams("id"));
            return "";
        });

        get("/question/removeAnswer", (request, response) -> {
            Answer.get(Integer.parseInt(request.queryParams("rmv"))).setQuestion(null);
            response.redirect("/question/update?id=" + request.queryParams("id"));
            return "";
        });
        get("/api/question", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(Question.all());
        });
        
        get("/api/question/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(Question.get(Integer.parseInt(req.params("id"))));
        });
        
        post("/api/question", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            Question question = gson.fromJson(req.body(), Question.class);
            question.save();
            return gson.toJson(question);
        });
        
        put("/api/question/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            Question question = gson.fromJson(req.body(), Question.class);
            question.setId((Integer.parseInt(req.params("id"))));
            question.save();
            return gson.toJson(question);
        });
        
        delete("/api/question/:id", (req, res) -> {
        
            res.type("application/json");
            Question question = Question.get((Integer.parseInt(req.params("id"))));
            if (question != null) {
            question.delete();
            return true;
            }

            return false;
        
        });


    }
}
