package com.RachasitbGmailCom.RachasitbGmailComSProxFzl.estimote;

public interface BeaconContentFactory {

    void getContent(BeaconID beaconID, Callback callback);

    interface Callback {
        void onContentReady(Object content);
    }
}
