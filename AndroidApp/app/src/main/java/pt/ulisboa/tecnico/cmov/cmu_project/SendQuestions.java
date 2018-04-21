package pt.ulisboa.tecnico.cmov.cmu_project;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;

public class SendQuestions extends Service {

    private ArrayList<String> dataToSend;

    public SendQuestions() {
        this.dataToSend = new ArrayList<>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
