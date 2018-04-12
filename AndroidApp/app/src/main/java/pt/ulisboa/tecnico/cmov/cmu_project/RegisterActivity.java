package pt.ulisboa.tecnico.cmov.cmu_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.sql.SQLOutput;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_register);
    }

    public void registerOnClick(View v) throws IOException, JSONException {

        EditText usrName = findViewById(R.id.txtRegister);
        String userName = usrName.getText().toString();
        EditText ticketNum = findViewById(R.id.txtTicketNum);
        String ticketNumber = ticketNum.getText().toString();

        if (ticketNum.length() > 1 && userName.length() > 1) {
            int randInt = (new Random()).nextInt();
            registerPost(userName, ticketNumber, randInt);
        } else {
            Toast.makeText(this, R.string.invalid_register, Toast.LENGTH_LONG).show();
        }

    }

    private void registerPost(String userName, String ticketNumber, int randInt) throws IOException, JSONException {

        JSONObject postParams = new JSONObject();

        postParams.put("username", userName);
        postParams.put("ticket", ticketNumber);
        postParams.put("random", randInt);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, URLS.URL_REGISTER, postParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("token").equals(""))
                        Toast.makeText(getBaseContext(), response.getString("error"), Toast.LENGTH_LONG).show();
                    else {
                        String token = response.getString("token");
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("wqq");
                        error.printStackTrace();

                    }
                });

        Volley.newRequestQueue(this).add(jsonObjReq);
    }
}
