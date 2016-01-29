package com.livestrong.tracker.googlefitmodule.main;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by shambhavipunja on 1/26/16.
 */
public class LSGoogleFitStepCount implements Runnable {
    public static String TAG = "Fitness-Steps";
    private LSGoogleFitDatabaseConn lsGoogleFitDatabaseConn;
    private StepCountListener mstepCountListener;
    HashMap<Long,Integer> step_map = new HashMap<Long,Integer>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

    public LSGoogleFitStepCount(StepCountListener listener) {
        lsGoogleFitDatabaseConn = LSGoogleFitManager.getLsGoogleFitManager().getLsGoogleFitDatabaseConn();
        mstepCountListener = listener;
    }

    @Override
    public void run() {
        Log.i(TAG,"STEPS");
        DataReadRequest readRequest = queryFitnessData();
        GoogleApiClient client = LSGoogleFitManager.getLsGoogleFitManager().getClient();
        DataReadResult dataReadResult =
                Fitness.HistoryApi.readData(client, readRequest).await(1, TimeUnit.MINUTES);
        printData(dataReadResult);


    }

    private DataReadRequest queryFitnessData() {
        long endTime = getendTime();
        long startTime = getstartTime();

        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        return readRequest;
    }

    private void printData(DataReadResult dataReadResult) {
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: "
                    + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            /*Log.i(TAG, "Number of returned DataSets is: "
                    + dataReadResult.getDataSets().size());*/
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }
        Log.i(TAG,"HashMap");
        for (Long key:step_map.keySet()) {
            Log.i(TAG,dateFormat.format(key)+"----"+step_map.get(key));
        }
        notifyStepCountRetrieved(step_map);


    }

    private void dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());

        for (DataPoint dp : dataSet.getDataPoints()) {

            //get date and set to 12:00 am
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(dp.getStartTime(TimeUnit.MILLISECONDS));
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

           Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());

            Log.i(TAG, dateFormat.format(cal.getTimeInMillis()));

            Log.i(TAG, "\tStartday: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));

            for (Field field : dp.getDataType().getFields()) {
                if(field.getName().equals("steps")){
                    step_map.put(cal.getTimeInMillis(),dp.getValue(field).asInt());
                }
                Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
            }
        }
    }

    private long getendTime() {
        Calendar cal = Calendar.getInstance(); //set end time now
        return cal.getTimeInMillis();
    }

    private long getstartTime(){
        Date last_sync = lsGoogleFitDatabaseConn.GetLastInsertDate();
        Log.i(TAG, "DATE***********: " + last_sync);

        if (last_sync != null) {
            return last_sync.getTime();
        }
        else
        {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 00);
            cal.set(Calendar.MILLISECOND, 0);
            cal.add(Calendar.DAY_OF_WEEK, -6);
            return cal.getTimeInMillis();
        }
    }

    public void notifyStepCountRetrieved(HashMap<Long,Integer> step_map){
        if (mstepCountListener != null){
            mstepCountListener.stepCountRetrieved(step_map);
        }
    }

    }

