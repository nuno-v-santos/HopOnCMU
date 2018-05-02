package EndPoints;

import Model.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

import static spark.Spark.*;

public class AndroidEndPoints {

    private static User validateUser(String token) {

        System.out.println("TOKEN");
        System.out.println(token);

        if (token == null) {
            return null;
        }

        //split do token
        String[] s = token.split("\\|");
        User user = User.get(Integer.parseInt(s[1]));

        if (user == null)
            return null;

        if (!user.getSession().getToken().equals(token))
            return null;

        return user;
    }

    public static void mount() {

        get("/android/monuments/", ((request, response) -> {

            System.out.println("ok");
            try {
                //obter o token
                String token = request.headers("token");
                User user = validateUser(token);

                if (user == null)
                    return false;

                Gson gson = new Gson();

                String result = gson.toJson(user.getTicket().getTour().getMonuments());
                System.out.println("Monuments =======> ok");
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }));


        get("/android/userInfo/", ((request, response) -> {

            try {

                //obter o token
                String token = request.headers("token");
                User user = validateUser(token);


                System.out.println(token);

                if (user == null)
                    return false;

                Gson gson = new Gson();

                String ticket = gson.toJson(user.getTicket());
                String tour = gson.toJson(user.getTicket().getTour());
                JsonObject responsePrams = new JsonObject();

                return "" + '{' +
                        '"' + "tiket" + '"' + ':' + ticket + ',' +
                        '"' + "tour" + '"' + ':' + tour +
                        '}';

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }));


        get("/android/monumentQuestions/", ((request, response) -> {

            try {

                //obter o token
                String token = request.headers("token");
                User user = validateUser(token);
                if (user == null)
                    return false;

                int monID = Integer.parseInt(request.headers("monumentID"));
                Gson gson = new Gson();
                Monument monument = Monument.get(monID);
                Quiz quiz = monument.getQuiz();


                StringBuilder quizQuestions = new StringBuilder();
                quizQuestions.append("[");

                List<Question> questions = quiz.getQuestions();

                for (Question question : questions) {
                    String answersString = gson.toJson(question.getAnswers());

                    quizQuestions.append(
                            "{" +
                                    "\"id\" : " + "\"" + question.getId() + "\"" + "," +
                                    "\"question\" : " + "\"" + question.getQuestion() + "\"" + "," +
                                    "\"answers\" : " + answersString +
                                    "}"
                    );
                }

                quizQuestions.append("]");

                return quizQuestions.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }));


        get("/android/ranking/", (req, res) -> {

            //obter o token
            String token = req.headers("token");
            User user = validateUser(token);
            if (user == null)
                return false;

            List<QuizResponse> quizResponses = QuizResponse.all();


            Object[] result = quizResponses.stream().filter(quizResponse -> {
                return (new Date().getDate() == quizResponse.getDate().getDate()) &&
                        (new Date().getMonth() == quizResponse.getDate().getMonth()) &&
                        (new Date().getYear() == quizResponse.getDate().getYear());

            }).toArray();


            HashMap<String, Integer> score = new HashMap<>();

            for (Object quizResponsee : result) {
                QuizResponse quizResponse = (QuizResponse) quizResponsee;
                if (score.containsKey(quizResponse.getUser().getUsername())) {
                    score.put(quizResponse.getUser().getUsername(), score.get(quizResponse.getUser().getUsername()) + quizResponse.getScore());
                } else {
                    score.put(quizResponse.getUser().getUsername(), quizResponse.getScore());
                }
            }

            Gson gson = new Gson();
            return gson.toJson(score);

        });


        post("/android/userAnswers/", (req, response) -> {

            //obter o token
            String token = req.headers("token");
            User user = validateUser(token);

            if (user == null)
                return "{" +
                        "\"error\" : " + "invalid user" + "," +
                        "}";


            JsonElement body = new JsonParser().parse(req.body());
            String questionID = body.getAsJsonObject().get("questionID").getAsString();
            String answerID = body.getAsJsonObject().get("answerID").getAsString();



            return "{" +
                    "\"error\" : " + "" + "," +
                    "}";


        });


    }


}
