
/*
* Simple Spark web application
*
*/

import person.Person;
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
        FreemarkerEngine engine = new FreemarkerEngine("src/resources/templates");

        // Set up endpoints
        get("/", (request, response) -> {
        return engine.render(null, "index.html");
        });
<#-- gerar de acordo com as classes-->
<#list classes as class>
        // Set up Person endpoints
        get("/${class.name?lower_case}/list", (request, response) -> {

            List<${class.name}> list = ${class.name}.all();
            HashMap<String, Object> model = new HashMap<>();
            model.put("list", list);
            return engine.render(model, "/${class.name?lower_case}/list.html");

        });

        get("/${class.name?lower_case}/get", (request, response) -> {

            ${class.name} ${class.name?lower_case} = ${class.name}.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("${class.name?lower_case}", ${class.name?lower_case});
            return engine.render(model, "/${class.name?lower_case}/get.html");

        });

        get("/${class.name?lower_case}/update", (request, response) -> {

            ${class.name} ${class.name?lower_case} = ${class.name}.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("${class.name?lower_case}", ${class.name?lower_case});
            return engine.render(model, "/${class.name?lower_case}/update.html");

        });

        post("/${class.name?lower_case}/update", (request, response) -> {
            System.out.println(request.contentType()); // What type of data am I sending?
            System.out.println(request.queryParams()); // What are the params sent?
            System.out.println(request.raw()); // What's the raw data sent?

            ${class.name} ${class.name?lower_case} = ${class.name}.get(Integer.parseInt(request.queryParams("id")));
            <#list class.attributes as attribute>
                    <#if attribute.type?contains("int") >
            ${class.name?lower_case}.set${attribute.name?cap_first}(Integer.parseInt(request.queryParams("${attribute.name}")));
                    <#else>
            ${class.name?lower_case}.set${attribute.name?cap_first}(request.queryParams("${attribute.name}"));
            </#if>
            </#list>
            ${class.name?lower_case}.save();

            response.redirect("/${class.name?lower_case}/get?id=" + request.queryParams("id"));

            return "";
        });

        get("/${class.name?lower_case}/delete", (req, res) -> {

            try {
            ${class.name}.get(Integer.parseInt(req.queryParams("id"))).delete();
            } catch (Exception e) {

            }

            res.redirect("/${class.name?lower_case}/list");
            return "";
        });

        get("/${class.name?lower_case}/create", (req, res) -> {
            return engine.render(null, "/${class.name?lower_case}/create.html");
        });

        post("/${class.name?lower_case}/create", (req, res) -> {
            ${class.name} ${class.name?lower_case} = new ${class.name}();
            <#list class.attributes as attribute>
                    <#if attribute.type?contains("int") >
            ${class.name?lower_case}.set${attribute.name?cap_first}(Integer.parseInt(req.queryParams("${attribute.name}")));
                    <#else>
            ${class.name?lower_case}.set${attribute.name?cap_first}(req.queryParams("${attribute.name}"));
                    </#if>
            </#list>
            ${class.name?lower_case}.save();
            res.redirect("/${class.name?lower_case}/list");
            return "";
        });
</#list>
        }
    }