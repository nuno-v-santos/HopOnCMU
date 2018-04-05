package M2T;

import M2TBd.M2TBd;
import metamodels.Model;
import org.apache.commons.io.FileUtils;
import utils.transformations.Model2Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by pctm on 23/06/2017.
 */
public class ReactNative implements M2TClass {

    public final String PATH_DEFAULT = "src/proj/";

    @Override
    public void convert(Model model, M2TBd m2TBd, String path) {


        if (path == null) {
            path = PATH_DEFAULT + "/" + model.getName().toLowerCase();
        }

        path += "/RN";

        System.out.println("Copying RN to "+path);
        try {
            FileUtils.copyDirectory(new File("src/RN/DBM"), new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("End Copying RN to "+path);

        Model2Text model2Text = new Model2Text("src/templates");

        try {
            PrintWriter out = new PrintWriter(path+"/info.json");
            String result = model2Text.render(model, "ReactNative/conf.ftl");
            out.println(result);
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.out.println("error generating .RN");
        }


    }
}
