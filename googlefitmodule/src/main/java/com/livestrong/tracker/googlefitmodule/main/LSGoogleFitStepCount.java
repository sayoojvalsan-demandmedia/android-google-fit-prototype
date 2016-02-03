package com.livestrong.tracker.googlefitmodule.main;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by shambhavipunja on 1/26/16.
 */
public class LSGoogleFitStepCount implements Runnable {
    public static String TAG = "Fitness-Steps";
    private StepCountListener mstepCountListener;
    private HashMap<Date,Integer> mStepmap = new HashMap<Date,Integer>();
    public SimpleDateFormat dateFormat = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

    public LSGoogleFitStepCount(StepCountListener listener) {
        mstepCountListener = listener;
    }

    @Override
    public void run() {
        Log.i(TAG,"STEPS");

        DataReadRequest readRequest = queryFitnessData();
        GoogleApiClient client = LSGoogleFitManager.getLsGoogleFitManager().getClient();
        DataReadResult dataReadResult =
                Fitness.HistoryApi.readData(client, readRequest).await(1, TimeUnit.MINUTES);
        getStepData(dataReadResult);

    }

    /**
     *
     * @return The readRequest of type DataReadRequest
     */
    private DataReadRequest queryFitnessData() {
        long endTime = getEndTime();
        long startTime = getStartTime();

        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        return readRequest;
    }

    private void getStepData(DataReadResult dataReadResult) {
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: "
                    + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataPoint(dataSet);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            /*Log.i(TAG, "Number of returned DataSets is: "
                    + dataReadResult.getDataSets().size());*/
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataPoint(dataSet);
            }
        }

        Log.i(TAG,"HashMap");
        //Log purpose
        for(Map.Entry<Date,Integer> entry : mStepmap.entrySet()) {
            Log.i( TAG , dateFormat.format(entry.getKey()) + "----" + entry.getValue() );
        }

        notifyStepCountRetrieved(mStepmap);
    }

    /**
     *
     * @param dataSet
     * get Step data for each day
     */
    private void dumpDataPoint(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());

        for (DataPoint dp : dataSet.getDataPoints()) {

            //get date and set to 12:00 am
            Calendar cal = Calendar.getInstance(Locale.US);
            cal.setTimeInMillis(CalenderTimeConvertor.getmInstance().getTimeMidnight(dp.getStartTime(TimeUnit.MILLISECONDS)));
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date date  = cal.getTime();

            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());

            Log.i(TAG, dateFormat.format(cal.getTimeInMillis()));

            Log.i(TAG, "\tStartday: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));

            //store step count in hashmap.
            for (Field field : dp.getDataType().getFields()) {
                if(field.getName().equals("steps")){

                    mStepmap.put(date, dp.getValue(field).asInt());
                }
                Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
            }
        }
    }

    /**
     *
     * @return The current time in millis.
     */
    private long getEndTime() {
        Calendar cal = Calendar.getInstance(); //set end time now
        return cal.getTimeInMillis();
    }

    /**
     *
     * @return The last sync date or the date 1 month back in millis.
     */
    private long getStartTime(){
        Date lastSync = LSGoogleFitDatabaseConn.getinstance().getLastInsertDate();
        Log.i(TAG, "DATE***********: " + lastSync);

        if (lastSync != null) {
            //To get date last sync date for history
            return lastSync.getTime();
        }
        else
        {
            //When database is empty
            Calendar cal = Calendar.getInstance(Locale.US);
            cal.setTimeInMillis(CalenderTimeConvertor.getmInstance().getTimeMidnight(System.currentTimeMillis()));
            //cal.set(Calendar.HOUR_OF_DAY, 0);
            //cal.set(Calendar.MINUTE, 0);
            //cal.set(Calendar.SECOND, 00);
            //cal.set(Calendar.MILLISECOND, 0);
            cal.add(Calendar.DAY_OF_WEEK, -30); //retrieve data for the past 30 days.
            return cal.getTimeInMillis();
        }
    }

    public void notifyStepCountRetrieved(HashMap<Date,Integer> step_map){
        if (mstepCountListener != null){
            mstepCountListener.stepCountRetrieved(step_map);
        }
    }

    }

