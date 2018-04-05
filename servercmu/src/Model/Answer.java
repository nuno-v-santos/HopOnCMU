
package Model;
import utils.sqlite.BdConnection;
import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Answer {
    private int id;
    private static BdConnection con = SQLiteConn.getInstace("src/ORM.db");
    private String answer;
    private int correct;

    // public constructor
    public Answer() {
        
        this.id = -1;
    }



    public int getId() {

        return this.id;
    }


    public void setId(int id) {

        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {

        this.answer = answer;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {

        this.correct = correct;
    }



   
    
    public void save() {


        String query = "";

        if (this.id == -1) {

            
            query = "Insert into Answer(answer, correct ) values (? , ?)";

            List<String> args = new ArrayList<>();
            args.add(this.answer+"");
            args.add(this.correct+"");
            this.id =  con.executeUpdate(query,args);
        } else {
            
            query = "Update Answer set answer = ? , correct = ? where id = ?";

            List<String> args = new ArrayList<>();
            args.add(this.answer+"");
            args.add(this.correct+"");
            args.add(this.id+"");
            con.executeUpdate(query,args);

        }



    }


    public static List<Answer> all() {
        String query = String.format("SELECT * FROM Answer;");
        ResultSet result = con.executeQuery(query);

        List<Answer> lista = new ArrayList<>();
        try {
            while (result.next()) {

                Answer p = getAnswerClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<Answer> where(String clause) {

        String query = String.format("SELECT * FROM Answer where " + clause + ";");
        ResultSet result = con.executeQuery(query);
        List<Answer> lista = new ArrayList<>();

        try {
            while (result.next()) {

                Answer p = getAnswerClass(result);
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }


    public static Answer get(int i) {
        String query = String.format("SELECT * FROM Answer where id = ? ;");
           List<String> args = new ArrayList<>();
           args.add(i+"");
           ResultSet result = con.executeQuery(query,args);

        try {
            while (result.next()) {

                Answer p = getAnswerClass(result);
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
        con.executeUpdate("DELETE FROM Answer WHERE id = ? ", args);
        System.out.println("Success");


    }


   public static Answer getAnswerClass(ResultSet result) throws SQLException {

        int id = result.getInt("id");
        String answer = result.getString("answer");
        int correct = result.getInt("correct");

        //Manel p = new Manel(Person.get(id));
         Answer p = new Answer();
        p.setId(id);
        p.setAnswer(answer);
        p.setCorrect(correct);
       return p;

   }


    public Question getQuestion() {
        // select * from Question as a inner join Answer as b on a.id = b.question_id where b.id = this.id;

        String query = String.format("select a.* from Question as a inner join Answer as b on a.id = b.question_id where b.id ="+ this.id);
        ResultSet result = con.executeQuery(query);

        try {
            while (result.next()) {

                Question p = (Question) Question.getQuestionClass(result);
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void setQuestion(Question question) {

        String query = String.format("Update %s set question_id = '%d'  where id = %d", "Answer", question != null ? question.getId() : -1, this.id);
        con.executeUpdate(query);

    }



    @Override
    public String toString() {
        return "Answer{" +
                " answer=" + answer +
                " correct=" + correct +
                '}';
    }

}
