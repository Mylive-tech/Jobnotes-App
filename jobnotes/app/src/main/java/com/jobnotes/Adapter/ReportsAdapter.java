package com.jobnotes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jobnotes.Pojo.Reports;
import com.jobnotes.R;

import java.util.ArrayList;


public class ReportsAdapter extends BaseAdapter {

    Context context;
    private Activity activity;
    private LayoutInflater layoutInflator;
    private ArrayList<Reports> alReports;

    public ReportsAdapter(Activity activity, ArrayList<Reports> alReports) {
        super();
        this.context = context;
        this.activity = activity;
        this.alReports = alReports;
    }

    @Override
    public int getCount() {
        return alReports.size();
    }

    @Override
    public Reports getItem(int position) {
        return alReports.get(position);
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
            convertView = layoutInflator.inflate(R.layout.side_list_item, null);

            holder = new Holder();

            holder.tvReport = (TextView) convertView.findViewById(R.id.tv_sidebar_name);


        } else {
            holder = (Holder) convertView.getTag();
        }
        convertView.setTag(holder);

        holder.tvReport.setText(alReports.get(position).getReport_name());

        return convertView;
    }

    public static class Holder {
        TextView tvReport;

    }
}