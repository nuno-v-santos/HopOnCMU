package EndPoints;

import Model.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class AndroidEndPoints {

    public static int VERSION = 0;

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
                boolean firs = true;

                for (Question question : questions) {
                    String answersString = gson.toJson(question.getAnswers());

                    if (firs) {
                        firs = false;
                    } else {
                        quizQuestions.append(",");
                    }

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
                        "\"error\" : " + "invalid user" +
                        "}";

            JsonElement body = new JsonParser().parse(req.body());
            System.out.println(body);
            String questionID = body.getAsJsonObject().get("questionID").getAsString();

            boolean alreadyAnswered = !QuizResponse.where("answer_id = " + questionID).isEmpty();

            if (alreadyAnswered) {
                return "{" +
                        "\"error\" : " + "Already Answered" +
                        "}";
            }


            String answerID = body.getAsJsonObject().get("answerID").getAsString();

            Answer answer = Answer.get(Integer.parseInt(answerID));
            Question question = Question.get(Integer.parseInt(questionID));

            QuizResponse quizResponse = new QuizResponse();
            quizResponse.setTime(0);
            quizResponse.setCorrect(answer.getCorrect());
            quizResponse.setDate(new Date());
            quizResponse.setScore(answer.getCorrect() * 100);
            quizResponse.save();
            quizResponse.setUser(user);
            quizResponse.setQuiz(question.getQuiz());
            quizResponse.setQuestion(question);

            return "{" +
                    "\"error\" : " + "empty" +
                    "}";

        });


        get("/android/version/", (req, res) -> {
            return VERSION;
        });

        get("/android/sync/", (req, res) -> {
            try {

                //obter o token
                String token = req.headers("token");
                User user = validateUser(token);
                if (user == null)
                    return false;


                Gson gson = new Gson();
                StringBuilder stringBuilder = new StringBuilder();
                List<Monument> monuments = user.getTicket().getTour().getMonuments();
                List<QuizResponse> quizResponses = user.getQuizResponses();


                stringBuilder.append("{");
                //monuments
                stringBuilder.append("\"monuments\":[");

                for (Monument monument : monuments) {

                    Quiz quiz = monument.getQuiz();

                    StringBuilder quizQuestions = new StringBuilder();
                    quizQuestions.append("[");

                    List<Question> questions = quiz.getQuestions();
                    boolean firs = true;

                    for (Question question : questions) {
                        String answersString = gson.toJson(question.getAnswers());

                        if (firs) {
                            firs = false;
                        } else {
                            quizQuestions.append(",");
                        }

                        quizQuestions.append(
                                "{" +
                                        "\"id\" : " + "\"" + question.getId() + "\"" + "," +
                                        "\"question\" : " + "\"" + question.getQuestion() + "\"" + "," +
                                        "\"answers\" : " + answersString +
                                        "}"
                        );

                    }


                    quizQuestions.append("]");
                    System.out.println(quizQuestions.toString());

                    List<QuizResponse> thisMonumentQuizResponses = quizResponses.stream().filter(quizResponse -> {
                        return quizResponse.getQuiz().getId() == monument.getQuiz().getId();
                    }).collect(Collectors.toList());

                    stringBuilder.append("{" +
                            "\"monument\" : " + gson.toJson(monument) + "," +
                            "\"activeQuiz\" : " + (thisMonumentQuizResponses.size() != monument.getQuiz().getQuestions().size()) + "," +
                            "\"responses\" : " + gson.toJson(thisMonumentQuizResponses) + "," +
                            "\"questions\" : " + quizQuestions.toString() +
                            "}");

                }
                stringBuilder.append("]");
                //end monuments
                stringBuilder.append("}");
                return stringBuilder.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });

    }


}
