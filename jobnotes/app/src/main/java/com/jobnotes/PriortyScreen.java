package com.jobnotes;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jobnotes.Pojo.Property;
import com.jobnotes.Utility.Fonts;


/**
 * A placeholder fragment containing a simple view.
 */
public class PriortyScreen extends FragmentActivity implements OnMapReadyCallback {
    Context context;
    private Activity activity;
    private Typeface boldFonts;
    private Typeface regularFonts;
    private Typeface mediumFonts;
    private GoogleMap mMap;
    private TextView tvPriortyHeader;
    private TextView tvPriortyTitle;
    private TextView tvAddressTitle;
    private TextView tvAddress;
    private TextView tvPhoneTitle;
    private TextView tvPhone;
    private TextView tvGpsTitle;
    private TextView tvGps;
    private TextView tvSpecialNotesTitle;
    private TextView tvSpecialNotes;
    private Property property;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priorty);
        initComponents();
        initListeners();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void initComponents() {

        boldFonts = Fonts.getBoldFont(getApplicationContext());
        mediumFonts = Fonts.getMediumFont(getApplicationContext());
        regularFonts = Fonts.getRegularFont(getApplicationContext());

        tvPriortyHeader = (TextView) findViewById(R.id.tv_priorty_header);
        tvPriortyTitle = (TextView) findViewById(R.id.tv_priorty_title);
        tvAddressTitle = (TextView) findViewById(R.id.tv_address_title);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvPhoneTitle = (TextView) findViewById(R.id.tv_phone_number_title);
        tvPhone = (TextView) findViewById(R.id.tv_phone_number);
        tvGpsTitle = (TextView) findViewById(R.id.tv_gps_title);
        tvGps = (TextView) findViewById(R.id.tv_gps);
        tvSpecialNotesTitle = (TextView) findViewById(R.id.tv_special_notes_title);
        tvSpecialNotes = (TextView) findViewById(R.id.tv_special_notes);
        property = (Property) getIntent().getExtras().getSerializable("property");
        tvPriortyHeader.setTypeface(mediumFonts);
        tvPriortyTitle.setTypeface(mediumFonts);
        tvAddressTitle.setTypeface(boldFonts);
        tvAddress.setTypeface(mediumFonts);
        tvPhoneTitle.setTypeface(boldFonts);
        tvPhone.setTypeface(mediumFonts);
        tvGpsTitle.setTypeface(boldFonts);
        tvGps.setTypeface(mediumFonts);
        tvSpecialNotesTitle.setTypeface(boldFonts);
        tvSpecialNotes.setTypeface(mediumFonts);

        tvPriortyTitle.setText(property.getJob_listing());
        tvAddress.setText(property.getLocation_address());
        tvPhone.setText(property.getPhn_no() + "");
        tvGps.setText(property.getPhn_no() + "");
        tvSpecialNotes.setText(property.getImportent_notes());


    }

    private void initListeners() {
        tvPriortyHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(22.85445646, 75.15584556);
        mMap.setTrafficEnabled(true);
        // mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setBuildingsEnabled(true);

    }


}
