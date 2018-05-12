package pt.ulisboa.tecnico.cmov.cmu_project;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;


public class HelloService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            try {

                while (true) {
                    System.out.println("Checking if server is online");
                    if (isHostReachable()) {
                        System.out.println("PING AO SERVIDOR ");
                        UserAnswers u = UserAnswers.getInstance();
                        u.sendRequests(VolleySingleton.getInstance(getBaseContext()));
                        Toast.makeText(getBaseContext(), "sending requests", Toast.LENGTH_LONG).show();
                    }

                    Thread.sleep(5000); //sleep for 5 seconds.
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // Stop the service using the startId, so that we don't stop
                // the service in the middle of handling another job
                stopSelf(msg.arg1);
            }
        }


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
