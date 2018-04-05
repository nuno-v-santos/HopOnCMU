package M2T;

import M2TBd.M2TBd;
import metamodels.Class;
import metamodels.Model;
import metamodels.Relation;
import metamodels.RelationType;
import utils.transformations.Model2Text;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by pctm on 08/05/2017.
 */
public class Java implements M2TClass {

    public final String PATH_DEFAULT = "src/proj/";


    @Override
    public void convert(Model model, M2TBd m2TBd, String path) {
        boolean pkg = false;

        if (path == null) {
            path = PATH_DEFAULT + "/" + model.getName().toLowerCase();
            pkg = true;
        }

        List<Relation> relations = model.getRelations();

        for (Relation r : relations) {

            r.getBase().getRelations().add(r);

            if (r.getType() == RelationType.OneToMany) {

                r.getTarget().getRelations().add(new Relation(r.getTarget(), r.getBase(), RelationType.ManyToOne));

            } else if (r.getType() == RelationType.ManyToOne) {

                r.getTarget().getRelations().add(new Relation(r.getTarget(), r.getBase(), RelationType.OneToMany));

            } else {

                r.getTarget().getRelations().add(r);
            }
        }

        // Generate SQL tables
        Model2Text model2Text = new Model2Text("src/templates");
        System.out.println(m2TBd.getBdTyp().toString());
        model2Text.addShared("bdname", m2TBd.getBdTyp().toString());
        model2Text.addShared("bdinclude", m2TBd.getIncludeClassFileName());
        model2Text.addShared("bdincludefunctions", m2TBd.getImportFunctionsFileName());
        model2Text.addShared("pkg", pkg ? "sim" : "nao");


        for (int i = 0; i < model.getClasses().size(); i++) {

            Class c = model.getClasses().get(i);
            String protectedAreas = M2TClass.getProtectedAreas(path + "/" + c.getName() + ".java");
            c.setProtectedMethods(protectedAreas);

            try {
                PrintWriter out = new PrintWriter(path + "/" + c.getName() + ".java");
                String result = model2Text.render(c, "java/java_class.ftl");
                out.println(result);
                out.close();
            } catch (FileNotFoundException e) {
                System.out.println(e);
                System.out.println("error generating .java");
            }

        }

        generateMain(path);

    }

    private void generateMain(String path){
        String content = "public class Main {\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "\n" +
                "\n" +
                "    }\n" +
                "}";


        try {
            PrintWriter out = new PrintWriter(path + "/Main.java");
            out.println(content);
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.out.println("error generating .java");
        }

    }

    @Override
    public String toString() {
        return "Java";
    }
}
