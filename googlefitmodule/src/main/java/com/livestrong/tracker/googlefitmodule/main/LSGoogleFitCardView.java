package com.livestrong.tracker.googlefitmodule.main;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.shambhavipunja.googlefitmodule.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * TODO: document your custom view class.
 */
public class LSGoogleFitCardView extends FrameLayout implements LSGoogleFitObserver{

    private Calendar mDate;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
    private TextView mtv,mtv2;
    private Date mdate;
    public LSGoogleFitCardView(Context context,Calendar date){
        this(context, null, date);
        mDate = date;

    }
    public LSGoogleFitCardView(Context context, AttributeSet attrs,Calendar date) {
        this(context, attrs, 0, date);
        mDate = date;
    }
    public LSGoogleFitCardView(Context context, AttributeSet attrs, int defStyle,Calendar date){
        super(context, attrs, defStyle);
        mDate = date;
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.lsgoogle_fit_card_view, this);
        mtv = (TextView) findViewById(R.id.info_text);
        mtv2 = (TextView) findViewById(R.id.text2);
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(CalenderTimeConvertor.getmInstance().getTimeMidnight(mDate.getTimeInMillis()));
        mdate = cal.getTime();
        mtv.setText(dateFormat.format(mdate));


        /*LSGoogleFitManager.getLsGoogleFitManager().attach(new LSGoogleFitObserver() {
            @Override
            public void onDatabaseUpdated() {
                tv.setText("yayy");
                Log.i("*************", "******");

            }
        });*/

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
            mtv2.setText("yayy");
        }


}





