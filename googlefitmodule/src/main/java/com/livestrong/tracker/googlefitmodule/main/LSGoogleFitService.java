package com.livestrong.tracker.googlefitmodule.main;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by shambhavipunja on 1/26/16.
 */
public class LSGoogleFitService extends IntentService {
    public static final String TAG = "Service";
    HashMap<Long,Integer> step_map;

    public LSGoogleFitService(){
        super("Service");
        step_map = null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "started");
        int scaleFactor = 2;
        int cpus = Runtime.getRuntime().availableProcessors();
        int maxThreads = cpus * scaleFactor;
        maxThreads = (maxThreads > 0 ? maxThreads : 1);

        ExecutorService executorService =
                new ThreadPoolExecutor(
                        maxThreads, // core thread pool size
                        maxThreads, // maximum thread pool size
                        1, // time to wait before resizing pool
                        TimeUnit.MINUTES,
                        new ArrayBlockingQueue<Runnable>(maxThreads, true),
                        new ThreadPoolExecutor.CallerRunsPolicy());



        executorService.submit(new LSGoogleFitStepCount(new StepCountListener() {
            @Override
            public void stepCountRetrieved(HashMap<Long, Integer> steps) {
                Log.i(TAG, "steps received");
                for (Long key:steps.keySet()) {
                    step_map.put(key,steps.get(key));
            }
            }
        })
        );

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                // pool didn't terminate after the first try
                executorService.shutdownNow();
            }

            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                // pool didn't terminate after the second try
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

    }


}

