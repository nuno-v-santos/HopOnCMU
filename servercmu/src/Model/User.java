package Model;

import utils.sqlite.BdConnection;
import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private static BdConnection con = SQLiteConn.getInstace("src/ORM.db");
    private String username;

    // public constructor
    public User() {
        
        this.id = -1;
    }



    public int getId() {

        return this.id;
    }


    public void setId(int id) {

        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }



   
    
    public void save() {


        String query = "";

        if (this.id == -1) {

            
            query = "Insert into User(username ) values (?)";

            List<String> args = new ArrayList<>();
            args.add(this.username+"");
            this.id =  con.executeUpdate(query,args);
        } else {
            
            query = "Update User set username = ? where id = ?";

            List<String> args = new ArrayList<>();
            args.add(this.username+"");
            args.add(this.id+"");
            con.executeUpdate(query,args);

        }



    }


    public static List<User> all() {
        String query = String.format("SELECT * FROM User;");
        ResultSet result = con.executeQuery(query);

        List<User> lista = new ArrayList<>();
        try {
            while (result.next()) {

                User p = getUserClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<User> where(String clause) {

        String query = String.format("SELECT * FROM User where " + clause + ";");
        System.out.println(query);
        ResultSet result = con.executeQuery(query);
        List<User> lista = new ArrayList<>();

        try {
            while (result.next()) {

                User p = getUserClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }


    public static User get(int i) {
        String query = String.format("SELECT * FROM User where id = ? ;");
           List<String> args = new ArrayList<>();
           args.add(i+"");
           ResultSet result = con.executeQuery(query,args);

        try {
            while (result.next()) {

                User p = getUserClass(result);
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
        con.executeUpdate("DELETE FROM User WHERE id = ? ", args);
        System.out.println("Success");


    }


   public static User getUserClass(ResultSet result) throws SQLException {

        int id = result.getInt("id");
        String username = result.getString("username");

        //Manel p = new Manel(Person.get(id));
         User p = new User();
        p.setId(id);
        p.setUsername(username);
       return p;

   }


    public Session getSession() {
    // select * from Session as a inner join User as b on a.id = b.session_id where b.id = this.id;

        String query = String.format("select a.* from Session as a inner join User as b on a.id = b.session_id where b.id ="+ this.id);
        ResultSet result = con.executeQuery(query);

            try {
                while (result.next()) {

                    Session p = (Session) Session.getSessionClass(result);
                    return p;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        return null;

    }

    public void setSession(Session session) {

        String query = String.format("Update %s set session_id = %s  where id = %d", "User", session != null ? "'"+session.getId()+"'" :"null", this.id);
        con.executeUpdate(query);

    }







    public Ticket getTicket() {
    // select * from Ticket as a inner join User as b on a.id = b.ticket_id where b.id = this.id;

        String query = String.format("select a.* from Ticket as a inner join User as b on a.id = b.ticket_id where b.id ="+ this.id);
        ResultSet result = con.executeQuery(query);

            try {
                while (result.next()) {

                    Ticket p = (Ticket) Ticket.getTicketClass(result);
                    return p;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        return null;

    }

    public void setTicket(Ticket ticket) {

        String query = String.format("Update %s set ticket_id = %s  where id = %d", "User", ticket != null ? "'"+ticket.getId()+"'" :"null", this.id);
        con.executeUpdate(query);

    }





   /* public List<QuizResponse> getQuizResponses() {
        // select * from QuizResponse where user_id = this.id;
    }*/

    public List<QuizResponse> getQuizResponses() {
        // select * from Person where manel_id = this.id;
        String query = String.format("select * from QuizResponse where user_id = " + this.id);
        ResultSet result = con.executeQuery(query);
        List<QuizResponse> lista = new ArrayList<>();

        try {

            while (result.next()) {

                QuizResponse p = (QuizResponse) QuizResponse.getQuizResponseClass(result);
                lista.add(p);

            }

            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }




    @Override
    public String toString() {
        return "User{" +
                " username=" + username +
                '}';
    }

}
