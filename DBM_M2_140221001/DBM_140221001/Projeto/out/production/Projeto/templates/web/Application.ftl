
/*
* Simple Spark web application
*
*/

import utils.FreemarkerEngine;

import java.util.HashMap;
import java.util.List;
import com.google.gson.Gson;
import static spark.Spark.*;

public class Application {

    public static void main(String[] args) {

        // Configure Spark
        port(1234);
        staticFiles.externalLocation("src/resources/public/");

        // Configure freemarker engine
        FreemarkerEngine engine = new FreemarkerEngine("src/resources/templates");

        // Set up endpoints
        get("/", (request, response) -> {
        return engine.render(null, "index.html");
        });
<#-- gerar de acordo com as classes-->
<#list classes as class>
        ${class.name?lower_case?cap_first}EndPoints.mount(engine);
</#list>
        }
    }