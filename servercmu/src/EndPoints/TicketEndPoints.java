package EndPoints;
import Model.Ticket;
import Model.Tour;
import Model.User;
import com.google.gson.Gson;
import utils.FreemarkerEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class TicketEndPoints{

    public static void mount(FreemarkerEngine engine){

        // Set up Person endpoints
        get("/ticket/list", (request, response) -> {
        
            List<Ticket> list = Ticket.all();
            HashMap<String, Object> model = new HashMap<>();
            model.put("list", list);
            return engine.render(model, "/ticket/list.html");
            
        });
        
        get("/ticket/get", (request, response) -> {
        
            Ticket ticket = Ticket.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("ticket", ticket);
            return engine.render(model, "/ticket/get.html");
        
        });
        
        get("/ticket/update", (request, response) -> {
        
            Ticket ticket = Ticket.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("ticket", ticket);
                        model.put("ticketUser", ticket.getUser() != null ? ticket.getUser() : new User() );
                        model.put("user", User.all());
                    model.put("ticketTour", ticket.getTour() == null? new Tour() : ticket.getTour());
                    model.put("tour", Tour.all());
            return engine.render(model, "/ticket/update.html");
        
        });
        
        post("/ticket/update", (request, response) -> {
            System.out.println(request.contentType()); // What type of data am I sending?
            System.out.println(request.queryParams()); // What are the params sent?
            System.out.println(request.raw()); // What's the raw data sent?

            Ticket ticket = Ticket.get(Integer.parseInt(request.queryParams("id")));
                ticket.setNumber(Integer.parseInt(!request.queryParams("number").isEmpty() ? request.queryParams("number").replaceAll(",","") : "0"));
                ticket.setTaken(Integer.parseInt(!request.queryParams("taken").isEmpty() ? request.queryParams("taken") : "0"));
            ticket.save();
                        if(Integer.parseInt(request.queryParams("userId")) != -1){
                        if(User.get(Integer.parseInt(request.queryParams("userId"))).getTicket() != null)
                        User.get(Integer.parseInt(request.queryParams("userId"))).setTicket(null);
                        }
                        if(ticket.getUser() != null)
                        ticket.setUser(null);
                        ticket.setUser(User.get(Integer.parseInt(request.queryParams("userId"))));
                    ticket.setTour(Tour.get(Integer.parseInt(request.queryParams("tourId"))));
            response.redirect("/ticket/list?id=" + request.queryParams("id"));

            return "";
        });
        
        get("/ticket/delete", (req, res) -> {
        
            try {
            Ticket.get(Integer.parseInt(req.queryParams("id"))).delete();
            } catch (Exception e) {

            }

            res.redirect("/ticket/list");
            return "";
        });
        
        get("/ticket/create", (req, res) -> {
            return engine.render(null, "/ticket/create.html");
        });
        
        post("/ticket/create", (req, res) -> {
            Ticket ticket = new Ticket();
                ticket.setNumber(Integer.parseInt(!req.queryParams("number").isEmpty() ? req.queryParams("number") : "0"));
                ticket.setTaken(Integer.parseInt(!req.queryParams("taken").isEmpty() ? req.queryParams("taken") : "0"));
            ticket.save();
            res.redirect("/ticket/list");
            return "";
        });
        
        get("/api/ticket", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(Ticket.all());
        });
        
        get("/api/ticket/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(Ticket.get(Integer.parseInt(req.params("id"))));
        });
        
        post("/api/ticket", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            Ticket ticket = gson.fromJson(req.body(), Ticket.class);
            ticket.save();
            return gson.toJson(ticket);
        });
        
        put("/api/ticket/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            Ticket ticket = gson.fromJson(req.body(), Ticket.class);
            ticket.setId((Integer.parseInt(req.params("id"))));
            ticket.save();
            return gson.toJson(ticket);
        });
        
        delete("/api/ticket/:id", (req, res) -> {
        
            res.type("application/json");
            Ticket ticket = Ticket.get((Integer.parseInt(req.params("id"))));
            if (ticket != null) {
            ticket.delete();
            return true;
            }

            return false;
        
        });


    }
}
