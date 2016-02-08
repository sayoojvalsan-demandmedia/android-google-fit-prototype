package com.livestrong.tracker.googlefitmodule.views;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

    private TextView mFitnessData, mSuffix;
    private ProgressBar mSpinner;
    private ImageView mImageView;
    private CardPresenterInterface mCardPresenterInterface;

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
        mFitnessData = (TextView) findViewById(R.id.text_data);
        mSuffix = (TextView) findViewById(R.id.text_suffix);
        mSpinner = (ProgressBar) findViewById(R.id.progressBar1);
        mImageView = (ImageView)findViewById(R.id.imageView);
        mImageView.setVisibility(INVISIBLE);

        mCardPresenterInterface = new CardPresenter(this);
        mCardPresenterInterface.setDate(date);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mCardPresenterInterface.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        mCardPresenterInterface.onDetachToWindow();
        super.onDetachedFromWindow();
    }

    @Override
    public void setFitnessText(String text, String suffix) {
        mSpinner.setVisibility(View.GONE);
        mImageView.setVisibility(VISIBLE);
        mFitnessData.setText(text);
        mSuffix.setText(suffix);
    }



}





