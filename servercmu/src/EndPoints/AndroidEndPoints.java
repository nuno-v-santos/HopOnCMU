package EndPoints;

import Model.*;
import com.google.gson.*;

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

                String result = "" + '{' +
                        '"' + "ticket" + '"' + ':' + ticket + ',' +
                        '"' + "tour" + '"' + ':' + tour +
                        '}';

                response.header("INTEGRIDATE", Crypto.calculateHMAC(result));
                return result;

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

                String result = quizQuestions.toString();
                response.header("INTEGRIDATE", Crypto.calculateHMAC(result));
                return result;

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
                List<UserMonument> userMonuments = user.getUserMonuments();


                stringBuilder.append("[");
                //monuments

                for (Monument monument : monuments) {

                    Quiz quiz = monument.getQuiz();
                    List<UserMonument> ums = userMonuments.stream().filter(um -> um.getMon_id() == monument.getId()).collect(Collectors.toList());
                    UserMonument userMonument = ums.size() > 0 ? ums.get(0) : null;

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
                            "\"status\" : " + gson.toJson((userMonument != null ? userMonument.getStatus() : "Not Visited")) + "," +
                            "\"quizStatus\" : " + gson.toJson((userMonument != null ? userMonument.getQuizStatus() : "INITIAL")) + "," +
                            "\"responses\" : " + gson.toJson(thisMonumentQuizResponses) + "," +
                            "\"questions\" : " + (userMonument != null && !userMonument.getQuizStatus().equals("INITIAL") ? quizQuestions.toString() : "[]") +
                            "}");

                }
                stringBuilder.append("]");
                //end monuments
                String result = stringBuilder.toString();
                res.header("INTEGRIDATE", Crypto.calculateHMAC(result));
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });

        post("/android/sync/answers/", (req, res) -> {

            String token = req.headers("token");
            User user = validateUser(token);

            if (user == null)
                return "{" +
                        "\"error\" : " + "invalid user" +
                        "}";

            System.out.println(req.body());
            JsonElement body = new JsonParser().parse(req.body());
            JsonArray answers = body.getAsJsonArray();
            ArrayList<String> response = new ArrayList<>();
            Gson gson = new Gson();

            for (JsonElement a : answers) {
                response.add(a.getAsJsonObject().get("id").getAsString());
                if (QuizResponse.where("answer_id =" + a.getAsJsonObject().get("questionId").getAsString() + " and user_id=" + user.getId()).size() == 0) {
                    QuizResponse quizResponse = new QuizResponse();
                    quizResponse.setScore(a.getAsJsonObject().get("isCorrect").getAsBoolean() ? 100 + (30 - a.getAsJsonObject().get("time").getAsInt()) : 0);
                    quizResponse.setDate(new Date());
                    quizResponse.setCorrect(a.getAsJsonObject().get("isCorrect").getAsBoolean() ? 1 : 0);
                    quizResponse.save();
                    quizResponse.setQuestion(Question.get(a.getAsJsonObject().get("questionId").getAsInt()));
                    quizResponse.setQuiz(Question.get(a.getAsJsonObject().get("questionId").getAsInt()).getQuiz());
                    quizResponse.setUser(user);
                }
            }


            String result = gson.toJson(response);
            res.header("INTEGRIDATE", Crypto.calculateHMAC(result));
            return result;

        });

        post("/android/sync/events/", (req, res) -> {

            System.out.println(req.body());

            String token = req.headers("token");
            User user = validateUser(token);

            if (user == null)
                return "{" +
                        "\"error\" : " + "invalid user" +
                        "}";

            System.out.println(req.body());
            JsonElement body = new JsonParser().parse(req.body());
            JsonArray answers = body.getAsJsonArray();
            ArrayList<String> response = new ArrayList<>();
            Gson gson = new Gson();

            for (JsonElement a : answers) {
                response.add(a.getAsJsonObject().get("id").getAsString());

                List<UserMonument> userMonuments = UserMonument.where("user_id=" + user.getId() + " and mon_id=" + a.getAsJsonObject().get("munId").getAsString());

                if (userMonuments.size() == 0) {
                    UserMonument userMonument = new UserMonument();
                    userMonument.setUser_id(user.getId());
                    userMonument.setMon_id(a.getAsJsonObject().get("munId").getAsInt());
                    if (a.getAsJsonObject().get("type").getAsString().equals("status"))
                        userMonument.setStatus(a.getAsJsonObject().get("value").getAsString());
                    else
                        userMonument.setQuizStatus(a.getAsJsonObject().get("value").getAsString());
                    userMonument.save();
                } else {
                    UserMonument userMonument = userMonuments.get(0);
                    if (a.getAsJsonObject().get("type").getAsString().equals("status"))
                        userMonument.setStatus(a.getAsJsonObject().get("value").getAsString());
                    else
                        userMonument.setQuizStatus(a.getAsJsonObject().get("value").getAsString());
                    userMonument.save();
                }


            }


            String result = gson.toJson(response);
            res.header("INTEGRIDATE", Crypto.calculateHMAC(result));
            return result;


        });

        post("/android/sync/server/", (req, res) -> {

            try {
                System.out.println(req.body());

                JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
                User user = User.get(body.get("id").getAsInt());

                if (user != null) {

                    String decrypt = Crypto.decrypt(Crypto.hexStringToByteArray(body.get("data").getAsString()), user.getSession().getToken().substring(0, 32));
                    if (!Crypto.calculateHMAC(decrypt).equals(body.get("INTEGRIDADE").getAsString())) {
                        return false;
                    }
                    System.out.println("DECRYPT");
                    System.out.println(decrypt);

                    JsonObject jsonObject = new JsonParser().parse(decrypt).getAsJsonObject();

                    String message = "{" +
                            "\"answers\" : " + processAnswers(jsonObject.get("answers").getAsJsonArray(), body.get("id").getAsInt()) + "," +
                            "\"events\" : " + processEvents(jsonObject.get("events").getAsJsonArray(), body.get("id").getAsInt()) +
                            "}";

                    return "{" +
                            "\"type\" : " + "\"serverResponse\"," +
                            "\"INTEGRIDADE\" : " + "\"" + Crypto.calculateHMAC(message) + "\"," +
                            "\"test\" : " + "\"" + user.getSession().getToken().substring(0, 32) + "\"," +
                            "\"data\" : " + "\"" + Crypto.encrypt(message, user.getSession().getToken().substring(0, 32)) + "\"" +
                            "}";

                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }

        });

    }

    private static String processAnswers(JsonArray answers, int userId) {

        ArrayList<String> response = new ArrayList<>();
        Gson gson = new Gson();

        for (JsonElement a : answers) {
            response.add(a.getAsJsonObject().get("id").getAsString());
            if (QuizResponse.where("answer_id =" + a.getAsJsonObject().get("questionId").getAsString() + " and user_id=" + userId).size() == 0) {
                QuizResponse quizResponse = new QuizResponse();
                quizResponse.setScore(a.getAsJsonObject().get("isCorrect").getAsBoolean() ? 100 + (30 - a.getAsJsonObject().get("time").getAsInt()) : 0);
                quizResponse.setDate(new Date());
                quizResponse.setCorrect(a.getAsJsonObject().get("isCorrect").getAsBoolean() ? 1 : 0);
                quizResponse.save();
                quizResponse.setQuestion(Question.get(a.getAsJsonObject().get("questionId").getAsInt()));
                quizResponse.setQuiz(Question.get(a.getAsJsonObject().get("questionId").getAsInt()).getQuiz());
                quizResponse.setUser(User.get(userId));
            }
        }


        return gson.toJson(response);

    }


    private static String processEvents(JsonArray events, int userId) {

        Gson gson = new Gson();
        ArrayList<String> response = new ArrayList<>();
        for (JsonElement a : events) {
            response.add(a.getAsJsonObject().get("id").getAsString());

            List<UserMonument> userMonuments = UserMonument.where("user_id=" + userId + " and mon_id=" + a.getAsJsonObject().get("munId").getAsString());

            if (userMonuments.size() == 0) {
                UserMonument userMonument = new UserMonument();
                userMonument.setUser_id(userId);
                userMonument.setMon_id(a.getAsJsonObject().get("munId").getAsInt());
                if (a.getAsJsonObject().get("type").getAsString().equals("status"))
                    userMonument.setStatus(a.getAsJsonObject().get("value").getAsString());
                else
                    userMonument.setQuizStatus(a.getAsJsonObject().get("value").getAsString());
                userMonument.save();
            } else {
                UserMonument userMonument = userMonuments.get(0);
                if (a.getAsJsonObject().get("type").getAsString().equals("status"))
                    userMonument.setStatus(a.getAsJsonObject().get("value").getAsString());
                else
                    userMonument.setQuizStatus(a.getAsJsonObject().get("value").getAsString());
                userMonument.save();
            }


        }


        return gson.toJson(response);
    }


}
