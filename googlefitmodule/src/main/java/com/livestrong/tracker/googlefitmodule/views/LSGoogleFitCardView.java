package com.livestrong.tracker.googlefitmodule.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.shambhavipunja.googlefitmodule.R;
import com.livestrong.tracker.googlefitmodule.Interfaces.CardPresenterInterface;
import com.livestrong.tracker.googlefitmodule.Interfaces.CardViewInterface;
import com.livestrong.tracker.googlefitmodule.presenters.CardPresenter;

import java.util.Calendar;

/**
 * TODO: document your custom view class.
 */
public class LSGoogleFitCardView extends FrameLayout implements CardViewInterface {

    private TextView fitnessData;
    private CardPresenterInterface cardPresenterInterface;

    public LSGoogleFitCardView(Context context, Calendar date){
        this(context, null, date);
    }

    public LSGoogleFitCardView(Context context, AttributeSet attrs, Calendar date) {
        this(context, attrs, 0, date);
    }

    public LSGoogleFitCardView(Context context, AttributeSet attrs, int defStyle, Calendar date){
        super(context, attrs, defStyle);
        init(context, date);
    }

    private void init(Context context, Calendar date) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.lsgoogle_fit_card_view, this);
        fitnessData = (TextView) findViewById(R.id.tvfitnessdata);
        cardPresenterInterface = new CardPresenter(this);
        cardPresenterInterface.setDate(date);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        cardPresenterInterface.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        cardPresenterInterface.onDetachToWindow();
        super.onDetachedFromWindow();
    }

    @Override
    public void setFitnessText(String text) {
        fitnessData.setText(text);
    }
}





