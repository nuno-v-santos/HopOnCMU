
package Model;
import utils.sqlite.BdConnection;
import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
public class QuizResponse {
    private int id;
    private static BdConnection con = SQLiteConn.getInstace("src/ORM.db");
    private int score;
    private Date date;

    // public constructor
    public QuizResponse() {
        
        this.id = -1;
    }



    public int getId() {

        return this.id;
    }


    public void setId(int id) {

        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {

        this.score = score;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {

        this.date = date;
    }



   
    
    public void save() {


        String query = "";

        if (this.id == -1) {

            
            query = "Insert into QuizResponse(score, date ) values (? , ?)";

            List<String> args = new ArrayList<>();
            args.add(this.score+"");
            args.add(new java.sql.Timestamp(this.date.getTime())+"");
            this.id =  con.executeUpdate(query,args);
        } else {
            
            query = "Update QuizResponse set score = ? , date = ? where id = ?";

            List<String> args = new ArrayList<>();
            args.add(this.score+"");
            args.add(new java.sql.Timestamp(this.date.getTime())+"");
            args.add(this.id+"");
            con.executeUpdate(query,args);

        }



    }


    public static List<QuizResponse> all() {
        String query = String.format("SELECT * FROM QuizResponse;");
        ResultSet result = con.executeQuery(query);

        List<QuizResponse> lista = new ArrayList<>();
        try {
            while (result.next()) {

                QuizResponse p = getQuizResponseClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<QuizResponse> where(String clause) {

        String query = String.format("SELECT * FROM QuizResponse where " + clause + ";");
        ResultSet result = con.executeQuery(query);
        List<QuizResponse> lista = new ArrayList<>();

        try {
            while (result.next()) {

                QuizResponse p = getQuizResponseClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }


    public static QuizResponse get(int i) {
        String query = String.format("SELECT * FROM QuizResponse where id = ? ;");
           List<String> args = new ArrayList<>();
           args.add(i+"");
           ResultSet result = con.executeQuery(query,args);

        try {
            while (result.next()) {

                QuizResponse p = getQuizResponseClass(result);
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
        con.executeUpdate("DELETE FROM QuizResponse WHERE id = ? ", args);
        System.out.println("Success");


    }


   public static QuizResponse getQuizResponseClass(ResultSet result) throws SQLException {

        int id = result.getInt("id");
        int score = result.getInt("score");
        Date date = result.getDate("date");

        //Manel p = new Manel(Person.get(id));
         QuizResponse p = new QuizResponse();
        p.setId(id);
        p.setScore(score);
        p.setDate(date);
       return p;

   }


    public Quiz getQuiz() {
        // select * from Quiz as a inner join QuizResponse as b on a.id = b.quiz_id where b.id = this.id;

        String query = String.format("select a.* from Quiz as a inner join QuizResponse as b on a.id = b.quiz_id where b.id ="+ this.id);
        ResultSet result = con.executeQuery(query);

        try {
            while (result.next()) {

                Quiz p = (Quiz) Quiz.getQuizClass(result);
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void setQuiz(Quiz quiz) {

        String query = String.format("Update %s set quiz_id = '%d'  where id = %d", "QuizResponse", quiz != null ? quiz.getId() : -1, this.id);
        con.executeUpdate(query);

    }


    public User getUser() {
        // select * from User as a inner join QuizResponse as b on a.id = b.user_id where b.id = this.id;

        String query = String.format("select a.* from User as a inner join QuizResponse as b on a.id = b.user_id where b.id ="+ this.id);
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

        String query = String.format("Update %s set user_id = '%d'  where id = %d", "QuizResponse", user != null ? user.getId() : -1, this.id);
        con.executeUpdate(query);

    }



    @Override
    public String toString() {
        return "QuizResponse{" +
                " score=" + score +
                " date=" + date +
                '}';
    }

}
