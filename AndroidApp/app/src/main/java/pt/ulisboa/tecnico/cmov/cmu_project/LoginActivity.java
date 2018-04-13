package pt.ulisboa.tecnico.cmov.cmu_project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
    public final static String SHARED_PREF_TOKEN = "SHARED_PREF_TOKEN";
    public final static String SESSION_TOKEN = "token";

    private SharedPreferences.Editor editor ;

    final private static String RANDOM = "random";
    final private static String USERNAME = "username";
    final private static String TICKET = "ticket";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_login);
        this.editor =  getSharedPreferences(LoginActivity.SHARED_PREF_TOKEN, this.MODE_PRIVATE).edit();
        editor.remove(SESSION_TOKEN);
        editor.commit();
        setupParent(findViewById(R.id.relativeLayout));
    }

    protected void setupParent(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
                    return false;
                }
            });
        }
        //If a layout container, iterate over children
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupParent(innerView);
            }
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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
            Toast.makeText(this, R.string.short_login, Toast.LENGTH_LONG).show();
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

        postParams.put(LoginActivity.USERNAME, userName);
        postParams.put(LoginActivity.TICKET, ticketNumber);
        postParams.put(LoginActivity.RANDOM, randInt);
        postParams.put(LoginActivity.USERNAME, userName);
        postParams.put(LoginActivity.TICKET, Integer.parseInt(ticketNumber));
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

                    SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF_TOKEN, LoginActivity.this.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(LoginActivity.this.SESSION_TOKEN, response.getString(LoginActivity.this.SESSION_TOKEN));
                    editor.commit();


                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), R.string.server_connection_error, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getBaseContext(), R.string.server_connection_error, Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            });
        Volley.newRequestQueue(this).add(jsonObjReq);
    }
}







