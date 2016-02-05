package com.livestrong.tracker.googlefitmodule.models;

import com.livestrong.tracker.googlefitmodule.greendao.FitnessData;
import com.livestrong.tracker.googlefitmodule.Interfaces.ModelInterface;
import com.livestrong.tracker.googlefitmodule.Interfaces.OnCardDataReady;
import com.livestrong.tracker.googlefitmodule.main.LSGoogleFitDatabaseConnection;

import java.util.Date;

/**
 * Created by shambhavipunja on 2/4/16.
 */
public class GoogleFitModel implements ModelInterface {

    public GoogleFitModel(){
    }

    @Override
    public void getDataForDate(Date date, OnCardDataReady onCardDataReady) {
        FitnessData fitnessData = new FitnessData();
        LSGoogleFitDatabaseConnection dbConn = LSGoogleFitDatabaseConnection.getInstance();
        fitnessData = dbConn.getRowByDate(date);
        onCardDataReady.setFitnessData(fitnessData);

    }
}
