package com.RachasitbGmailCom.RachasitbGmailComSProxFzl;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.RachasitbGmailCom.RachasitbGmailComSProxFzl.estimote.BeaconID;
import com.RachasitbGmailCom.RachasitbGmailComSProxFzl.estimote.EstimoteCloudBeaconDetails;
import com.RachasitbGmailCom.RachasitbGmailComSProxFzl.estimote.EstimoteCloudBeaconDetailsFactory;
import com.RachasitbGmailCom.RachasitbGmailComSProxFzl.estimote.ProximityContentManager;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.cloud.model.Color;
import com.estimote.sdk.telemetry.EstimoteTelemetry;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class ScanActivityV2 extends AppCompatActivity {

    public  String h;
    public  String myBeaconDetail;
    private ProgressBar spinner;
    private List<BeaconID> beaconIDs;
    private BeaconManager beaconManager;
    private String scanId;


    private static final String TAG = "MainActivity";

    private static final Map<Color, Integer> BACKGROUND_COLORS = new HashMap<>();

    static {
        BACKGROUND_COLORS.put(Color.ICY_MARSHMALLOW, android.graphics.Color.rgb(109, 170, 199));
        BACKGROUND_COLORS.put(Color.BLUEBERRY_PIE, android.graphics.Color.rgb(98, 84, 158));
        BACKGROUND_COLORS.put(Color.MINT_COCKTAIL, android.graphics.Color.rgb(155, 186, 160));
        BACKGROUND_COLORS.put(Color.CANDY_FLOSS, android.graphics.Color.rgb(236, 190, 211));
        BACKGROUND_COLORS.put(Color.LEMON_TART, android.graphics.Color.rgb(234, 221, 30));
        BACKGROUND_COLORS.put(Color.SWEET_BEETROOT, android.graphics.Color.rgb(138, 44, 97));
    }

    private static final int BACKGROUND_COLOR_NEUTRAL = android.graphics.Color.rgb(160, 169, 172);

    private ProximityContentManager proximityContentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_v2);

        String appId = "alzbsinglebeac-ixa";
        String appToken = "5ce4c3c03d75a6404377c4b4941ba9c1";
        //String myUUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
        //EstimoteSDK.initialize(getApplicationContext(), "<#App ID#>", "<#App Token#>");
        EstimoteSDK.initialize(getApplicationContext(), appId, appToken);

        // Optional, debug logging.
        EstimoteSDK.enableDebugLogging(true);
        EstimoteSDK.enableMonitoringAnalytics(true);
        EstimoteSDK.enableRangingAnalytics(true);
        EstimoteSDK.enableGpsPositioningForAnalytics(true);
        EstimoteSDK.isRangingAnalyticsEnabled();
        EstimoteSDK.isMonitoringAnalyticsEnabled();
        //==========================

        proximityContentManager = new ProximityContentManager(this,
                Arrays.asList(
                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 5745, 3374),
                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 50346, 15506),
                        new BeaconID("2304FE47-57EC-AD6F-CB57-29778C342BDE", 52178, 33917)),
//                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 6709,58019),
//                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 36305,5104),
//                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 47698,51389)),
                new EstimoteCloudBeaconDetailsFactory() );
        proximityContentManager.setListener(new ProximityContentManager.Listener() {
            @Override
            public void onContentChanged(Object content) {
                String text;
                Integer backgroundColor;
                String bgColorTest;
                if (content != null) {
                    EstimoteCloudBeaconDetails beaconDetails = (EstimoteCloudBeaconDetails) content;
                    text = "You're in " + beaconDetails.getBeaconName() + "'s range!";
                    backgroundColor = BACKGROUND_COLORS.get(beaconDetails.getBeaconColor());
                    bgColorTest = beaconDetails.getBeaconColor().toString();
                    Log.e("COLORRRRRR",bgColorTest);
                } else {
                    text = "No beacons in range.";
                    backgroundColor = null;
                }
                ((TextView) findViewById(R.id.textView)).setText(text);
                findViewById(R.id.relativeLayout).setBackgroundColor(
                        backgroundColor != null ? backgroundColor : BACKGROUND_COLOR_NEUTRAL);
            }
        });

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        beaconManager = new BeaconManager(this);
        beaconManager.setTelemetryListener(new BeaconManager.TelemetryListener() {
            @Override
            public void onTelemetriesFound(List<EstimoteTelemetry> telemetries) {
                for (EstimoteTelemetry tlm : telemetries) {
                    Log.e("TELEMETRY", "beaconID: " + tlm.deviceId +
                            ", temperature: " + tlm.temperature + " °C\n" + tlm.batteryPercentage+ "\t\t\t" + tlm.ambientLight

                    );
                    TextView tlmView = (TextView) findViewById(R.id.tlm_text_view);
                    tlmView.setText("Temp: " + tlm.temperature + "\nBatt:"+ tlm.batteryPercentage +"\nLux:" + tlm.ambientLight);

                }

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            Log.e(TAG, "Can't scan for beacons, some pre-conditions were not met");
            Log.e(TAG, "Read more about what's required at: http://estimote.github.io/Android-SDK/JavaDocs/com/estimote/sdk/SystemRequirementsChecker.html");
            Log.e(TAG, "If this is fixable, you should see a popup on the app's screen right now, asking to enable what's necessary");
        } else {
            Log.d(TAG, "Starting ProximityContentManager content updates");
            proximityContentManager.startContentUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Stopping ProximityContentManager content updates");
        proximityContentManager.stopContentUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        proximityContentManager.destroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {

            @Override
            public void onServiceReady() {
                scanId = beaconManager.startTelemetryDiscovery();
                //Log.e("HELLOOOOOOOOOOOOOOOOO","BEE");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        beaconManager.stopTelemetryDiscovery(scanId);
    }

    public void startMyTimer(View view){

        final EditText inputText1 = (EditText)findViewById(R.id.myInputRange);
        final EditText inputFileName = (EditText)findViewById(R.id.myInputFileName);
        final String inputVal = inputText1.getText().toString();
        final String myFileName = inputFileName.getText().toString();
        final TextView mTextField = (TextView) findViewById(R.id.timerText);
        final TextView textT1 = (TextView) findViewById(R.id.scanningText);
        final TextView textT2 = (TextView) findViewById(R.id.timerText);
        final TextView textT3 = (TextView) findViewById(R.id.timerText2);
        final TextView textDis = (TextView) findViewById(R.id.rssi_text_view);
        final TextView startBut = (TextView) findViewById(R.id.button2);
        final TextView result = (TextView)  findViewById(R.id.scanningText);
        textT3.setVisibility(View.VISIBLE);
        textT2.setVisibility(View.VISIBLE);
        new CountDownTimer(10500, 500) {
            int count = 0;
            String[] myGetVal = new String[100];
            String tvtest1 = "" ;
            String tvtest2 = "" ;

            public void onTick(long millisUntilFinished) {
                String cutText = textDis.getText().toString();
                int index = cutText.indexOf("e:");
                String toBeReplaced = cutText.substring(index+3,cutText.length());
                String toBeReplaced2 = cutText.substring(index+3,cutText.length()-1);
                startBut.setVisibility(View.INVISIBLE);

                //===========Init================
                textT2.setText("" + (count+1));
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                textT1.setText("seconds remaining: " + millisUntilFinished / 1000);
                //NearestBeaconManager getMyVal = new NearestBeaconManager();
                count = count +1;
                myGetVal[1] = "HELLOOOOOO";
                tvtest1 = tvtest1 +count +"s:\t"+ toBeReplaced +"\n";
                tvtest2 = tvtest2 + toBeReplaced2 +",";
                Log.e("Analyzed",tvtest2);
                textT3.setText(tvtest1);

            }

            public void onFinish() {
                textT3.setText("");
                textT1.setText("SCANNING BEACON #1,3,5 Done!");
                startBut.setVisibility(View.VISIBLE);
                inputText1.setVisibility(View.VISIBLE);
                Log.e("Analyzed DONE!",inputVal +"m,"+tvtest2 + getCacheDir());
                String finalRangingVal = myBeaconDetail + ","+ inputVal +"m,"+tvtest2;


                writeToFile(finalRangingVal);
            }

            public void writeToFile(String data) {
                try {
                    h = DateFormat.format("MM-dd-yyyyy,h:mm", System.currentTimeMillis()).toString();
                    // this will create a new name everytime and unique
                     File root = new File("/sdcard", "kmuttFiles"); //##For Unroot Devices Path **RMK Enable Storage permission first!

                    //File root = new File(getCacheDir(), "beeFiles"); //## For Rooted Devices
                    // File root = new File(getExternalCacheDir(), "beeFile");
                    Log.e("FILEEEEEEEEEE", getExternalCacheDir().toString());
                    // if external memory exists and folder with name Notes
                    if (!root.exists()) {
                        root.mkdirs(); // this will create folder.
                    }
//                    root.mkdir();
//                    File filepath = new File(root, h + ".txt");  // file path to save
                    File filepath = new File(root, myFileName + ".txt");  // file path to save
                    FileWriter writer = new FileWriter(filepath,true); // #BEE- Set True for no overwriting
                    writer.append(data.toString());
                    writer.write(h);
                    writer.write("\n\n");
                    writer.flush();
                    writer.close();
                    String m = "File generated with name " + myFileName + ".txt";
                    result.setText(m);

                } catch (IOException e) {
                    e.printStackTrace();
                    result.setText(e.getMessage().toString());
                    textT3.setText(e.getMessage().toString()); // Clear Text
                }
            }
        }.start();
    }
}
