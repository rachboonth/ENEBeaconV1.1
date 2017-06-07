//Version 2.2 @Rachasit 13/4/17 *Changing file path for Unrooted android devices
//Version 2.0 @Rachasit 12/4/17 *Adding Beacon HW setting
package com.RachasitbGmailCom.RachasitbGmailComSProxFzl;

import android.support.v7.app.AppCompatActivity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent i = new Intent(this, ScanActivity.class);
//        startActivity(i);
    }

    public void openEstimote(View view){
        //Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vogella.com"));
        Intent i = new Intent(this, ScanActivity.class);
        startActivity(i);
    }

    public void openEstimoteV2(View view){
        //Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vogella.com"));
        Intent i = new Intent(this, ScanActivityV2.class);
        startActivity(i);
    }

    public void openProximi(View view){
        //Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vogella.com"));
        Intent i = new Intent(this, ProximiActivity.class);
        startActivity(i);
    }
    public void openBeaconSetting(View view){
        //Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vogella.com"));
        Intent i = new Intent(this, BeaconConfigure.class);
        startActivity(i);
    }


}
