package EndPoints;
import Model.Monument;
import Model.Ticket;
import Model.Tour;
import com.google.gson.Gson;
import utils.FreemarkerEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class TourEndPoints{

    public static void mount(FreemarkerEngine engine){

        // Set up Person endpoints
        get("/tour/list", (request, response) -> {
        
            List<Tour> list = Tour.all();
            HashMap<String, Object> model = new HashMap<>();
            model.put("list", list);
            return engine.render(model, "/tour/list.html");
            
        });
        
        get("/tour/get", (request, response) -> {
        
            Tour tour = Tour.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("tour", tour);
            return engine.render(model, "/tour/get.html");
        
        });
        
        get("/tour/update", (request, response) -> {
        
            Tour tour = Tour.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("tour", tour);
                    model.put("tourTicket", tour.getTickets());
                    model.put("ticket", Ticket.all());
                        model.put("tourMonument", tour.getMonuments() == null? new Monument() : tour.getMonuments());
                        model.put("monument", Monument.all());
            return engine.render(model, "/tour/update.html");
        
        });
        
        post("/tour/update", (request, response) -> {
            System.out.println(request.contentType()); // What type of data am I sending?
            System.out.println(request.queryParams()); // What are the params sent?
            System.out.println(request.raw()); // What's the raw data sent?

            Tour tour = Tour.get(Integer.parseInt(request.queryParams("id")));
                tour.setName(request.queryParams("name"));
            tour.save();
            response.redirect("/tour/list?id=" + request.queryParams("id"));

            return "";
        });
        
        get("/tour/delete", (req, res) -> {
        
            try {
            Tour.get(Integer.parseInt(req.queryParams("id"))).delete();
            } catch (Exception e) {

            }

            res.redirect("/tour/list");
            return "";
        });
        
        get("/tour/create", (req, res) -> {
            return engine.render(null, "/tour/create.html");
        });
        
        post("/tour/create", (req, res) -> {
            Tour tour = new Tour();
                tour.setName(req.queryParams("name"));
            tour.save();
            res.redirect("/tour/list");
            return "";
        });
        
        post("/tour/addTicket", (request, response) -> {

            Ticket.get(Integer.parseInt(request.queryParams("add"))).setTour(Tour.get(Integer.parseInt(request.queryParams("id"))));

            response.redirect("/tour/update?id=" + request.queryParams("id"));
            return "";
        });

        get("/tour/removeTicket", (request, response) -> {
            Ticket.get(Integer.parseInt(request.queryParams("rmv"))).setTour(null);
            response.redirect("/tour/update?id=" + request.queryParams("id"));
            return "";
        });
        post("/tour/addMonument", (request, response) -> {

            Monument.get(Integer.parseInt(request.queryParams("add"))).addTour(Tour.get(Integer.parseInt(request.queryParams("id"))));

            response.redirect("/tour/update?id=" + request.queryParams("id"));
            return "";
        });

        get("/tour/removeMonument", (request, response) -> {
            Tour.get(Integer.parseInt(request.queryParams("id"))).removeMonument(Monument.get(Integer.parseInt(request.queryParams("rmv"))));
            response.redirect("/tour/update?id=" + request.queryParams("id"));
            return "";
        });
        get("/api/tour", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(Tour.all());
        });
        
        get("/api/tour/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(Tour.get(Integer.parseInt(req.params("id"))));
        });
        
        post("/api/tour", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            Tour tour = gson.fromJson(req.body(), Tour.class);
            tour.save();
            return gson.toJson(tour);
        });
        
        put("/api/tour/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            Tour tour = gson.fromJson(req.body(), Tour.class);
            tour.setId((Integer.parseInt(req.params("id"))));
            tour.save();
            return gson.toJson(tour);
        });
        
        delete("/api/tour/:id", (req, res) -> {
        
            res.type("application/json");
            Tour tour = Tour.get((Integer.parseInt(req.params("id"))));
            if (tour != null) {
            tour.delete();
            return true;
            }

            return false;
        
        });


    }
}
