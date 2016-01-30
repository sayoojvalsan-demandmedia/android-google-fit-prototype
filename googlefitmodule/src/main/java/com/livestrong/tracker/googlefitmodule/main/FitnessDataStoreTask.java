package com.livestrong.tracker.googlefitmodule.main;

import android.util.Log;

import com.livestrong.tracker.googlefitmodule.model.FitnessData;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shambhavipunja on 1/29/16.
 */
public class FitnessDataStoreTask {
    public static final String TAG="FitnesDataStoreTask";
    public FitnessDataStoreTask(){

    }

    public void insertStepcount(HashMap<Date, Integer> stepMap){
        List<FitnessData> list = new ArrayList<FitnessData>();
        if(stepMap != null) {
            for (Map.Entry<Date,Integer> entry : stepMap.entrySet()) {
                //Storing into list of FitnessData from Hashmap
                FitnessData fit = new FitnessData();
                fit.setDate(entry.getKey());
                fit.setFitness_step_count(stepMap.get(entry.getValue()));
                list.add(fit);
            }
            //insert in db
            LSGoogleFitDatabaseManager.getinstance().insert(list);

            /* For Log purpose */
            List<FitnessData> dataList = LSGoogleFitDatabaseManager.getinstance().getAll();
            for(FitnessData fit: dataList){
                Log.i(TAG, fit.getId() + " " + fit.getDate() + " " + fit.getFitness_step_count() + " " + fit.getFitness_calorie_count() + " " + fit.getFItness_distance() + " " + fit.getFitness_time());
            }
        }
    }

}
