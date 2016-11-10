package com.jobnotes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jobnotes.Pojo.Notes;
import com.jobnotes.R;

import java.util.ArrayList;


public class NotesAdapter extends BaseAdapter {

    Context context;
    private Activity activity;
    private LayoutInflater layoutInflator;
    private ArrayList<Notes> alNotes;

    public NotesAdapter(Activity activity, ArrayList<Notes> alNotes) {
        super();
        this.context = context;
        this.activity = activity;
        this.alNotes = alNotes;
    }

    @Override
    public int getCount() {
        return alNotes.size();
    }

    @Override
    public Notes getItem(int position) {
        return alNotes.get(position);
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
            holder.tvPostedOn = (TextView) convertView.findViewById(R.id.tv_special_notes_posted_on);
            holder.tvPostedBy = (TextView) convertView.findViewById(R.id.tv_special_notes_posted_by);

        } else {
            holder = (Holder) convertView.getTag();
        }
        convertView.setTag(holder);

        holder.tvNotes.setText(alNotes.get(position).getNote());

        String postedBy = "<b> Posted by : </b> " + alNotes.get(position).getname();
        holder.tvPostedBy.setText(Html.fromHtml(postedBy));

        String postedOn  = "<b> Posted on : </b> " + alNotes.get(position).getDate();
        holder.tvPostedOn.setText(Html.fromHtml(postedOn));

        return convertView;
    }

    public static class Holder {
        private TextView tvNotes;
        private TextView tvPostedOn;
        private TextView tvPostedBy;

    }
}