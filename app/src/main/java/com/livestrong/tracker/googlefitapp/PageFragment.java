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
    private ListAdapter<ListItem> mAdapter;
    private Calendar mdate;
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
        mdate = Calendar.getInstance(Locale.US);
        mdate.setTimeInMillis(getDateForPage(mPostion));
        mAdapter = new ListAdapter<ListItem>(this,mdate);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_page, container, false);
        /*Button btn = (Button)rootview.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 LSGoogleFitManager.getLsGoogleFitManager().disconnectGoogleFit(getContext());
            }
        });*/

        return rootview;
    }

    public Long getDateForPage(int page)
    {
        page = MainActivity.TODAY_POSITION - page;
        //Logger.d(TAG, "Days = " + page);
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.add(Calendar.DATE, -page);

        /*Calendar newCalender = Calendar.getInstance(Locale.US);
        newCalender.setTimeInMillis(calendar.getTimeInMillis());*/
        return calendar.getTimeInMillis();
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            LSGoogleFitManager.getLsGoogleFitManager().attach(this);
        }
        catch(ClassCastException e){
            throw new ClassCastException(this.toString() + " Must implement GoogleFitObserver" );
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        LSGoogleFitManager.getLsGoogleFitManager().detach(this);
       // mAdapter.getLsGoogleFitCardView().removeListener();
    }

    @Override
    public void onDatabaseUpdated() {
        Log.i("********************", String.valueOf(mPostion));
    }
*/




}
