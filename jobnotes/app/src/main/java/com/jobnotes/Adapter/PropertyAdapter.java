package com.jobnotes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobnotes.HomeActivity;
import com.jobnotes.Pojo.Property;
import com.jobnotes.R;
import com.jobnotes.Utility.FragmentEntry;

import java.util.ArrayList;


public class PropertyAdapter extends BaseAdapter {

    Context context;
    private Activity activity;
    private LayoutInflater layoutInflator;
    private ArrayList<Property> alProperty;

    public PropertyAdapter(Activity activity, ArrayList<Property> alProperty) {
        super();
        this.context = context;
        this.activity = activity;
        this.alProperty = alProperty;
    }

    @Override
    public int getCount() {
        return alProperty.size();
    }

    @Override
    public Property getItem(int position) {
        return alProperty.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;


        if (convertView == null) {
            layoutInflator = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflator.inflate(R.layout.location_list_item, null);
            holder = new Holder();

            holder.tvName = (TextView) convertView.findViewById(R.id.tv_dashboard_location_name);


        } else {
            holder = (Holder) convertView.getTag();
        }
        convertView.setTag(holder);
        holder.tvName.setText(alProperty.get(position).getLocation_name());
       /* holder.llContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ( (HomeActivity)activity).changeFragment(FragmentEntry.PROPERTY_LISTING, alProperty.get(position));
                return false;
            }
        });*/
        /*holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        return convertView;
    }

    public static class Holder {
        TextView tvName;


    }
}