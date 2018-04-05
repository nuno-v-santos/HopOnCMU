package utils;

import M2M.M2M;
import M2T.Java;
import M2T.M2TClass;
import M2T.ReactNative;
import M2TBd.M2TBd;
import Web.Web;
import metamodels.Model;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pctm on 08/05/2017.
 */
public class Build {

    private M2TClass m2t;
    private M2M m2m;
    private M2TBd m2TBd;
    private Web web;


    public Build(M2TClass m2TClass, M2M m2m, M2TBd m2TBd, Web web) {
        this.m2t = m2TClass;
        this.m2m = m2m;
        this.m2TBd = m2TBd;
        this.web = web;
    }

    public void build() {

        Model model = m2m.getModel();
        File dir = new File("src/proj/" + model.getName().toLowerCase());

        if (!dir.exists()) {
            dir.mkdir();
        }

        m2t.convert(model, m2TBd, null);
        m2TBd.convert(model, null);
        web.build(model);
    }

    public void build(File path){
        Model model = m2m.getModel();
        build(model,path);

    }

    public void build(Model model) {

        File dir = new File("src/proj/" + model.getName().toLowerCase());

        if (!dir.exists()) {
            dir.mkdir();
        }

        m2t.convert(model, m2TBd, null);
        m2TBd.convert(model, null);
        web.build(model);
    }

    public void build(Model model, File dir) {

        File dir2 = new File(dir.toString() + "/" + model.getName().toLowerCase());

        if (!dir2.exists()) {
            dir2.mkdir();
            mountProject(dir.toString() + "/" + model.getName().toLowerCase());
        }

        m2t.convert(model, m2TBd, dir.toString() + "/" + model.getName().toLowerCase() + "/src");
        new ReactNative().convert(model,null,dir.toString() + "/" + model.getName().toLowerCase());
        m2TBd.convert(model, dir.toString() + "/" + model.getName().toLowerCase() + "/src");
        web.build(model, dir.toString() + "/" + model.getName().toLowerCase() + "/src/");
    }


    private void mountProject(String path) {

        try {
            File f = new File("");
            System.out.println(f.getAbsolutePath());
            FileUtils.copyDirectory(new File(f.getAbsolutePath()), new File(path));
            FileUtils.deleteDirectory(new File(path + "/src"));
            new File(path + "/src").mkdir();
            new File(path + "/src/utils").mkdir();
            new File(path + "/src/utils/sqlite").mkdir();
            FileUtils.copyDirectory(new File(f.getAbsolutePath() + "/src/utils/sqlite"), new File(path + "/src/utils/sqlite"));
            FileUtils.copyFile(new File(f.getAbsolutePath() + "/src/utils/FreemarkerEngine.java"), new File(path + "/src/utils/FreemarkerEngine.java"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
