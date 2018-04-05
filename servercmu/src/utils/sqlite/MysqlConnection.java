/*
 * SQLite JDBC Connection
 *
 */

package utils.sqlite;

import java.sql.*;
import java.util.List;

public class MysqlConnection extends BdConnection {


    public MysqlConnection() {

        super();

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://localhost/ORM?allowMultiQueries=true&autoReconnect=true&useSSL=false", "root", "1234");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static BdConnection getInstace() {

        if (bdConnection == null) {
            bdConnection = new MysqlConnection();
        }
        return bdConnection;
    }

    /**
     * Closes the database connection.
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes an INSERT, UPDATE or DELETE sql statement.
     *
     * @param sql the sql statement
     * @return either the id or 0 for statements that
     * do not return anything
     */
    public int executeUpdate(String sql) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();

            rs.beforeFirst();

            if (rs.next())
                return rs.getInt(1);
            else
                return -1;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int executeUpdate(String sql, List<String> args) {
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            for (int i = 0; i < args.size(); i++) {
                stmt.setString(i + 1, args.get(i));
            }
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            rs.beforeFirst();

            if (rs.next())
                return rs.getInt(1);
            else
                return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * Executes a given sql statement.
     *
     * @param sql the sql statement
     * @return ResultSet with the results
     */
    public ResultSet executeQuery(String sql) {
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet executeQuery(String sql, List<String> args) {
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            for (int i = 0; i < args.size(); i++) {
                stmt.setString(i + 1, args.get(i));
            }
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Executes a given sql statement.
     *
     * @param sql the sql statement
     * @return ResultSet with the results
     */
    public void executeAll(String sql) {
        //System.out.println(sql);

        String[] queries = sql.split(";");

        try {

            Statement stmt = connection.createStatement();

            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public ResultSet Insert(String sql) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeQuery(sql);
            return stmt.getGeneratedKeys();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
