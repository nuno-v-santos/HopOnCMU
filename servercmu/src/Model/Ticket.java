package Model;

import utils.sqlite.BdConnection;
import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
    private int id;
    private static BdConnection con = SQLiteConn.getInstace("src/ORM.db");
    private int number;
    private int taken;

    // public constructor
    public Ticket(int number) {
        
        this.id = -1;
        this.number = number;
    }

    //protected construtor for child and internal consume
    public Ticket() {
        
        this.id=-1;
    }


    public int getId() {

        return this.id;
    }


    public void setId(int id) {

        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {

        this.number = number;
    }

    public int getTaken() {
        return taken;
    }

    public void setTaken(int taken) {

        this.taken = taken;
    }



   
    
    public void save() {


        String query = "";

        if (this.id == -1) {

            
            query = "Insert into Ticket(number, taken ) values (? , ?)";

            List<String> args = new ArrayList<>();
            args.add(this.number+"");
            args.add(this.taken+"");
            this.id =  con.executeUpdate(query,args);
        } else {
            
            query = "Update Ticket set number = ? , taken = ? where id = ?";

            List<String> args = new ArrayList<>();
            args.add(this.number+"");
            args.add(this.taken+"");
            args.add(this.id+"");
            con.executeUpdate(query,args);

        }



    }


    public static List<Ticket> all() {
        String query = String.format("SELECT * FROM Ticket;");
        ResultSet result = con.executeQuery(query);

        List<Ticket> lista = new ArrayList<>();
        try {
            while (result.next()) {

                Ticket p = getTicketClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<Ticket> where(String clause) {

        String query = String.format("SELECT * FROM Ticket where " + clause + ";");
        ResultSet result = con.executeQuery(query);
        List<Ticket> lista = new ArrayList<>();

        try {
            while (result.next()) {

                Ticket p = getTicketClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }


    public static Ticket get(int i) {
        String query = String.format("SELECT * FROM Ticket where id = ? ;");
           List<String> args = new ArrayList<>();
           args.add(i+"");
           ResultSet result = con.executeQuery(query,args);

        try {
            while (result.next()) {

                Ticket p = getTicketClass(result);
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
        con.executeUpdate("DELETE FROM Ticket WHERE id = ? ", args);
        System.out.println("Success");


    }


   public static Ticket getTicketClass(ResultSet result) throws SQLException {

        int id = result.getInt("id");
        int number = result.getInt("number");
        int taken = result.getInt("taken");

        //Manel p = new Manel(Person.get(id));
         Ticket p = new Ticket();
        p.setId(id);
        p.setNumber(number);
        p.setTaken(taken);
       return p;

   }




    public User getUser() {
    // select * from Ticket as a inner join User as b on a.id = b.ticket_id where b.id = this.id;

        String query = String.format("select * from User  where ticket_id ="+ this.id);
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

        String query = String.format("Update %s set ticket_id = '%d'  where id = %d", "User",this.id,  user != null ? user.getId() : -1);
        if(user == null){
            query = String.format("Update %s set ticket_id = null  where ticket_id = %d", "User",this.id);
        }
        con.executeUpdate(query);

    }






    public Tour getTour() {
        // select * from Tour as a inner join Ticket as b on a.id = b.tour_id where b.id = this.id;

        String query = String.format("select a.* from Tour as a inner join Ticket as b on a.id = b.tour_id where b.id ="+ this.id);
        ResultSet result = con.executeQuery(query);

        try {
            while (result.next()) {

                Tour p = (Tour) Tour.getTourClass(result);
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void setTour(Tour tour) {

        String query = String.format("Update %s set tour_id = '%d'  where id = %d", "Ticket", tour != null ? tour.getId() : -1, this.id);
        con.executeUpdate(query);

    }



    @Override
    public String toString() {
        return "Ticket{" +
                " number=" + number +
                " taken=" + taken +
                '}';
    }

}
