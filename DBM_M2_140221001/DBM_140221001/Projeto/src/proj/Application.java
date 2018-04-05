package proj;
/*
* Simple Spark web application
*
*/

import proj.person.Person;
import proj.person.Super;
import utils.FreemarkerEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class Application {

    public static void main(String[] args) {

        // Configure Spark
        port(8000);
        staticFiles.externalLocation("src/resources");

        // Configure freemarker engine
        FreemarkerEngine engine = new FreemarkerEngine("src/proj/resources/templates");

        // Set up endpoints
        get("/", (request, response) -> {
        return engine.render(null, "index.html");
        });
        // Set up Person endpoints
        get("/person/list", (request, response) -> {

            List<Super> list = Person.all();
            HashMap<String, Object> model = new HashMap<>();
            model.put("list", list);
            return engine.render(model, "/person/list.html");

        });

        get("/person/get", (request, response) -> {

            Super person = Person.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("person", person);
            return engine.render(model, "/person/get.html");

        });

        get("/person/update", (request, response) -> {

            Super person = Person.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("person", person);
            return engine.render(model, "/person/update.html");

        });

        post("/person/update", (request, response) -> {
            System.out.println(request.contentType()); // What type of data am I sending?
            System.out.println(request.queryParams()); // What are the params sent?

            Person person = (Person) Person.get(Integer.parseInt(request.queryParams("id")));
            person.setName(request.queryParams("name"));
            person.setAge(Integer.parseInt(request.queryParams("age")));
            person.save();

            response.redirect("/person/get?id=" + request.queryParams("id"));

            return "";
        });

        get("/person/delete", (req, res) -> {

            try {
            Person.get(Integer.parseInt(req.queryParams("id"))).delete();
            } catch (Exception e) {

            }

            res.redirect("/person/list");
            return "";
        });

        get("/person/create", (req, res) -> {
            return engine.render(null, "/person/create.html");
        });

        post("/person/create", (req, res) -> {
            Person person = new Person();
            person.setName(req.queryParams("name"));
            person.setAge(Integer.parseInt(req.queryParams("age")));
            person.save();
            res.redirect("/person/list");
            return "";
        });
        }
    }
