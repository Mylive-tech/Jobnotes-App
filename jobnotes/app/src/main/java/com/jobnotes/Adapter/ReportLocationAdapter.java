package com.jobnotes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jobnotes.Pojo.Property;
import com.jobnotes.R;

import java.util.ArrayList;


public class ReportLocationAdapter extends BaseAdapter {

    Context context;
    private Activity activity;
    private LayoutInflater layoutInflator;
    private ArrayList<Property> alProperty;

    public ReportLocationAdapter(Activity activity, ArrayList<Property> alProperty) {
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
            convertView = layoutInflator.inflate(R.layout.report_form_list_item, null);
            holder = new Holder();

            holder.tvName = (TextView) convertView.findViewById(R.id.tv_report_form_spinner_item);


        } else {
            holder = (Holder) convertView.getTag();
        }
        convertView.setTag(holder);

        holder.tvName.setText(alProperty.get(position).getLocation_name());

        return convertView;
    }

    public static class Holder {
        TextView tvName;

    }
}