package com.livestrong.tracker.googlefitmodule.main;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shambhavipunja on 1/25/16.
 */
public class LSGoogleFitManager {
    private static LSGoogleFitManager slsGoogleFitManager;
    private Context mcontext;
    private GoogleApiClient mClient;
    private LSGoogleFitConnectionListener mConnectionListener;
    private LSGoogleFitServiceReciever mreciever;
    private List<LSGoogleFitObserver> mobservers;
    private int mobserverState;
    public static final int LISTENER_SET = 1;
    public static final String TAG = "FitnessClient";
    public static final String GET_HISTORY = "FitnessHistory";


    private LSGoogleFitManager(Context context, LSGoogleFitConnectionListener connectionListener){
        this.mcontext = context.getApplicationContext();
        this.mConnectionListener = connectionListener;
        mobservers = new ArrayList<LSGoogleFitObserver>();
        buildFitnessClient(context);

    }

    public static synchronized LSGoogleFitManager initialize(Context context, LSGoogleFitConnectionListener errorlistener) {
        if(slsGoogleFitManager == null){
            slsGoogleFitManager = new LSGoogleFitManager(context, errorlistener);

        }
        return slsGoogleFitManager;
    }

    public Context getcontext() {
        return mcontext;
    }

    /**
     * Buil
     */
    public void buildFitnessClient(final Context context) {
        // Create the Google API Client
        mClient = new GoogleApiClient.Builder(context)
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
                .enableAutoManage((FragmentActivity) context, 0, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {

                        Log.i(TAG, "Google Play services connection failed. Cause: Exception while connecting to Google Play services" +
                                result.getErrorCode() + result.getErrorMessage() + result.toString());

                        notifyConnectionStatus(result.toString());
                        Toast.makeText(context, "failed", Toast.LENGTH_LONG).show();

                    }
                })

                .build();
    }

    //Method to disconnect from Google Fit Services
    public void disconnectGoogleFit(final Context context) {
        if(!mClient.isConnected()){
            Log.e(TAG, "Google Fit wasn't connected");
            return;
        }
        PendingResult<Status> pendingResult = Fitness.ConfigApi.disableFit(mClient);

        pendingResult.setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if (status.isSuccess()) {

                    Toast.makeText(context, "Google Fit disabled", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Google Fit disabled");

                } else {
                    Log.e(TAG, "Google Fit wasn't disabled " + status);
                }
            }
        });
    }

    public void startLSGoogleFitService(){
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
    public GoogleApiClient getClient() {
        return mClient;
    }

    public static LSGoogleFitManager getLsGoogleFitManager() {
        if(slsGoogleFitManager == null){
            throw new IllegalStateException("Not initialized");
        }
        return slsGoogleFitManager;
    }

    public void setState(int state) {
        this.mobserverState = state;
        notifyAllObservers();
    }

    public void attach(LSGoogleFitObserver observer){
        mobservers.add(observer);
        notifyObserver(observer);
    }

    public void detach(LSGoogleFitObserver observer){
        mobservers.remove(observer);
    }

    private void notifyAllObservers() {
        if(this.mobserverState == LISTENER_SET) {
            for (LSGoogleFitObserver observer : mobservers) {
                observer.onDatabaseUpdated();
            }
        }
    }

    private void notifyObserver(LSGoogleFitObserver observer) {
        if(this.mobserverState == LISTENER_SET) {
                observer.onDatabaseUpdated();
        }
    }

    public void registerLSReciever(String action){
        IntentFilter filter = new IntentFilter(action);
        mreciever = new LSGoogleFitServiceReciever();
        mcontext.registerReceiver(mreciever,filter);
    }

    public void unregisterLSReciever(){
        mcontext.unregisterReceiver(mreciever);
    }

    /*public void initializedb(){
        mdbmanager = LSGoogleFitDatabaseManager.getinstance();
    }*/

}




