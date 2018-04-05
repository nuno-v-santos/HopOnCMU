package EndPoints;
import Model.Session;
import Model.User;
import com.google.gson.Gson;
import utils.FreemarkerEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class SessionEndPoints{

    public static void mount(FreemarkerEngine engine){

        // Set up Person endpoints
        get("/session/list", (request, response) -> {
        
            List<Session> list = Session.all();
            HashMap<String, Object> model = new HashMap<>();
            model.put("list", list);
            return engine.render(model, "/session/list.html");
            
        });
        
        get("/session/get", (request, response) -> {
        
            Session session = Session.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("session", session);
            return engine.render(model, "/session/get.html");
        
        });
        
        get("/session/update", (request, response) -> {
        
            Session session = Session.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("session", session);
                        model.put("sessionUser", session.getUser() != null ? session.getUser() : new User() );
                        model.put("user", User.all());
            return engine.render(model, "/session/update.html");
        
        });
        
        post("/session/update", (request, response) -> {
            System.out.println(request.contentType()); // What type of data am I sending?
            System.out.println(request.queryParams()); // What are the params sent?
            System.out.println(request.raw()); // What's the raw data sent?

            Session session = Session.get(Integer.parseInt(request.queryParams("id")));
                session.setToken(request.queryParams("token"));
                session.setRandom(Integer.parseInt(!request.queryParams("random").isEmpty() ? request.queryParams("random") : "0"));
                session.setSharedToken(request.queryParams("sharedToken"));
            session.save();
                        if(Integer.parseInt(request.queryParams("userId")) != -1){
                        if(User.get(Integer.parseInt(request.queryParams("userId"))).getSession() != null)
                        User.get(Integer.parseInt(request.queryParams("userId"))).setSession(null);
                        }
                        if(session.getUser() != null)
                        session.setUser(null);
                        session.setUser(User.get(Integer.parseInt(request.queryParams("userId"))));
            response.redirect("/session/list?id=" + request.queryParams("id"));

            return "";
        });
        
        get("/session/delete", (req, res) -> {
        
            try {
            Session.get(Integer.parseInt(req.queryParams("id"))).delete();
            } catch (Exception e) {

            }

            res.redirect("/session/list");
            return "";
        });
        
        get("/session/create", (req, res) -> {
            return engine.render(null, "/session/create.html");
        });
        
        post("/session/create", (req, res) -> {
            Session session = new Session();
                session.setToken(req.queryParams("token"));
                session.setRandom(Integer.parseInt(!req.queryParams("random").isEmpty() ? req.queryParams("random") : "0"));
                session.setSharedToken(req.queryParams("sharedToken"));
            session.save();
            res.redirect("/session/list");
            return "";
        });
        
        get("/api/session", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(Session.all());
        });
        
        get("/api/session/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(Session.get(Integer.parseInt(req.params("id"))));
        });
        
        post("/api/session", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            Session session = gson.fromJson(req.body(), Session.class);
            session.save();
            return gson.toJson(session);
        });
        
        put("/api/session/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            Session session = gson.fromJson(req.body(), Session.class);
            session.setId((Integer.parseInt(req.params("id"))));
            session.save();
            return gson.toJson(session);
        });
        
        delete("/api/session/:id", (req, res) -> {
        
            res.type("application/json");
            Session session = Session.get((Integer.parseInt(req.params("id"))));
            if (session != null) {
            session.delete();
            return true;
            }

            return false;
        
        });


    }
}
