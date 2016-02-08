package com.livestrong.tracker.googlefitmodule.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by shambhavipunja on 2/3/16.
 */
public class LSGoogleFitServiceReciever extends BroadcastReceiver {
    public static final String ACTION_RESP =
            "com.intent.action.SERVICE_DONE";
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(ACTION_RESP)) {
            LSGoogleFitManager manager = LSGoogleFitManager.getLsGoogleFitManager();
            manager.setmFlagService(LSGoogleFitManager.SERVICE_STOPPED);
            manager.setState(LSGoogleFitManager.LISTENER_SET);
        }
    }
}
