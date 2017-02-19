package com.alchemistcorp.naukri.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alchemistcorp.naukri.R;

import java.util.ArrayList;

/**
 * Created by Nitin on 2/18/2017.
 */

public class JazzyListViewAdapter extends BaseAdapter {
    Context context;
    String[] titles , urls , descriptions ;
    public JazzyListViewAdapter(Context context , String[] titles , String[] urls , String[] descriptions) {
        this.context = context ;
        this.titles = titles ;
        this.urls = urls ;
        this.descriptions = descriptions ;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(R.layout.row_jazzy_list,viewGroup,false);
        TextView title = (TextView) rowView.findViewById(R.id.tvJobTitle);
        TextView description = (TextView) rowView.findViewById(R.id.tvJobDescription);
        title.setText(titles[i]);
        description.setText(descriptions[i]);
        return rowView;
    }
}
