package M2TBd;

import metamodels.BD_Type;
import metamodels.Model;
import utils.sqlite.MysqlConnection;
import utils.transformations.Model2Text;

/**
 * Created by pctm on 08/05/2017.
 */
public class Mysql implements M2TBd {
    @Override
    public void convert(Model model, String path) {
        Model2Text model2Text = new Model2Text("src/templates");
        MysqlConnection sq = new MysqlConnection();
        String sqlTables = model2Text.render(model, "mysql/mysql_create.ftl");
        System.out.println(sqlTables);
        sq.executeAll(sqlTables);
    }

    @Override
    public BD_Type getBdTyp() {
        return BD_Type.MySql;
    }

    @Override
    public String getIncludeClassFileName() {
        return "java_sqlite3.ftl";
    }

    @Override
    public String getImportFunctionsFileName() {
        return "java_mysql_functions.ftl";
    }

    @Override
    public String toString() {
        return "Mysql";
    }
}
