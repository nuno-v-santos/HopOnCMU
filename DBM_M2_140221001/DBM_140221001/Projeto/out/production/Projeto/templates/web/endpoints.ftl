import com.google.gson.Gson;
import utils.FreemarkerEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class ${name?lower_case?cap_first}EndPoints{

    public static void mount(FreemarkerEngine engine){

        // Set up Person endpoints
        get("/${name?lower_case}/list", (request, response) -> {
        
            List<${name}> list = ${name}.all();
            HashMap<String, Object> model = new HashMap<>();
            model.put("list", list);
            return engine.render(model, "/${name?lower_case}/list.html");
            
        });
        
        get("/${name?lower_case}/get", (request, response) -> {
        
            ${name} ${name?lower_case} = ${name}.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("${name?lower_case}", ${name?lower_case});
            return engine.render(model, "/${name?lower_case}/get.html");
        
        });
        
        get("/${name?lower_case}/update", (request, response) -> {
        
            ${name} ${name?lower_case} = ${name}.get(Integer.parseInt(request.queryParams("id")));
            HashMap<String, Object> model = new HashMap<>();
            model.put("${name?lower_case}", ${name?lower_case});
            <#list relations as relation >
                <#switch relation.type>
                    <#case "OneToMany">
                    model.put("${name?lower_case}${relation.target.name}", ${name?lower_case}.get${relation.target.name}s());
                    model.put("${relation.target.name?lower_case}", ${relation.target.name}.all());
                        <#break >
                    <#case "OneToOne">
                        <#if relation.base.name?contains(name)>
                        model.put("${name?lower_case}${relation.target.name}", ${name?lower_case}.get${relation.target.name}() != null ? ${name?lower_case}.get${relation.target.name}() : new ${relation.target.name}());
                        model.put("${relation.target.name?lower_case}", ${relation.target.name}.all());
                        <#else>
                        model.put("${name?lower_case}${relation.base.name}", ${name?lower_case}.get${relation.base.name}() != null ? ${name?lower_case}.get${relation.base.name}() : new ${relation.base.name}() );
                        model.put("${relation.base.name?lower_case}", ${relation.base.name}.all());
                        </#if>
                        <#break >
                    <#case "ManyToOne">
                    model.put("${relation.base.name?lower_case}${relation.target.name}", ${relation.base.name?lower_case}.get${relation.target.name}() == null? new ${relation.target.name}() : ${relation.base.name?lower_case}.get${relation.target.name}());
                    model.put("${relation.target.name?lower_case}", ${relation.target.name}.all());
                        <#break>
                    <#case "ManyToMany">
                        <#if relation.base.name?contains(name)>
                        model.put("${relation.base.name?lower_case}${relation.target.name}", ${relation.base.name?lower_case}.get${relation.target.name}s() == null? new ${relation.target.name}() : ${relation.base.name?lower_case}.get${relation.target.name}s());
                        model.put("${relation.target.name?lower_case}", ${relation.target.name}.all());
                        <#else>
                        model.put("${relation.target.name?lower_case}${relation.base.name}", ${relation.target.name?lower_case}.get${relation.base.name}s() == null? new ${relation.base.name}() : ${relation.target.name?lower_case}.get${relation.base.name}s());
                        model.put("${relation.base.name?lower_case}", ${relation.base.name}.all());
                        </#if>
                        <#break>
                </#switch>
            </#list>
            return engine.render(model, "/${name?lower_case}/update.html");
        
        });
        
        post("/${name?lower_case}/update", (request, response) -> {
            System.out.println(request.contentType()); // What type of data am I sending?
            System.out.println(request.queryParams()); // What are the params sent?
            System.out.println(request.raw()); // What's the raw data sent?

            ${name} ${name?lower_case} = ${name}.get(Integer.parseInt(request.queryParams("id")));
            <#list attributes as attribute>
                <#if attribute.type?contains("int") >
                ${name?lower_case}.set${attribute.name?cap_first}(Integer.parseInt(!request.queryParams("${attribute.name}").isEmpty() ? request.queryParams("${attribute.name}") : "0"));
                <#else>
                ${name?lower_case}.set${attribute.name?cap_first}(request.queryParams("${attribute.name}"));
                </#if>
            </#list>
            ${name?lower_case}.save();
            <#list relations as relation >
                <#switch relation.type>
                    <#case "ManyToOne">
                    ${name?lower_case}.set${relation.target.name}(${relation.target.name}.get(Integer.parseInt(request.queryParams("${relation.target.name?lower_case}Id"))));
                        <#break>
                    <#case "OneToOne">
                        <#if relation.base.name?contains(name)>
                        if(Integer.parseInt(request.queryParams("${relation.target.name?lower_case}Id")) != -1){
                        if(${relation.target.name}.get(Integer.parseInt(request.queryParams("${relation.target.name?lower_case}Id"))).get${relation.base.name}() != null)
                        ${relation.target.name}.get(Integer.parseInt(request.queryParams("${relation.target.name?lower_case}Id"))).set${relation.base.name}(null);
                        }
                        if(${relation.base.name?lower_case}.get${relation.target.name}() != null)
                        ${relation.base.name?lower_case}.set${relation.target.name}(null);

                        ${name?lower_case}.set${relation.target.name}(${relation.target.name}.get(Integer.parseInt(request.queryParams("${relation.target.name?lower_case}Id"))));
                        <#else>
                        if(Integer.parseInt(request.queryParams("${relation.base.name?lower_case}Id")) != -1){
                        if(${relation.base.name}.get(Integer.parseInt(request.queryParams("${relation.base.name?lower_case}Id"))).get${relation.target.name}() != null)
                        ${relation.base.name}.get(Integer.parseInt(request.queryParams("${relation.base.name?lower_case}Id"))).set${relation.target.name}(null);
                        }
                        if(${relation.target.name?lower_case}.get${relation.base.name}() != null)
                        ${relation.target.name?lower_case}.set${relation.base.name}(null);
                        ${name?lower_case}.set${relation.base.name}(${relation.base.name}.get(Integer.parseInt(request.queryParams("${relation.base.name?lower_case}Id"))));
                        </#if>
                        <#break>
                </#switch>
            </#list>
            response.redirect("/${name?lower_case}/list?id=" + request.queryParams("id"));

            return "";
        });
        
        get("/${name?lower_case}/delete", (req, res) -> {
        
            try {
            ${name}.get(Integer.parseInt(req.queryParams("id"))).delete();
            } catch (Exception e) {

            }

            res.redirect("/${name?lower_case}/list");
            return "";
        });
        
        get("/${name?lower_case}/create", (req, res) -> {
            return engine.render(null, "/${name?lower_case}/create.html");
        });
        
        post("/${name?lower_case}/create", (req, res) -> {
            ${name} ${name?lower_case} = new ${name}();
            <#list attributes as attribute>
                <#if attribute.type?contains("int") >
                ${name?lower_case}.set${attribute.name?cap_first}(Integer.parseInt(!req.queryParams("${attribute.name}").isEmpty() ? req.queryParams("${attribute.name}") : "0"));
                <#else>
                ${name?lower_case}.set${attribute.name?cap_first}(req.queryParams("${attribute.name}"));
                </#if>
            </#list>
            ${name?lower_case}.save();
            res.redirect("/${name?lower_case}/list");
            return "";
        });
        
<#list relations as relation >
    <#switch relation.type>
        <#case "OneToMany">
        post("/${name?lower_case}/add${relation.target.name}", (request, response) -> {

            ${relation.target.name}.get(Integer.parseInt(request.queryParams("add"))).set${name}(${name}.get(Integer.parseInt(request.queryParams("id"))));

            response.redirect("/${name?lower_case}/update?id=" + request.queryParams("id"));
            return "";
        });

        get("/${name?lower_case}/remove${relation.target.name}", (request, response) -> {
            ${relation.target.name}.get(Integer.parseInt(request.queryParams("rmv"))).set${name}(null);
            response.redirect("/${name?lower_case}/update?id=" + request.queryParams("id"));
            return "";
        });
            <#break >
        <#case "ManyToMany">
            <#if relation.base.name?contains(name) >
        post("/${name?lower_case}/add${relation.target.name}", (request, response) -> {

            ${relation.target.name}.get(Integer.parseInt(request.queryParams("add"))).add${name}(${name}.get(Integer.parseInt(request.queryParams("id"))));

            response.redirect("/${name?lower_case}/update?id=" + request.queryParams("id"));
            return "";
        });

        get("/${name?lower_case}/remove${relation.target.name}", (request, response) -> {
            ${relation.base.name}.get(Integer.parseInt(request.queryParams("id"))).remove${relation.target.name}(${relation.target.name}.get(Integer.parseInt(request.queryParams("rmv"))));
            response.redirect("/${name?lower_case}/update?id=" + request.queryParams("id"));
            return "";
        });
        <#else>
        post("/${name?lower_case}/add${relation.base.name}", (request, response) -> {

            ${relation.base.name}.get(Integer.parseInt(request.queryParams("add"))).add${name}(${name}.get(Integer.parseInt(request.queryParams("id"))));

            response.redirect("/${name?lower_case}/update?id=" + request.queryParams("id"));
            return "";
        });

        get("/${name?lower_case}/remove${relation.base.name}", (request, response) -> {
            ${relation.target.name}.get(Integer.parseInt(request.queryParams("id"))).remove${relation.base.name}(${relation.base.name}.get(Integer.parseInt(request.queryParams("rmv"))));
            response.redirect("/${name?lower_case}/update?id=" + request.queryParams("id"));
            return "";
        });
            </#if>
            <#break >
    </#switch>
</#list>
        get("/api/${name?lower_case}", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(${name}.all());
        });
        
        get("/api/${name?lower_case}/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            return gson.toJson(${name}.get(Integer.parseInt(req.params("id"))));
        });
        
        post("/api/${name?lower_case}", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            ${name} ${name?lower_case} = gson.fromJson(req.body(), ${name}.class);
            ${name?lower_case}.save();
            return gson.toJson(${name?lower_case});
        });
        
        put("/api/${name?lower_case}/:id", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            ${name} ${name?lower_case} = gson.fromJson(req.body(), ${name}.class);
            ${name?lower_case}.setId((Integer.parseInt(req.params("id"))));
            ${name?lower_case}.save();
            return gson.toJson(${name?lower_case});
        });
        
        delete("/api/${name?lower_case}/:id", (req, res) -> {
        
            res.type("application/json");
            ${name} ${name?lower_case} = ${name}.get((Integer.parseInt(req.params("id"))));
            if (${name?lower_case} != null) {
            ${name?lower_case}.delete();
            return true;
            }

            return false;
        
        });


    }
}