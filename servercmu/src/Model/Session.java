package Model;

import utils.sqlite.BdConnection;
import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Session {
    private int id;
    private static BdConnection con = SQLiteConn.getInstace("src/ORM.db");
    private String token;
    private int random;
    private String sharedToken;

    // public constructor
    public Session() {
        
        this.id = -1;
    }



    public int getId() {

        return this.id;
    }


    public void setId(int id) {

        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {

        this.token = token;
    }

    public int getRandom() {
        return random;
    }

    public void setRandom(int random) {

        this.random = random;
    }

    public String getSharedToken() {
        return sharedToken;
    }

    public void setSharedToken(String sharedToken) {

        this.sharedToken = sharedToken;
    }



   
    
    public void save() {


        String query = "";

        if (this.id == -1) {

            
            query = "Insert into Session(token, random, sharedToken ) values (? , ? , ?)";

            List<String> args = new ArrayList<>();
            args.add(this.token+"");
            args.add(this.random+"");
            args.add(this.sharedToken+"");
            this.id =  con.executeUpdate(query,args);
        } else {
            
            query = "Update Session set token = ? , random = ? , sharedToken = ? where id = ?";

            List<String> args = new ArrayList<>();
            args.add(this.token+"");
            args.add(this.random+"");
            args.add(this.sharedToken+"");
            args.add(this.id+"");
            con.executeUpdate(query,args);

        }



    }


    public static List<Session> all() {
        String query = String.format("SELECT * FROM Session;");
        ResultSet result = con.executeQuery(query);

        List<Session> lista = new ArrayList<>();
        try {
            while (result.next()) {

                Session p = getSessionClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<Session> where(String clause) {

        String query = String.format("SELECT * FROM Session where " + clause + ";");
        ResultSet result = con.executeQuery(query);
        List<Session> lista = new ArrayList<>();

        try {
            while (result.next()) {

                Session p = getSessionClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }


    public static Session get(int i) {
        String query = String.format("SELECT * FROM Session where id = ? ;");
           List<String> args = new ArrayList<>();
           args.add(i+"");
           ResultSet result = con.executeQuery(query,args);

        try {
            while (result.next()) {

                Session p = getSessionClass(result);
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    
    public void delete() {

        List<String> args = new ArrayList<>();
        args.add(this.id+"");
        con.executeUpdate("DELETE FROM Session WHERE id = ? ", args);
        System.out.println("Success");


    }


   public static Session getSessionClass(ResultSet result) throws SQLException {

        int id = result.getInt("id");
        String token = result.getString("token");
        int random = result.getInt("random");
        String sharedToken = result.getString("sharedToken");

        //Manel p = new Manel(Person.get(id));
         Session p = new Session();
        p.setId(id);
        p.setToken(token);
        p.setRandom(random);
        p.setSharedToken(sharedToken);
       return p;

   }




    public User getUser() {
    // select * from Session as a inner join User as b on a.id = b.session_id where b.id = this.id;

        String query = String.format("select * from User  where session_id ="+ this.id);
        ResultSet result = con.executeQuery(query);

            try {
                while (result.next()) {

                    User p = (User) User.getUserClass(result);
                    return p;

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        return null;

    }

    public void setUser(User user) {

        String query = String.format("Update %s set session_id = '%d'  where id = %d", "User",this.id,  user != null ? user.getId() : -1);
        if(user == null){
            query = String.format("Update %s set session_id = null  where session_id = %d", "User",this.id);
        }
        con.executeUpdate(query);

    }







    @Override
    public String toString() {
        return "Session{" +
                " token=" + token +
                " random=" + random +
                " sharedToken=" + sharedToken +
                '}';
    }

}
