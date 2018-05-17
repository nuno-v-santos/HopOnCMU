package pt.ulisboa.tecnico.cmov.cmu_project.Termite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.Channel;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.PeerListListener;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.ulisboa.tecnico.cmov.cmu_project.R;

public class PeerScannerActivity extends Activity implements
        PeerListListener {

    public static final String TAG = "peerscanner";

    private SimWifiP2pManager mManager = null;
    private Channel mChannel = null;
    private SimWifiP2pBroadcastReceiver mReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// register broadcast receiver
		IntentFilter filter = new IntentFilter();
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
		mReceiver = new SimWifiP2pBroadcastReceiver(this);
		registerReceiver(mReceiver, filter);

		Intent intent = new Intent(getApplicationContext(), SimWifiP2pService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

	private ServiceConnection mConnection = new ServiceConnection() {
		// callbacks for service binding, passed to bindService()

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			mManager = new SimWifiP2pManager(new Messenger(service));
            mChannel = mManager.initialize(getApplication(), getMainLooper(), null);
			mManager.requestPeers(mChannel, PeerScannerActivity.this);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.d("c","c");
			mManager = null;
            mChannel = null;
		}
	};

	/*
	 * Termite listeners
	 */
	
	@Override
	public void onPeersAvailable(SimWifiP2pDeviceList peers) {
		StringBuilder peersStr = new StringBuilder();
		
		// compile list of devices in range
		for (SimWifiP2pDevice device : peers.getDeviceList()) {
			String devstr = device.deviceName + ",";
			peersStr.append(devstr);
		}

		//returns the scanned peers
		Intent returnIntent = new Intent();
		returnIntent.putExtra("peers",peersStr.toString());
		setResult(Activity.RESULT_OK,returnIntent);
		finish();
	}
}
