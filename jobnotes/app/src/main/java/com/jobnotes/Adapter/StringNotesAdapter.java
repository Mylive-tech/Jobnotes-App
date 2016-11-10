package com.jobnotes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jobnotes.Pojo.Notes;
import com.jobnotes.R;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class StringNotesAdapter extends BaseAdapter {

    Context context;
    private Activity activity;
    private LayoutInflater layoutInflator;
    private String[] arrNotes;

    public StringNotesAdapter(Activity activity, String[] alNotes) {
        super();
        this.context = context;
        this.activity = activity;
        this.arrNotes = alNotes;
    }

    @Override
    public int getCount() {
        return arrNotes.length;
    }

    @Override
    public String getItem(int position) {
        return arrNotes[position];
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
            convertView = layoutInflator.inflate(R.layout.special_notes_item, null);
            holder = new Holder();

            holder.tvNotes = (TextView) convertView.findViewById(R.id.tv_special_notes_description);

        } else {
            holder = (Holder) convertView.getTag();
        }
        convertView.setTag(holder);

        holder.tvNotes.setText(arrNotes[position]);

        return convertView;
    }

    public static class Holder {
        TextView tvNotes;

    }
}