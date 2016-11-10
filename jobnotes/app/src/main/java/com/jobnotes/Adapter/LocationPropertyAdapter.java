package com.jobnotes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.jobnotes.HomeActivity;
import com.jobnotes.Pojo.Property;
import com.jobnotes.R;
import com.jobnotes.Utility.Fonts;
import com.jobnotes.Utility.FragmentEntry;
import com.jobnotes.Utility.Message;
import com.jobnotes.Utility.SPDomain;
import com.jobnotes.Utility.SPUser;
import com.jobnotes.Utility.WebUrl;

import java.util.ArrayList;


public class LocationPropertyAdapter extends BaseAdapter {

    private Typeface regularFonts;
    private Typeface mediumFonts;
    private Typeface boldFonts;
    private Activity activity;
    private LayoutInflater layoutInflator;
    private ArrayList<Property> alProperty;
    private AQuery aq;

    public LocationPropertyAdapter(Activity activity, ArrayList<Property> alProperty) {
        super();

        this.activity = activity;
        this.alProperty = alProperty;
        boldFonts = Fonts.getBoldFont(activity);
        mediumFonts = Fonts.getMediumFont(activity);
        regularFonts = Fonts.getRegularFont(activity);
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
        final Holder holder;


        if (convertView == null) {
            layoutInflator = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflator.inflate(R.layout.property_listing_item, null);
            holder = new Holder();

            aq = new AQuery(convertView);
            holder.tvPriortyTitle = (TextView) convertView.findViewById(R.id.tv_property_title);
            holder.tvAddressTitle = (TextView) convertView.findViewById(R.id.tv_address_title);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
            holder.tvPhoneTitle = (TextView) convertView.findViewById(R.id.tv_phone_number_title);
            holder.tvPhone = (TextView) convertView.findViewById(R.id.tv_phone_number);
            holder.tvGpsTitle = (TextView) convertView.findViewById(R.id.tv_gps_title);
            holder.tvGps = (TextView) convertView.findViewById(R.id.tv_gps);
            holder.tvSpecialNotesTitle = (TextView) convertView.findViewById(R.id.tv_special_notes_title);
            holder.tvSpecialNotes = (TextView) convertView.findViewById(R.id.tv_special_notes);
            holder.tvViewDetail = (TextView) convertView.findViewById(R.id.tv_view_detail);
            holder.imPropertyMap = (ImageView) convertView.findViewById(R.id.im_property_map);
            holder.tvViewDetails = (TextView) convertView.findViewById(R.id.tv_view_detail);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);

            holder.tvPriortyTitle.setTypeface(mediumFonts);
            holder.tvAddressTitle.setTypeface(boldFonts);
            holder.tvAddress.setTypeface(mediumFonts);
            holder.tvPhoneTitle.setTypeface(boldFonts);
            holder.tvPhone.setTypeface(mediumFonts);
            holder.tvGpsTitle.setTypeface(boldFonts);
            holder.tvGps.setTypeface(mediumFonts);
            holder.tvSpecialNotesTitle.setTypeface(boldFonts);
            holder.tvSpecialNotes.setTypeface(mediumFonts);
            holder.tvViewDetails.setTypeface(boldFonts);
            holder.llGps = (LinearLayout) convertView.findViewById(R.id.ll_listing_gps);
            holder.llAddress = (LinearLayout) convertView.findViewById(R.id.ll_listing_address);


        } else {
            holder = (Holder) convertView.getTag();
        }
        convertView.setTag(holder);
        convertView.setClickable(true);
        holder.tvPriortyTitle.setText(alProperty.get(position).getJob_listing());
        holder.tvAddress.setText(alProperty.get(position).getLocation_address());
        holder.tvPhone.setText(alProperty.get(position).getOnsite_contact_person() + "\n" + alProperty.get(position).getPhn_no() + "");
        holder.tvGps.setText(alProperty.get(position).getLat() + "\n" + alProperty.get(position).getLag());
        //   holder.tvSpecialNotes.setText(alProperty.get(position).getImportent_notes());
        //  aq.id(R.id.im_property_map).progress(R.id.progressBar).image(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.IMAGEPATH + alProperty.get(position).getMap_widget(), true, true);
        if (alProperty.get(position).getNotes().size() == 0) {
            holder.tvSpecialNotes.setText("No notes are available");
        } else if (alProperty.get(position).getNotes().size() > 1) {
            String notesShown = alProperty.get(position).getNotes().get(0).getNote();
            if (notesShown != null) {
                holder.tvSpecialNotes.setText(Html.fromHtml(notesShown));
            }
            try {
                if (alProperty.get(position).getNotes().get(0).getNote().equalsIgnoreCase("none") || alProperty.get(position).getNotes().get(0).getNote().toString().equalsIgnoreCase("null") || alProperty.get(position).getNotes().get(0).getNote().toString().equalsIgnoreCase("")) {
                    holder.tvSpecialNotes.setText(alProperty.get(position).getNotes().get(1).getNote());
                }
            } catch (Exception e) {
                holder.tvSpecialNotes.setText("");
                e.printStackTrace();
            }
        } else {
            holder.tvSpecialNotes.setText(alProperty.get(position).getNotes().get(0).getNote());
        }

        Glide
                .with(activity)
                .load(Uri.parse(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.IMAGEPATH + alProperty.get(position).getMap_widget())).into(holder.imPropertyMap);
        Log.e("image path---", SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.IMAGEPATH + alProperty.get(position).getMap_widget());
        holder.tvSpecialNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(holder.tvSpecialNotes.getText().toString().equalsIgnoreCase("none") || holder.tvSpecialNotes.getText().toString().equalsIgnoreCase("No notes are available"))) {
                    WebUrl.ShowLog(holder.tvSpecialNotes.getText().toString());
                    SPUser.setInt(activity, SPUser.SCROLL_POSITION, position);
                    ((HomeActivity) activity).changeFragment(FragmentEntry.SPECIAL_NOTES, alProperty.get(position));
                } else {
                    Message.show(activity, "No notes are available");
                }

            }
        });
        holder.tvSpecialNotesTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(holder.tvSpecialNotes.getText().toString().equalsIgnoreCase("none") || holder.tvSpecialNotes.getText().toString().equalsIgnoreCase("No notes are available"))) {
                    WebUrl.ShowLog(holder.tvSpecialNotes.getText().toString());
                    SPUser.setInt(activity, SPUser.SCROLL_POSITION, position);
                    ((HomeActivity) activity).changeFragment(FragmentEntry.SPECIAL_NOTES, alProperty.get(position));

                } else {
                    Message.show(activity, "No notes are available");
                }


            }
        });
        holder.tvViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(activity,"Clicked",Toast.LENGTH_LONG).show();
                SPUser.setInt(activity, SPUser.SCROLL_POSITION, position);
                ((HomeActivity) activity).changeFragment(FragmentEntry.VIEW_DETAIL, alProperty.get(position));
            }
        });
        holder.imPropertyMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(activity,"Clicked",Toast.LENGTH_LONG).show();
                SPUser.setInt(activity, SPUser.SCROLL_POSITION, position);
                ((HomeActivity) activity).changeFragment(FragmentEntry.VIEW_DETAIL, alProperty.get(position));
            }
        });


        holder.llGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUri = "http://maps.google.com/maps?q=loc:" + alProperty.get(position).getLat() + "," + alProperty.get(position).getLag() + " (" + alProperty.get(position).getLocation_name() + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                activity.startActivity(intent);
            }
        });

        return convertView;
    }

    public static class Holder {

        private TextView tvAddressTitle;
        private TextView tvPriortyTitle;
        private TextView tvAddress;
        private TextView tvPhoneTitle;
        private TextView tvPhone;
        private TextView tvGpsTitle;
        private TextView tvGps;
        private TextView tvSpecialNotesTitle;
        private TextView tvSpecialNotes;
        private TextView tvViewDetail;
        private ImageView imPropertyMap;
        private TextView tvViewDetails;
        private LinearLayout llGps;
        private LinearLayout llAddress;
        private ProgressBar progressBar;

    }
}