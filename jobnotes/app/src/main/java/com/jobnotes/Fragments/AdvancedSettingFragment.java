package com.jobnotes.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jobnotes.LoginActivity;
import com.jobnotes.R;
import com.jobnotes.Utility.Fonts;
import com.jobnotes.Utility.FragmentEntry;
import com.jobnotes.Utility.Message;
import com.jobnotes.Utility.SPDomain;
import com.jobnotes.Utility.SPUser;

/**
 * A placeholder fragment containing a simple view.
 */
public class AdvancedSettingFragment extends Fragment {
    Context context;
    private LayoutInflater inflater;
    private Activity activity;
    private EditText etDomainName;
    private TextView tvSave;
    private Typeface boldFonts;
    private Typeface regularFonts;
    private Typeface mediumFonts;
    private TextView tvWelcomeTo;
    private TextView tvJobNotes;
    private TextView tvCopyright;
    private TextView tvBack;

    public AdvancedSettingFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advanced_setting,
                container, false);
        activity = getActivity();
        this.inflater = inflater;

        initComponents(view);
        initListeners();
        return view;
    }

    private void initComponents(View view) {

        boldFonts = Fonts.getBoldFont(activity);
        mediumFonts = Fonts.getMediumFont(activity);
        regularFonts = Fonts.getRegularFont(activity);

        tvWelcomeTo = (TextView) view.findViewById(R.id.tv_login_welcome_to);
        tvJobNotes = (TextView) view.findViewById(R.id.tv_login_jobnotes);
        etDomainName = (EditText) view.findViewById(R.id.et_advanced_setting_domain);
        tvSave = (TextView) view.findViewById(R.id.tv_advanced_setting_save);
        tvCopyright = (TextView) view.findViewById(R.id.tv_login_advanced_copyright);
        tvBack = (TextView) view.findViewById(R.id.tv_advance_header);
        tvWelcomeTo.setTypeface(regularFonts);
        tvJobNotes.setTypeface(mediumFonts);
        etDomainName.setTypeface(regularFonts);
        tvSave.setTypeface(boldFonts);
        tvCopyright.setTypeface(regularFonts);
        etDomainName.setText(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) );
    }

    private void initListeners() {
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etDomainName.getText().toString().length() != 0) {
                    Toast.makeText(activity, etDomainName.getText().toString(), Toast.LENGTH_LONG).show();
                    SPDomain.setString(activity, SPDomain.DOMAIN_NAME, etDomainName.getText().toString() + "");
                    ((LoginActivity) activity).changeFragment(FragmentEntry.LOGIN, null);
                } else {
                    Message.show(activity, "Please enter domain name");
                }
            }
        });
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity) activity).changeFragment(FragmentEntry.LOGIN, null);
            }
        });


    }
}
