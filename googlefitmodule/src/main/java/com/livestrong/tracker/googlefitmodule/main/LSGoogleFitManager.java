package com.livestrong.tracker.googlefitmodule.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;

/**
 * Created by shambhavipunja on 1/25/16.
 */
public class LSGoogleFitManager {
    private Context mcontext;
    private static GoogleApiClient mClient = null;
    private LSGoogleFitConnectionListener mConnectionListener;
    public static final String TAG = "FitnessClient";
    public static final String GET_HISTORY = "FitnessHistory";
    public static LSGoogleFitManager lsGoogleFitManager;

    private LSGoogleFitManager(Context context, LSGoogleFitConnectionListener connectionListener){
        this.mcontext = context;
        this.mConnectionListener = connectionListener;
        buildFitnessClient();
    }

    public static synchronized LSGoogleFitManager initialize(Context context, LSGoogleFitConnectionListener errorlistener) {
        if(lsGoogleFitManager == null){
            lsGoogleFitManager = new LSGoogleFitManager(context, errorlistener);
        }
        return lsGoogleFitManager;
    }

    public void buildFitnessClient() {
        // Create the Google API Client
        mClient = new GoogleApiClient.Builder(mcontext)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.CONFIG_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {

                            @Override
                            public void onConnected(Bundle bundle) {
                                Log.i(TAG, "Connected!!!");
                                notifyConnectionStatus("Connected");

                                // Subscribe to some data sources!
                                new LSGoogleFitRecord().subscribe(mClient);
                                startLSGoogleFitService();
                                //resultcode = LSGoogleFitManager.AUTH_SUCCESS;
                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                // If your connection to the sensor gets lost at some point,
                                // you'll be able to determine the reason and react to it here.
                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                    notifyConnectionStatus("Network Lost");
                                    Log.i(TAG, "******************Connection lost.  Cause: Network Lost.");
                                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                    notifyConnectionStatus("Service Disconnected");
                                    Log.i(TAG, "******************Connection lost.  Reason: Service Disconnected");
                                }
                            }
                        }
                )
                .enableAutoManage((FragmentActivity) mcontext, 0, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {

                        Log.i(TAG, "Google Play services connection failed. Cause: Exception while connecting to Google Play services" +
                                result.getErrorCode()+result.getErrorMessage()+result.toString());

                        notifyConnectionStatus(result.toString());
                        Toast.makeText(mcontext,"failed",Toast.LENGTH_LONG).show();

                    }
                })

                .build();
    }

    //Method to disconnect from Google Fit Services
    public void disconnectGoogleFit() {
        if(!mClient.isConnected()){
            Log.e(TAG, "Google Fit wasn't connected");
            return;
        }
        PendingResult<Status> pendingResult = Fitness.ConfigApi.disableFit(mClient);

        pendingResult.setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if(status.isSuccess()) {

                    Toast.makeText(mcontext,"Google Fit disabled",Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Google Fit disabled");

                }else{
                    Log.e(TAG, "Google Fit wasn't disabled " + status);
                }
            }
        });
    }

    void startLSGoogleFitService(){
        Intent intent = new Intent(mcontext, LSGoogleFitService.class);
        intent.setAction(GET_HISTORY);
        mcontext.startService(intent);

    }
    void notifyConnectionStatus(String status){
        if (mConnectionListener != null){
            mConnectionListener.ConnectionStatus(status);
        }
    }
    void notifySubscribeStatus(String status){
        if (mConnectionListener != null){
            mConnectionListener.SubscribeStatus(status);
        }
    }

    public static GoogleApiClient getmClient() {
        return mClient;
    }
}




