package com.livestrong.tracker.googlefitmodule.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.shambhavipunja.googlefitmodule.R;
import com.livestrong.tracker.googlefitmodule.Interfaces.LSGoogleFitObserver;
import com.livestrong.tracker.googlefitmodule.Tasks.TimeConvertorTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * TODO: document your custom view class.
 */
public class LSGoogleFitCardView extends FrameLayout implements LSGoogleFitObserver {

    private Calendar mCalendar;
    private TextView mTv, mTv2;
    private Date mDate;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

    public LSGoogleFitCardView(Context context, Calendar date){
        this(context, null, date);
    }

    public LSGoogleFitCardView(Context context, AttributeSet attrs, Calendar date) {
        this(context, attrs, 0, date);
    }

    public LSGoogleFitCardView(Context context, AttributeSet attrs, int defStyle, Calendar date){
        super(context, attrs, defStyle);
        mCalendar = date;
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.lsgoogle_fit_card_view, this);

        mTv = (TextView) findViewById(R.id.info_text);
        mTv2 = (TextView) findViewById(R.id.text2);

        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(TimeConvertorTask.getmInstance().getTimeMidnight(mCalendar.getTimeInMillis()));
        mDate = cal.getTime();

        mTv.setText(mDateFormat.format(mDate));

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LSGoogleFitManager.getLsGoogleFitManager().attach(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        LSGoogleFitManager.getLsGoogleFitManager().detach(this);
        super.onDetachedFromWindow();

    }
        @Override
        public void onDatabaseUpdated() {
            //TODO: change text to respective
            mTv2.setText("yayy");
        }


}





