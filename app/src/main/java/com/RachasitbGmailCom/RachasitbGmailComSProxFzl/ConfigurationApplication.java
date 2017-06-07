package com.RachasitbGmailCom.RachasitbGmailComSProxFzl;

import android.app.Application;
import android.util.Log;

import com.estimote.sdk.EstimoteSDK;

public class ConfigurationApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        EstimoteSDK.initialize(getApplicationContext(), "eb-ln2", "fbdf9efe5e378aed1c3bf5b01210d75d");
        EstimoteSDK.enableDebugLogging(false);
    }
}
