package com.livestrong.tracker.googlefitmodule.main;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by shambhavipunja on 2/2/16.
 */
public class CalenderTimeConvertor {
    private static CalenderTimeConvertor mInstance = new CalenderTimeConvertor();
    private CalenderTimeConvertor(){
    }

    public static CalenderTimeConvertor getmInstance() {
        return mInstance;
    }

    public Long getTimeMidnight(Long timeInMillis){

        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(timeInMillis);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        //Calendar newCal = Calendar.getInstance(Locale.US);
        //newCal.setTimeInMillis(cal.getTimeInMillis());

        return cal.getTimeInMillis();

    }
}
