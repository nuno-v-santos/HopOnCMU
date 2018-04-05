package utils.sqlite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by pctm on 08/05/2017.
 */
public abstract class BdConnection {

    protected static BdConnection bdConnection;
    protected Connection connection;


    public static BdConnection getInstace(String filename) {
        return null;
    }

    /**
     * Closes the database connection.
     */
    public abstract void close();

    /**
     * Executes an INSERT, UPDATE or DELETE sql statement.
     *
     * @param sql the sql statement
     * @return either the id or 0 for statements that
     * do not return anything
     */
    public abstract int executeUpdate(String sql);
    public abstract int executeUpdate(String sql, List<String> args);


    /**
     * Executes a given sql statement.
     *
     * @param sql the sql statement
     * @return ResultSet with the results
     */
    public abstract ResultSet executeQuery(String sql);
    public abstract ResultSet executeQuery(String sql, List<String> args);


    /**
     * Executes a given sql statement.
     *
     * @param sql the sql statement
     * @return ResultSet with the results
     */
    public abstract void executeAll(String sql);

    public abstract ResultSet Insert(String sql);

}
