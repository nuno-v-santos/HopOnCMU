package pt.ulisboa.tecnico.cmov.cmu_project;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.cmu_project.Monument.MonumentData;
import pt.ulisboa.tecnico.cmov.cmu_project.Monument.MonumentScreenActivity;
import pt.ulisboa.tecnico.cmov.cmu_project.Quiz.QuizAnswer;
import pt.ulisboa.tecnico.cmov.cmu_project.Quiz.QuizEvent;


public class SenderService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    public final class ServiceHandler extends Handler {

        private DatabaseHelper db;

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            db = DatabaseHelper.getInstance(getApplicationContext());
            db.setCurrentPool(this);
            NetworkStateReceiver.setCurrentPool(this);

            try {

                while (true) {

                    if (isHostReachable()) {
                        List<QuizAnswer> answers = db.getPoolQuizAnswers();
                        List<QuizEvent> events = db.getEventPool();
                        try {
                            postEvents(events);
                            postAnswers(answers);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println(answers);
                        System.out.println(events);
                    }

                    synchronized (this) {
                        this.wait();
                        Toast.makeText(getApplicationContext(), "SOMEONE WAKED UP SERVICE", Toast.LENGTH_LONG);
                    }

                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // Stop the service using the startId, so that we don't stop
                // the service in the middle of handling another job
                stopSelf(msg.arg1);
            }
        }

        private void postEvents(List<QuizEvent> events) throws JSONException {

            SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_TOKEN, getApplicationContext().MODE_PRIVATE);
            final String token = sharedPreferences.getString(LoginActivity.SESSION_TOKEN, "");
            Gson gson = new Gson();

            if (!token.equals("")) {
                JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST, URLS.URL_POST_EVENTS_POOL, new JSONArray(gson.toJson(events)), new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                int id = Integer.parseInt(response.getString(i));
                                db.updateEventPoolAck(id);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), R.string.server_connection_error, Toast.LENGTH_SHORT).show();
                    }

                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("token", token);
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

                VolleySingleton.getInstance(getBaseContext()).getRequestQueue().add(jsonObjectRequest);
            }

        }

        private void postAnswers(List<QuizAnswer> answers) throws JSONException {


            SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_TOKEN, getApplicationContext().MODE_PRIVATE);
            final String token = sharedPreferences.getString(LoginActivity.SESSION_TOKEN, "");
            Gson gson = new Gson();


            if (!token.equals("")) {
                JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST, URLS.URL_POST_ANSWERS_POOL, new JSONArray(gson.toJson(answers)),
                        new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                System.out.println(response.toString());

                                for (int i = 0; i < response.length(); i++) {
                                    try {

                                        int id = Integer.parseInt(response.getString(i));
                                        db.updateAnswerPoolAck(id);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), R.string.server_connection_error, Toast.LENGTH_SHORT).show();
                    }

                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("token", token);
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

                VolleySingleton.getInstance(getBaseContext()).getRequestQueue().add(jsonObjectRequest);
            }

        }

        /**
         * Function that checks if the server is available or not
         *
         * @return True if available, false otherwise
         */
        private boolean isHostReachable() {
            try {
                InetAddress.getByName(URLS.SERVER_IP).isReachable(3000);
                return true;
            } catch (Exception e) {
                return false;
            }
        }


    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}
