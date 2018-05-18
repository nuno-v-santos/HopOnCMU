package Model;

import utils.sqlite.BdConnection;
import utils.sqlite.SQLiteConn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserMonument {


    private static BdConnection con = SQLiteConn.getInstace("src/ORM.db");
    private int id;
    private int mon_id;
    private int user_id;
    private String status;
    private String quizStatus;


    public UserMonument() {

        this.id = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMon_id() {
        return mon_id;
    }

    public void setMon_id(int mon_id) {
        this.mon_id = mon_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuizStatus() {
        return quizStatus;
    }

    public void setQuizStatus(String quizStatus) {
        this.quizStatus = quizStatus;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void save() {


        String query = "";

        if (this.id == -1) {


            query = "Insert into UserMonument(user_id, mon_id, status, quizStatus ) values (?, ?, ?, ?)";

            List<String> args = new ArrayList<>();
            args.add(this.user_id + "");
            args.add(this.mon_id + "");
            args.add(this.status + "");
            args.add(this.quizStatus + "");
            this.id = con.executeUpdate(query, args);
        } else {

            query = "Update UserMonument set user_id = ?, mon_id = ?, status= ?, quizStatus = ? where id = ?";

            List<String> args = new ArrayList<>();
            args.add(this.user_id + "");
            args.add(this.mon_id + "");
            args.add(this.status + "");
            args.add(this.quizStatus + "");
            args.add(this.id + "");
            con.executeUpdate(query, args);

        }


    }

    public static List<UserMonument> where(String clause) {

        String query = String.format("SELECT * FROM UserMonument where " + clause + ";");
        System.out.println(query);
        ResultSet result = con.executeQuery(query);
        List<UserMonument> lista = new ArrayList<>();

        try {
            while (result.next()) {

                UserMonument p = getUserMonumentClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }

    public static UserMonument getUserMonumentClass(ResultSet result) throws SQLException {

        int id = result.getInt("id");
        int user_id = result.getInt("user_id");
        int mon_id = result.getInt("mon_id");
        String status = result.getString("status");
        String quizStatus = result.getString("quizStatus");

        //Manel p = new Manel(Person.get(id));
        UserMonument p = new UserMonument();
        p.setId(id);
        p.setMon_id(mon_id);
        p.setUser_id(user_id);
        p.setStatus(status);
        p.setQuizStatus(quizStatus);
        return p;

    }

}
