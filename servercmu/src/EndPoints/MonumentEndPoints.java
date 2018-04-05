package EndPoints;

import Model.Monument;
import Model.Quiz;
import Model.Tour;
import com.google.gson.Gson;
import utils.FreemarkerEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class MonumentEndPoints{

    public static void mount(FreemarkerEngine engine){

        // Set up Person endpoints
        get("/monument/list", (request, response) -> {
        
            List<Monument> list = Monument.all();
            HashMap<String, Object> model = new HashMap<>();
            model.put("list", list);
            return engine.render(model, "/monument/list.html");
            
        });
        
        get("/monument/get", (request, response) -> {
        
            Monument monument = Monument.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("monument", monument);
            return engine.render(model, "/monument/get.html");
        
        });
        
        get("/monument/update", (request, response) -> {
        
            Monument monument = Monument.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("monument", monument);
                        model.put("monumentTour", monument.getTours() == null? new Tour() : monument.getTours());
                        model.put("tour", Tour.all());
                        model.put("monumentQuiz", monument.getQuiz() != null ? monument.getQuiz() : new Quiz());
                        model.put("quiz", Quiz.all());
            return engine.render(model, "/monument/update.html");
        
        });
        
        post("/monument/update", (request, response) -> {
            System.out.println(request.contentType()); // What type of data am I sending?
            System.out.println(request.queryParams()); // What are the params sent?
            System.out.println(request.raw()); // What's the raw data sent?

            Monument monument = Monument.get(Integer.parseInt(request.queryParams("id")));
                monument.setName(request.queryParams("name"));
                monument.setWifiId(request.queryParams("wifiId"));
                monument.setImageURL(request.queryParams("imageURL"));
            monument.save();
                        if(Integer.parseInt(request.queryParams("quizId")) != -1){
                        if(Quiz.get(Integer.parseInt(request.queryParams("quizId"))).getMonument() != null)
                        Quiz.get(Integer.parseInt(request.queryParams("quizId"))).setMonument(null);
                        }
                        if(monument.getQuiz() != null)
                        monument.setQuiz(null);

                        monument.setQuiz(Quiz.get(Integer.parseInt(request.queryParams("quizId"))));
            response.redirect("/monument/list?id=" + request.queryParams("id"));

            return "";
        });
        
        get("/monument/delete", (req, res) -> {
        
            try {
            Monument.get(Integer.parseInt(req.queryParams("id"))).delete();
            } catch (Exception e) {

            }

            res.redirect("/monument/list");
            return "";
        });
        
        get("/monument/create", (req, res) -> {
            return engine.render(null, "/monument/create.html");
        });
        
        post("/monument/create", (req, res) -> {
            Monument monument = new Monument();
                monument.setName(req.queryParams("name"));
                monument.setWifiId(req.queryParams("wifiId"));
                monument.setImageURL(req.queryParams("imageURL"));
            monument.save();
            res.redirect("/monument/list");
            return "";
        });
        
        post("/monument/addTour", (request, response) -> {

            Tour.get(Integer.parseInt(request.queryParams("add"))).addMonument(Monument.get(Integer.parseInt(request.queryParams("id"))));

            response.redirect("/monument/update?id=" + request.queryParams("id"));
            return "";
        });

        get("/monument/removeTour", (request, response) -> {
            Monument.get(Integer.parseInt(request.queryParams("id"))).removeTour(Tour.get(Integer.parseInt(request.queryParams("rmv"))));
            response.redirect("/monument/update?id=" + request.queryParams("id"));
            return "";
        });
        get("/api/monument", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(Monument.all());
        });
        
        get("/api/monument/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(Monument.get(Integer.parseInt(req.params("id"))));
        });
        
        post("/api/monument", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            Monument monument = gson.fromJson(req.body(), Monument.class);
            monument.save();
            return gson.toJson(monument);
        });
        
        put("/api/monument/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            Monument monument = gson.fromJson(req.body(), Monument.class);
            monument.setId((Integer.parseInt(req.params("id"))));
            monument.save();
            return gson.toJson(monument);
        });
        
        delete("/api/monument/:id", (req, res) -> {
        
            res.type("application/json");
            Monument monument = Monument.get((Integer.parseInt(req.params("id"))));
            if (monument != null) {
            monument.delete();
            return true;
            }

            return false;
        
        });


    }
}
