package com.livestrong.tracker.googlefitmodule.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.example.shambhavipunja.googlefitmodule.R;

/**
 * TODO: document your custom view class.
 */
public class LSGoogleFitCardView extends FrameLayout {
    public LSGoogleFitCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.lsgoogle_fit_card_view, this,true);

    }
}
