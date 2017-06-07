package com.RachasitbGmailCom.RachasitbGmailComSProxFzl.estimote;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.RachasitbGmailCom.RachasitbGmailComSProxFzl.R;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.util.ArrayList;
import java.util.List;

public class NearestBeaconManager {


    private ProgressBar spinner;

    Context context;

    private static String z = "";

    private static final String TAG = "NearestBeaconManager";

    private static final Region ALL_ESTIMOTE_BEACONS = new Region("all Estimote beacons", null, null, null);

    private List<BeaconID> beaconIDs;

    private Listener listener;

    private BeaconID currentlyNearestBeaconID;
    private boolean firstEventSent = false;

    private BeaconManager beaconManager;

    public NearestBeaconManager(Context context, List<BeaconID> beaconIDs) {
        this.beaconIDs = beaconIDs;
        this.context = context;

        beaconManager = new BeaconManager(context);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                checkForNearestBeacon(list);
            }
        });
    }
    public void Update(){
        TextView txtView = (TextView) ((Activity)context).findViewById(R.id.rssi_text_view);
        txtView.setText("Hello");
    }
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onNearestBeaconChanged(BeaconID beaconID);
    }

    public void startNearestBeaconUpdates() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
            }
        });
    }

    public void stopNearestBeaconUpdates() {
        beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
    }

    public void destroy() {
        beaconManager.disconnect();
    }

    private void checkForNearestBeacon(List<Beacon> allBeacons) {
        List<Beacon> beaconsOfInterest = filterOutBeaconsByIDs(allBeacons, beaconIDs);
        Beacon nearestBeacon = findNearestBeacon(beaconsOfInterest);
        if (nearestBeacon != null) {
            BeaconID nearestBeaconID = BeaconID.fromBeacon(nearestBeacon);
            if (!nearestBeaconID.equals(currentlyNearestBeaconID) || !firstEventSent) {
                updateNearestBeacon(nearestBeaconID);
            }
        } else if (currentlyNearestBeaconID != null || !firstEventSent) {
            updateNearestBeacon(null);
        }
    }

    private void updateNearestBeacon(BeaconID beaconID) {
        currentlyNearestBeaconID = beaconID;
        firstEventSent = true;
        if (listener != null) {
            listener.onNearestBeaconChanged(beaconID);
        }
    }

    private  List<Beacon> filterOutBeaconsByIDs(List<Beacon> beacons, List<BeaconID> beaconIDs) {
        List<Beacon> filteredBeacons = new ArrayList<>();
        for (Beacon beacon : beacons) {
            BeaconID beaconID = BeaconID.fromBeacon(beacon);
            if (beaconIDs.contains(beaconID)) {
                filteredBeacons.add(beacon);
            }
        }
        return filteredBeacons;
    }

    private  Beacon findNearestBeacon(List<Beacon> beacons) {
        Beacon nearestBeacon = null;
        double nearestBeaconsDistance = -1;
        for (Beacon beacon : beacons) {
            double distance = Utils.computeAccuracy(beacon);
            if (distance > -1 &&
                    (distance < nearestBeaconsDistance || nearestBeacon == null)) {
                nearestBeacon = beacon;
                nearestBeaconsDistance = (double)Math.round(distance * 1000d) / 1000d ;
                z =  String.valueOf(nearestBeacon.getRssi());
                // myRSSI();

                //Bee Edit


                TextView txtView = (TextView) ((Activity)context).findViewById(R.id.rssi_text_view);
                txtView.setText("RSSI: " + nearestBeacon.getRssi() +"\nTxpower: " + nearestBeacon.getMeasuredPower()
                        + "\nDistance: " + nearestBeaconsDistance + "m");
                TextView txtViewHeader  = (TextView) ((Activity)context).findViewById(R.id.name_text_view);
                txtViewHeader.setVisibility(View.VISIBLE);
                txtViewHeader.setText("MAC: " + nearestBeacon.getMacAddress()+ "\nUUID: " + nearestBeacon.getProximityUUID()+
                        "\nMajor: " + nearestBeacon.getMajor() +"\t\t\tMinor:" + nearestBeacon.getMinor());
                //============================

                spinner = (ProgressBar)((Activity)context).findViewById(R.id.progressBar1);
                spinner.setVisibility(View.GONE);
            }
        }

        Log.d(TAG, "Nearest beacon: " + nearestBeacon + ", distance: " + nearestBeaconsDistance);
        return nearestBeacon;
    }
}
