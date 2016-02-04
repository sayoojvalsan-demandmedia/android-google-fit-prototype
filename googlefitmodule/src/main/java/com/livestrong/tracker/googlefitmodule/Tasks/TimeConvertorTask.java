package com.livestrong.tracker.googlefitmodule.Tasks;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by shambhavipunja on 2/2/16.
 */
public class TimeConvertorTask {
    private static TimeConvertorTask mInstance = new TimeConvertorTask();
    private TimeConvertorTask(){
    }

    public static TimeConvertorTask getmInstance() {
        return mInstance;
    }

    public Long getTimeMidnight(Long timeInMillis){

        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(timeInMillis);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();

    }
}
