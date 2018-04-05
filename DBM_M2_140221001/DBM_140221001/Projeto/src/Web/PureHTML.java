package Web;

import M2T.M2TClass;
import metamodels.Class;
import metamodels.Model;
import org.apache.commons.io.FileUtils;
import utils.transformations.Model2Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by pctm on 01/06/2017.
 */
public class PureHTML implements Web {

    @Override
    public void build(Model model) {

        build(model, PATH_DEFAULT);

    }

    @Override
    public void build(Model model, String path) {

        //generating folders
        File dir = new File(path + "resources");
        if (!dir.exists()) {
            dir.mkdir();
        }
        dir = new File(path + "resources/templates");
        if (!dir.exists()) {
            dir.mkdir();
        }


        //render index.html
        Model2Text model2Text = new Model2Text("src/templates");
        String result = model2Text.render(model, "/web/index.ftl");


        generateFile(path + "resources/templates/index.html", result);


        //render CRUD for each class
        result = model2Text.render(model, "/web/Application.ftl");
        System.out.println(result);

        for (int i = 0; i < model.getClasses().size(); i++) {

            //get the class
            Class c = model.getClasses().get(i);

            //create directory
            dir = new File(path + "resources/templates/" + c.getName().toLowerCase());
            dir.mkdir();
            //generate list
            generateFile(path + "resources/templates/" + c.getName().toLowerCase() + "/list.html", model2Text.render(c, "/web/list.ftl"));
            //generate get
            generateFile(path + "resources/templates/" + c.getName().toLowerCase() + "/get.html", model2Text.render(c, "/web/get.ftl"));
            //generate create
            generateFile(path + "resources/templates/" + c.getName().toLowerCase() + "/create.html", model2Text.render(c, "/web/create.ftl"));
            //generate update
            generateFile(path + "resources/templates/" + c.getName().toLowerCase() + "/update.html", model2Text.render(c, "/web/update.ftl"));


        }

        generateFile(path + "resources/templates/imports.html", model2Text.render(null, "/web/imports.ftl"));
        generateFile(path + "resources/templates/menu.html", model2Text.render(model, "/web/menu.ftl"));


        //generate Application.java
        generateFile(path + "Application.java", model2Text.render(model, "/web/Application.ftl"));

        for (Class c : model.getClasses()) {
            generateFile(path + Character.toUpperCase(c.getName().charAt(0)) + c.getName().substring(1).toLowerCase() + "EndPoints.java", model2Text.render(c, "/web/endpoints.ftl"));
        }


        try {
            FileUtils.copyDirectory(new File("src/templates/web/public"), new File(path + "resources/public/"));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void generateFile(String path, String content) {
        try {
            PrintWriter out = new PrintWriter(path);
            out.println(content);
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.out.println("error generating : " + path);
        }
    }
}
