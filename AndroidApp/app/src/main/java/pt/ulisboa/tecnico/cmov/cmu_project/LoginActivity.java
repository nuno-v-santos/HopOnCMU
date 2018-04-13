package pt.ulisboa.tecnico.cmov.cmu_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private Random rand = new Random();
    public static String SESSION_TOKEN = "token";

    final private static String RANDOM = "random";
    final private static String USERNAME = "username";
    final private static String TICKET = "ticket";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_login);
    }

    public void register(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Method that responds to the event of the click in the login event
     *
     * @param v
     * @throws IOException
     * @throws JSONException
     */
    public void login(View v) throws IOException, JSONException {

        EditText usrName = findViewById(R.id.txtLogin);
        final String userName = usrName.getText().toString();
        EditText ticketNum = findViewById(R.id.txtTicketNum);
        final String ticketNumber = ticketNum.getText().toString();

        if (ticketNum.length() > 0 && userName.length() > 0) {
            int randInt = this.rand.nextInt();
            loginPost(userName, ticketNumber, randInt);
            SharedPreferences sharedPref = this.getPreferences(this.MODE_PRIVATE);

            String sessionToken = sharedPref.getString(LoginActivity.this.SESSION_TOKEN, null);
            if (sessionToken != null) {

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

        } else {

            Toast.makeText(this, R.string.invalid_login, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Function that sends to the server the username and ticketNumber
     *
     * @param userName     user username
     * @param ticketNumber user ticket number
     * @param randInt      a random int for security purposes
     * @throws IOException
     * @throws JSONException
     */
    private void loginPost(final String userName, final String ticketNumber, int randInt) throws IOException, JSONException {

        JSONObject postParams = new JSONObject();

        postParams.put(LoginActivity.USERNAME, "nfsnkdfdskfdkj");
        postParams.put(LoginActivity.TICKET, 9876789);
        postParams.put(LoginActivity.RANDOM, randInt);

        final SharedPreferences sharedPref = getPreferences(this.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, URLS.URL_LOGIN, postParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String token = response.getString(LoginActivity.SESSION_TOKEN);
                    editor.putString(LoginActivity.SESSION_TOKEN, token);
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });

        Volley.newRequestQueue(this).add(jsonObjReq);
    }
}







