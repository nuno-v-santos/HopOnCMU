package M2TBd;

import metamodels.BD_Type;
import metamodels.Model;
import utils.sqlite.SQLiteConn;
import utils.transformations.Model2Text;

/**
 * Created by pctm on 08/05/2017.
 */

public class Sqlite3 implements M2TBd {

    @Override
    public void convert(Model model, String path) {

        Model2Text model2Text = new Model2Text("src/templates");
        SQLiteConn sq = new SQLiteConn(path == null ? "src/ORM.db" : path+"/ORM.db");
        String sqlTables = model2Text.render(model, "sqlite/sqlite3_create.ftl");
        //System.out.println(sqlTables);
        sq.executeAll(sqlTables);
    }

    @Override
    public BD_Type getBdTyp() {
        return BD_Type.Sqlite3;
    }

    @Override
    public String getIncludeClassFileName() {
        return "java_sqlite3.ftl";
    }

    @Override
    public String getImportFunctionsFileName() {
        return "java_sqlite3_functions.ftl";
    }

    @Override
    public String toString() {
        return "Sqlite3";
    }
}