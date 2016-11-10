package com.jobnotes.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jobnotes.HomeActivity;
import com.jobnotes.LoginActivity;
import com.jobnotes.R;
import com.jobnotes.Utility.CustomHttpClient;
import com.jobnotes.Utility.Fonts;
import com.jobnotes.Utility.FragmentEntry;
import com.jobnotes.Utility.Message;
import com.jobnotes.Utility.SPDomain;
import com.jobnotes.Utility.SPUser;
import com.jobnotes.Utility.WebUrl;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {
    Context context;
    private LayoutInflater inflater;
    private Activity activity;
    private EditText etEmail;
    private TextView tvAdvancedSetting;
    private EditText etPassword;
    private TextView tvLogin;
    private Typeface boldFonts;
    private Typeface regularFonts;
    private Typeface mediumFonts;
    private TextView tvWelcomeTo;
    private TextView tvJobNotes;
    private TextView tvCopyright;
    private ProgressDialog progressDialog;

    public LoginFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,
                container, false);
        activity = getActivity();

        this.inflater = inflater;
        initComponents(view);
        initListeners();


        return view;
    }

    private void initComponents(View view) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);

        boldFonts = Fonts.getBoldFont(activity);
        mediumFonts = Fonts.getMediumFont(activity);
        regularFonts = Fonts.getRegularFont(activity);

        tvWelcomeTo = (TextView) view.findViewById(R.id.tv_login_welcome_to);
        tvJobNotes = (TextView) view.findViewById(R.id.tv_login_jobnotes);
        etEmail = (EditText) view.findViewById(R.id.et_login_email_id);
        etPassword = (EditText) view.findViewById(R.id.et_login_password);
        tvAdvancedSetting = (TextView) view.findViewById(R.id.tv_login_advanced_setting);
        tvLogin = (TextView) view.findViewById(R.id.tv_login_login);
        tvCopyright = (TextView) view.findViewById(R.id.tv_login_advanced_copyright);

        tvWelcomeTo.setTypeface(regularFonts);
        tvJobNotes.setTypeface(mediumFonts);
        etEmail.setTypeface(regularFonts);
        etPassword.setTypeface(regularFonts);
        tvLogin.setTypeface(boldFonts);
        tvAdvancedSetting.setTypeface(boldFonts);
        tvCopyright.setTypeface(regularFonts);

    }

    private void initListeners() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValid()) {

                    Log.e("Login==DOMAIN_NAME==", SPDomain.getString(activity, SPDomain.DOMAIN_NAME));
                    // Toast.makeText(activity,WebUrl.LOGIN+"",Toast.LENGTH_LONG).show();
                    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("uname", etEmail.getText().toString()));
                    params.add(new BasicNameValuePair("password", etPassword.getText().toString()));
                 /*   params.add(new BasicNameValuePair("device_id", SPDevice.getValue(activity, SPDevice.DEVICE_ID)));
                    params.add(new BasicNameValuePair("device_token", SPGCM.getValue(activity, SPGCM.GCM_TOKEN) + "123456"));*/
                    new CustomHttpClient(activity).executeHttp(SPDomain.getString(activity,SPDomain.DOMAIN_NAME) + WebUrl.LOGIN, params, progressDialog, new CustomHttpClient.OnSuccess() {
                        @Override
                        public void onSucess(String result) {
                            try {

                                JSONObject jObject = new JSONObject(result);
                                JSONObject jsonObject = jObject.optJSONObject("login_info");
                                if (jsonObject.optString("auto_login").equalsIgnoreCase("1")) {
                                    SPUser.setString(activity, SPUser.USER_ID, jsonObject.optString("id"));
                                    SPUser.setString(activity, SPUser.USER_NAME, etEmail.getText().toString());
                                    SPUser.setString(activity, SPUser.USER_PASSWORD, etPassword.getText().toString());
                                    activity.startActivity(new Intent(activity, HomeActivity.class));
                                    activity.finish();
                                }
                                SPUser.setString(activity, SPUser.PROPERTIES, jObject.optJSONObject("properites") + "");
                                SPUser.setString(activity, SPUser.REPORTS, jObject.optJSONObject("reports") + "");
                                SPUser.setString(activity, SPUser.REPORT_INFO, jObject.optJSONArray("report_info").toString());


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, null, CustomHttpClient.Method.POST, true);
                }


            }
        });

        tvAdvancedSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity) activity).changeFragment(FragmentEntry.ADVANCED_SETTING, null);
            }
        });


    }

    private boolean isValid() {
        if (etEmail.getText().toString().toString().length() == 0) {
            Message.show(activity, "Please enter Email");
            return false;
        } /*else if (!EmailValidator.isValid(etEmail.getText().toString().toString())) {
            Message.show(activity, "Please enter Valid Email");
            return false;
        }*/ else if (etPassword.getText().toString().toString().length() == 0) {
            Message.show(activity, "Please enter Password");
            return false;
        }
        return true;
    }


}
