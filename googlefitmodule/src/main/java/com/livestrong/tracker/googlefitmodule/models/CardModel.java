package com.livestrong.tracker.googlefitmodule.models;

import com.livestrong.tracker.googlefitmodule.greendao.FitnessData;
import com.livestrong.tracker.googlefitmodule.Interfaces.CardModelInterface;
import com.livestrong.tracker.googlefitmodule.Interfaces.OnCardDataReady;

import java.util.Date;

/**
 * Created by shambhavipunja on 2/4/16.
 */
public class CardModel implements CardModelInterface {

    public CardModel(){
    }

    @Override
    public void getDataForDate(Date date, OnCardDataReady onCardDataReady) {
        FitnessData fitnessData = new FitnessData();
        fitnessData.setDate(date);
        onCardDataReady.setFitnessData(fitnessData);

    }
}
