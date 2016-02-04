package com.livestrong.tracker.googlefitapp;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Calendar;
import java.util.Locale;
/**
 * Created by shambhavipunja on 1/25/16.
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link PageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PageFragment extends ListFragment  {
    private static final String PAGE_POSITION = "position";
    private int mPostion;
    private Calendar mcalendar;
    private ListAdapter<ListItem> mAdapter;

    public PageFragment() {
        // Required empty public constructor
    }

    public static PageFragment newInstance(int position) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_POSITION,position );
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPostion = getArguments().getInt(PAGE_POSITION);
        }
        mcalendar = Calendar.getInstance(Locale.US);
        mcalendar.setTimeInMillis(getMillisForPage(mPostion));
        mAdapter = new ListAdapter<ListItem>(this, mcalendar);
        setListAdapter(mAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_page, container, false);
        return rootview;
    }

    public Long getMillisForPage(int page)
    {
        page = MainActivity.TODAY_POSITION - page;
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.add(Calendar.DATE, -page);
        return calendar.getTimeInMillis();
    }

}
