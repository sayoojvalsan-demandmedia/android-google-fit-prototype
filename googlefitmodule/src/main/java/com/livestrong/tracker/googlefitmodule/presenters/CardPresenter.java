package com.livestrong.tracker.googlefitmodule.presenters;

import com.livestrong.tracker.googlefitmodule.greendao.FitnessData;
import com.livestrong.tracker.googlefitmodule.Interfaces.CardModelInterface;
import com.livestrong.tracker.googlefitmodule.Interfaces.CardPresenterInterface;
import com.livestrong.tracker.googlefitmodule.Interfaces.LSGoogleFitObserver;
import com.livestrong.tracker.googlefitmodule.Interfaces.OnCardDataReady;
import com.livestrong.tracker.googlefitmodule.main.LSGoogleFitManager;
import com.livestrong.tracker.googlefitmodule.models.CardModel;
import com.livestrong.tracker.googlefitmodule.views.LSGoogleFitCardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shambhavipunja on 2/4/16.
 */
public class CardPresenter implements CardPresenterInterface,LSGoogleFitObserver,OnCardDataReady{

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MMM dd, yyyy");
    private LSGoogleFitCardView mLsGoogleFitCardView;
    private CardModelInterface modelInterface;
    private Date mDate;
    public CardPresenter( LSGoogleFitCardView lsGoogleFitCardView){
        mLsGoogleFitCardView = lsGoogleFitCardView;
        modelInterface = new CardModel();
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
        mDate.setTime(date.getTimeInMillis());
    }

    @Override
    public void onDatabaseUpdated() {
        modelInterface.getDataForDate(mDate, this);

    }

    @Override
    public void setFitnessData(FitnessData fitnessData) {
        mLsGoogleFitCardView.setFitnessText(mDateFormat.format(fitnessData.getDate()));
    }
}
