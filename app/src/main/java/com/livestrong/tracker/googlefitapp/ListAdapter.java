package com.livestrong.tracker.googlefitapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.livestrong.tracker.googlefitmodule.views.LSGoogleFitCardView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by shambhavipunja on 2/2/16.
 */
public class ListAdapter<ListItem> extends BaseAdapter {
    private PageFragment mContext;
    private ArrayList<Integer> viewIndex = new ArrayList<Integer>();
    private Calendar mDate;
    public static final int LS_GOOGLE_FIT_CARD = 0;
    public ListAdapter(PageFragment context,Calendar date){
        mContext = context;
        mDate = date;
        viewIndex.add(0);
    }
    @Override
    public int getCount() {
        return viewIndex.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return viewIndex.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        int viewType = getItemViewType(position);
        if (rowView == null) {
            LayoutInflater inflater = mContext.getActivity().getLayoutInflater();
            switch (viewType) {
                case LS_GOOGLE_FIT_CARD:
                     LSGoogleFitCardView lsGoogleFitCardView = new LSGoogleFitCardView(inflater.getContext(), mDate);
                     rowView = lsGoogleFitCardView;
                     break;
            }
        }
        return  rowView;
    }
}
