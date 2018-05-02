package pt.ulisboa.tecnico.cmov.cmu_project;

import com.android.volley.toolbox.JsonObjectRequest;

import java.util.ArrayList;

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

        while (this.requests.size() > 0) {
            JsonObjectRequest e = this.requests.remove(0);
            volleySingleton.getRequestQueue().add(e);
            System.out.println("Sending request");
        }

    }

}
