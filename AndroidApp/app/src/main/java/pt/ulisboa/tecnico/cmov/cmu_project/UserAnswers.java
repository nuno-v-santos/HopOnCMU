package pt.ulisboa.tecnico.cmov.cmu_project;

import com.android.volley.toolbox.JsonObjectRequest;

import java.util.ArrayList;
import java.util.Iterator;

public class UserAnswers {

    public static UserAnswers mInstance = null;
    private ArrayList<JsonObjectRequest> requests;

    private UserAnswers() {
        this.requests = new ArrayList<>();
    }

    public static UserAnswers getInstance() {

        if (mInstance == null)
            mInstance = new UserAnswers();

        return mInstance;
    }


    public void addJsonRequest(JsonObjectRequest e) {
        this.requests.add(e);
    }


    public void sendRequests(VolleySingleton volleySingleton) {

        for (JsonObjectRequest e : this.requests) {
            volleySingleton.getRequestQueue().add(e);
            System.out.println("---Sending Pending Requests---");

        }

    }

}
