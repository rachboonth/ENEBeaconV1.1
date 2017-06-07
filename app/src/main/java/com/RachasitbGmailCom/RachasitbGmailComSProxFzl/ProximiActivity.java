package com.RachasitbGmailCom.RachasitbGmailComSProxFzl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import io.proximi.proximiiolibrary.Proximiio;
import io.proximi.proximiiolibrary.ProximiioFactory;
import io.proximi.proximiiolibrary.ProximiioFloor;
import io.proximi.proximiiolibrary.ProximiioGeofence;
import io.proximi.proximiiolibrary.ProximiioGoogleMapHelper;
import io.proximi.proximiiolibrary.ProximiioInput;
import io.proximi.proximiiolibrary.ProximiioListener;

public class ProximiActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Proximiio proximiio;
    public ProximiioInput proxInput;
    private ProximiioListener listener;
    @Nullable private ProximiioGoogleMapHelper mapHelper;
    private Toolbar toolbar;

    private final static String TAG = "ProximiioDemo";
    //String myPPP = getResources().getString(R.string.myPass);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximi);

        // Create a Proximiio instance
        proximiio = ProximiioFactory.getProximiio(this);

        // Create a ProximiioListener and add it to Proximiio
        listener = new ProximiioListener() {
            @Override
            public void geofenceEnter(ProximiioGeofence geofence) {
                Log.d(TAG, "Geofence enter: " + geofence.getName());
            }

            @Override
            public void geofenceExit(ProximiioGeofence geofence, @Nullable Long dwellTime) {
                Log.d(TAG, "Geofence exit: " + geofence.getName() + ", dwell time: " + String.valueOf(dwellTime));
            }

            @Override
            public void loginFailed(LoginError loginError) {
                Log.e(TAG, "LoginError! (" + loginError.toString() + ")");
            }
        };

        proximiio.setActivity(this);
        proximiio.addListener(listener);

        // Login to Proximi.io

        proximiio.setLogin("mzhen71@gmail.com", getResources().getString(R.string.myPass));

        // Login to Proximi.io ALTERNATIVELY via token!
        // proximiio.setAuth("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImlzcyI6ImQ1NzMzYWIxLWYxMjAtNDk0MS04ZjkyLTUzN2U3N2FmOGIzMyIsInR5cGUiOiJhcHBsaWNhdGlvbiIsImFwcGxpY2F0aW9uX2lkIjoiNmJiYzYxZWEtZjZhZS00ZTkxLWI3YjAtYmNkMTQwZjcwZmQ4In0.41YFqxGNPYKZRy0M37xlSEzTKjvOyihupn25kRQhOBc");

        proximiio.checkPermissions();

        // Set toolbar buttons to change the current floor up and down
        findViewById(R.id.floorUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapHelper != null) {
                    mapHelper.floorUp();
                }
            }
        });
        findViewById(R.id.floorDown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapHelper != null) {
                    mapHelper.floorDown();
                }
            }
        });

        // Set the toolbar title
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("KMUTT Loc V2");

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ProximiioInput inputLat = null;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapHelper != null) {
            mapHelper.destroy();
        }
        proximiio.removeListener(listener);
        proximiio.removeActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        proximiio.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        proximiio.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Called when the map is ready to use
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapHelper = new ProximiioGoogleMapHelper.Builder(this, googleMap)
                //.heatMapColor(0x66ff9900)
                .initialPositioningZoom(25)
                .pathWidth(3)
                .pathEndDistance(5)
                .pathColor(Color.BLUE)
                .clusterMarkers(ProximiioGoogleMapHelper.ClusterState.USE_APPLICATION)
                .showFloorIndicator(true)
                .listener(new ProximiioGoogleMapHelper.Listener() {
                    @Override
                    public void changedFloor(@Nullable ProximiioFloor floor) {
                        toolbar.setTitle(floor != null ? floor.getName() : TAG);
                        Log.e(TAG, "LoginError! HHHHHHHHHHHHHHHHHHH");
                    }
                })
                .build();

        googleMap.setOnMyLocationButtonClickListener(mapHelper);
        googleMap.setOnMapClickListener(mapHelper);
        googleMap.setOnCameraIdleListener(mapHelper);
        googleMap.setOnMarkerClickListener(mapHelper);
        googleMap.setOnCameraMoveStartedListener(mapHelper);
    }
}



