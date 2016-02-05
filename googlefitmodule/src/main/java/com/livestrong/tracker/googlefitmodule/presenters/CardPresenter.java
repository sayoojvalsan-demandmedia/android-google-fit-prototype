package com.livestrong.tracker.googlefitmodule.presenters;

import com.livestrong.tracker.googlefitmodule.Tasks.TimeConvertorTask;
import com.livestrong.tracker.googlefitmodule.greendao.FitnessData;
import com.livestrong.tracker.googlefitmodule.Interfaces.ModelInterface;
import com.livestrong.tracker.googlefitmodule.Interfaces.CardPresenterInterface;
import com.livestrong.tracker.googlefitmodule.Interfaces.LSGoogleFitObserver;
import com.livestrong.tracker.googlefitmodule.Interfaces.OnCardDataReady;
import com.livestrong.tracker.googlefitmodule.main.LSGoogleFitManager;
import com.livestrong.tracker.googlefitmodule.models.GoogleFitModel;
import com.livestrong.tracker.googlefitmodule.views.LSGoogleFitCardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shambhavipunja on 2/4/16.
 */
public class CardPresenter implements CardPresenterInterface,LSGoogleFitObserver,OnCardDataReady{

    private LSGoogleFitCardView mLsGoogleFitCardView;
    private ModelInterface modelInterface;
    private Date mDate;
    public static final String NO_DATA = "0";
    public static final String STEPS = " steps";

    public CardPresenter( LSGoogleFitCardView lsGoogleFitCardView){
        mLsGoogleFitCardView = lsGoogleFitCardView;
        modelInterface = new GoogleFitModel();
        mDate = new Date();
    }

    @Override
    public void onAttachedToWindow() {
        LSGoogleFitManager.getLsGoogleFitManager().attach(this);
    }

    @Override
    public void onDetachToWindow() {
        LSGoogleFitManager.getLsGoogleFitManager().detach(this);
    }

    @Override
    public void setDate(Calendar date) {
        mDate.setTime(TimeConvertorTask.getmInstance().getTimeMidnight(date.getTimeInMillis()));
    }

    @Override
    public void onDatabaseUpdated() {
        modelInterface.getDataForDate(mDate, this);

    }

    @Override
    public void setFitnessData(FitnessData fitnessData) {
        if (fitnessData == null) {
            mLsGoogleFitCardView.setFitnessText(NO_DATA, STEPS);
        }else {
            mLsGoogleFitCardView.setFitnessText(fitnessData.getFitness_step_count().toString(), STEPS);
        }
    }
}
