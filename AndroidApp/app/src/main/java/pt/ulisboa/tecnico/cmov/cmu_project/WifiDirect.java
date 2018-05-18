package pt.ulisboa.tecnico.cmov.cmu_project;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;

/**
 * Created by tiago on 18-May-18.
 */

public class WifiDirect implements SimWifiP2pManager.PeerListListener, SimWifiP2pManager.GroupInfoListener {


    private static WifiDirect instance;

    private Context context;
    private SimWifiP2pBroadcastReceiver mReceiver;
    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private Messenger mService = null;
    private boolean mBound = false;
    private boolean isGO = false;
    private ArrayList<SimWifiP2pDevice> devicesList = new ArrayList<>();
    private ArrayList<SimWifiP2pDevice> peerList = new ArrayList<>();
    private SimWifiP2pDevice myDevice = null;
    private String goIP;
    private Thread receiver;
    private static SenderService.ServiceHandler pool;

    private WifiDirect(Context context) {

        this.context = context;
        // initialize the WDSim API
        SimWifiP2pSocketManager.Init(context);

        // register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        mReceiver = new SimWifiP2pBroadcastReceiver(this);
        context.registerReceiver(mReceiver, filter);


        receiver = new Thread() {


            @Override
            public void run() {


                Log.d("MRECEIVER", "IncommingCommTask started (" + this.hashCode() +
                        ").");
                SimWifiP2pSocketServer mSrvSocket = null;
                try {
                    mSrvSocket = new SimWifiP2pSocketServer(10001);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (pool != null) {
                    synchronized (pool) {
                        pool.notify();
                    }
                }

                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        synchronized (this) {
                            try {
                                this.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        SimWifiP2pSocket sock = mSrvSocket.accept();
                        try {
                            BufferedReader sockIn = new BufferedReader(
                                    new InputStreamReader(sock.getInputStream()));
                            String st = sockIn.readLine();
                            this.onProgressUpdate(st);
                            processMessage(st);
                            sock.getOutputStream().write(("\n").getBytes());
                        } catch (IOException e) {
                            Log.d("Error reading socket:", e.getMessage());
                        } finally {
                            sock.close();
                        }
                    } catch (IOException e) {
                        Log.d("Error socket:", e.getMessage());
                        break;
                    }
                }
            }

            public void onProgressUpdate(String... values) {
                System.out.println(values);
            }
        };

        //receiver.start();

        Intent intent = new Intent(getContext(), SimWifiP2pService.class);
        getContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


    }

    public Context getContext() {
        return context;

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void onDestroy() {
        context.unregisterReceiver(mReceiver);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        // callbacks for service binding, passed to bindService()

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            mManager = new SimWifiP2pManager(mService);
            mChannel = mManager.initialize(getContext(), getContext().getMainLooper(), null);
            mBound = true;
            mManager.requestGroupInfo(mChannel, WifiDirect.this);
            mManager.requestPeers(mChannel, WifiDirect.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
            mManager = null;
            mChannel = null;
            mBound = false;
        }
    };


    public static WifiDirect getInstance(Context context) {
        if (instance == null) {
            instance = new WifiDirect(context);
        }

        return instance;
    }

    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList devices, SimWifiP2pInfo groupInfo) {

        // compile list of network members
        StringBuilder peersStr = new StringBuilder();
        this.devicesList.clear();
        for (String deviceName : groupInfo.getDevicesInNetwork()) {
            SimWifiP2pDevice device = devices.getByName(deviceName);
            this.devicesList.add(device);

            String devstr = "" + deviceName + " (" +
                    ((device == null) ? "??" : device.getVirtIp()) + ")\n";
            peersStr.append(devstr);
        }

        myDevice = devices.getByName(groupInfo.getDeviceName());

        System.out.println(peersStr);
        if (this.isGO) {
            this.pingOwner();
        }
    }

    private void pingOwner() {

        System.out.println("ENTER");

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                if (myDevice == null) return;

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("type", "pingGO");
                    jsonObject.put("owner", myDevice.getVirtIp());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                for (SimWifiP2pDevice device : devicesList) {
                    try {
                        SimWifiP2pSocket mCliSocket = new SimWifiP2pSocket(device.getVirtIp(), 10001);
                        mCliSocket.getOutputStream().write(
                                (jsonObject.toString() + "\n").getBytes());
                        BufferedReader sockIn = new BufferedReader(
                                new InputStreamReader(mCliSocket.getInputStream()));
                        sockIn.readLine();
                        mCliSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList peers) {
        StringBuilder peersStr = new StringBuilder();

        peerList.clear();
        // compile list of devices in range
        for (SimWifiP2pDevice device : peers.getDeviceList()) {
            SimWifiP2pDevice d = peers.getByName(device.deviceName);
            this.peerList.add(d);
            String devstr = "" + device.deviceName + " (" + device.getVirtIp() + ")\n";
            peersStr.append(devstr);
        }

        System.out.println(peersStr);
    }


    public void wifiOn() {
        mBound = true;

        if (receiver.getState() == Thread.State.NEW)
            receiver.start();
        else {
            synchronized (receiver) {
                receiver.notify();
            }
        }

    }

    public void wifiOff() {
        if (mBound) {
            mBound = false;
            receiver.interrupt();
        }
    }

    public boolean ismBound() {
        return mBound;
    }

    public SimWifiP2pManager getmManager() {
        return mManager;
    }

    public SimWifiP2pManager.Channel getmChannel() {
        return mChannel;
    }

    public boolean isGO() {
        return isGO;
    }

    public void setGO(boolean GO) {
        isGO = GO;
    }

    public void processMessage(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);

            switch (jsonObject.getString("type")) {
                case "pingGO":
                    this.goIP = jsonObject.getString("owner");
                    break;

                case "server":
                    if (this.isGO) {
                        this.redirect(jsonObject);
                    }
                    break;

                case "serverResponse":
                    this.receiveServerResponse(jsonObject);
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void receiveServerResponse(JSONObject jsonObject) {
        System.out.println(jsonObject);


        JSONArray response = null;
        try {
            response = jsonObject.getJSONObject("data").getJSONArray("answers");

            for (int i = 0; i < response.length(); i++) {
                try {

                    int id = Integer.parseInt(response.getString(i));
                    DatabaseHelper.getInstance(context).updateAnswerPoolAck(id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            response = jsonObject.getJSONObject("data").getJSONArray("events");

            for (int i = 0; i < response.length(); i++) {
                try {

                    int id = Integer.parseInt(response.getString(i));
                    DatabaseHelper.getInstance(context).updateEventPoolAck(id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void sendServerResponse(final JSONObject jsonObject, final JSONObject response) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {


                try {
                    SimWifiP2pSocket mCliSocket = new SimWifiP2pSocket(jsonObject.getString("sender"), 10001);
                    mCliSocket.getOutputStream().write(
                            (response.toString() + "\n").getBytes());
                    BufferedReader sockIn = new BufferedReader(
                            new InputStreamReader(mCliSocket.getInputStream()));
                    sockIn.readLine();
                    mCliSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void redirect(final JSONObject jsonObject) {
        System.out.println("SEND TO SERVER");


        Gson gson = new Gson();
        System.out.println(jsonObject);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLS.URL_POST_SERVER_POOL, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());

                        sendServerResponse(jsonObject, response);


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                //Toast.makeText(getApplicationContext(), R.string.server_connection_error, Toast.LENGTH_SHORT).show();
            }

        });

        VolleySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);


    }


    public void sendServer(final JSONObject jsonObject) {

        System.out.println(jsonObject);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {


                try {
                    SimWifiP2pSocket mCliSocket = new SimWifiP2pSocket(goIP, 10001);
                    mCliSocket.getOutputStream().write(
                            (jsonObject.toString() + "\n").getBytes());
                    BufferedReader sockIn = new BufferedReader(
                            new InputStreamReader(mCliSocket.getInputStream()));
                    sockIn.readLine();
                    mCliSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public static SenderService.ServiceHandler getPool() {
        return pool;
    }

    public static void setPool(SenderService.ServiceHandler pool) {
        WifiDirect.pool = pool;
    }

    public ArrayList<SimWifiP2pDevice> getDevicesList() {
        return devicesList;
    }

    public ArrayList<SimWifiP2pDevice> getPeerList() {
        return peerList;
    }

    public void setPeerList(ArrayList<SimWifiP2pDevice> peerList) {
        this.peerList = peerList;
    }

    public String getMyIp() {
        if (this.myDevice != null)
            return this.myDevice.getVirtIp().split(":")[0];
        else
            return "";
    }

}
