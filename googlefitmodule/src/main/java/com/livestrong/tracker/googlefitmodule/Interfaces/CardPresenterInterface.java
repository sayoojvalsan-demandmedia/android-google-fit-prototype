package com.livestrong.tracker.googlefitmodule.Interfaces;

import java.util.Calendar;

/**
 * Created by shambhavipunja on 2/4/16.
 */
public interface CardPresenterInterface {
    public void onAttachedToWindow();
    public void onDetachToWindow();
    public void setDate(Calendar date);
}
