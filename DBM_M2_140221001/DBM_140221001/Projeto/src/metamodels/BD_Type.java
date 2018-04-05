package metamodels;

/**
 * Created by pctm on 08/05/2017.
 */
public enum BD_Type {
    Sqlite3,MySql;

    @Override
    public String toString() {
        switch (this){

            case MySql: return "MySql";
            case Sqlite3: return "Sqlite3";
            default: return "";
        }
    }
}
