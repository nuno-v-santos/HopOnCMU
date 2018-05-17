package pt.ulisboa.tecnico.cmov.cmu_project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkStateReceiver extends BroadcastReceiver {


    protected Boolean connected;
    private static SenderService.ServiceHandler currentPool;

    public NetworkStateReceiver() {
        connected = false;
    }

    public static void setCurrentPool(SenderService.ServiceHandler currentPool) {
        NetworkStateReceiver.currentPool = currentPool;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getExtras() == null)
            return;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = manager.getActiveNetworkInfo();

        if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
            synchronized (currentPool) {
                currentPool.notify();
            }
        } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            connected = false;
        }

    }


}

