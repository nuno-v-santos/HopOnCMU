package EndPoints;

import Model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import static spark.Spark.*;

public class AndroidEndPoints {

    private static User validateUser(String token) {
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

            //obter o token
            String token = request.headers("token");
            User user = validateUser(token);

            if (user == null) return false;

            Gson gson = new Gson();

            String result = gson.toJson(user.getTicket().getTour().getMonuments());
            return result;

        }));


        get("/android/userInfo/", ((request, response) -> {

            //obter o token
            String token = request.headers("token");
            User user = validateUser(token);

            if (user == null) return false;

            Gson gson = new Gson();

            String ticket = gson.toJson(user.getTicket());
            String tour = gson.toJson(user.getTicket().getTour());

            JsonObject responsePrams = new JsonObject();

            return "" + '{' +
                    '"' + "tiket" + '"' + ':' + ticket + ',' +
                    '"' + "tour" + '"' + ':' + tour +
                    '}';

        }));


    }
}
