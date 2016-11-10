package com.jobnotes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jobnotes.Adapter.ReportLocationAdapter;
import com.jobnotes.Adapter.ReportPropertyAdapter;
import com.jobnotes.Adapter.ReportPropertyFromLocationAdapter;
import com.jobnotes.Pojo.LocationProperties;
import com.jobnotes.Pojo.Property;
import com.jobnotes.Pojo.Reports;
import com.jobnotes.Utility.CustomHttpClient;
import com.jobnotes.Utility.Fonts;
import com.jobnotes.Utility.FragmentEntry;
import com.jobnotes.Utility.NetworkConnection;
import com.jobnotes.Utility.SPDomain;
import com.jobnotes.Utility.SPUser;
import com.jobnotes.Utility.WebUrl;
import com.jobnotes.service.BackgroundReportRefresh;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReportForms extends Activity {
    ArrayList<EditText> alEdittext;
    ArrayList<RadioGroup> alRadiogroup;
    ArrayList<Spinner> alSpinner;
    ArrayList<ArrayList<CheckBox>> alCheckboxes;
    ArrayList<LocationProperties> alProperty;

    ArrayList<NameValuePair> reportparams;
    DBHolder dbHolder;
    private ArrayList<Reports> alReports;
    private ProgressDialog progressDialog;
    private ScrollView scrollContainer;
    private LinearLayout llReportContainer;
    private Reports report;
    private LinearLayout llScrollReportContainer;
    private MagicInflator inflator;
    private int etCounter;
    private int rgcounter;
    private int spCounter;
    private int cbCounter;
    private TextView tvSubmit;
    private TextView tvProperty;
    private Spinner spLocation;
    private Spinner spProperty;
    private TextView tvLocation;
    private ArrayList<Property> alMasterData;
    private ArrayList<Property> alUniqueLocation;
    private TextView tvHeaderBackTitle;
    private long location_id = 0;
    private long property_id = 0;
    private ArrayList<Property> alLocationProperty;
    private NetworkConnection networkConnection;
    private long selected_location = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private long selected_property_id = 0;
    private LinearLayout llReportSubmitContainer;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_report);

        initComponents();
        initListeners();
        setListData(SPUser.getString(ReportForms.this, SPUser.PROPERTIES));


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void initComponents() {
        progressDialog = new ProgressDialog(ReportForms.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);
        networkConnection = new NetworkConnection();
        dbHolder = new DBHolder(getApplicationContext(), ReportForms.this);
        inflator = new MagicInflator(ReportForms.this);
        report = (Reports) getIntent().getExtras().getSerializable("report");
        scrollContainer = (ScrollView) findViewById(R.id.sc_report_container);
        llReportContainer = (LinearLayout) findViewById(R.id.ll_report_container);
        llReportSubmitContainer = (LinearLayout) findViewById(R.id.ll_report_submit_container);
        llScrollReportContainer = (LinearLayout) findViewById(R.id.ll_report_scroll_container);
        spLocation = (Spinner) findViewById(R.id.sp_report_location);
        spProperty = (Spinner) findViewById(R.id.sp_report_property);
        tvLocation = (TextView) findViewById(R.id.tv_report_location);
        tvProperty = (TextView) findViewById(R.id.tv_report_property);
        tvHeaderBackTitle = (TextView) findViewById(R.id.tv_report_header_title);
        tvLocation.setTypeface(Fonts.getBoldFont(ReportForms.this));
        tvProperty.setTypeface(Fonts.getBoldFont(ReportForms.this));
        tvHeaderBackTitle.setTypeface(Fonts.getMediumFont(ReportForms.this));
        tvHeaderBackTitle.setText(report.getReport_name());

        System.out.println("report getReport_name== " + report.getReport_name());
        System.out.println("report getForm_body== " + report.getForm_body());
        inflateForm(report.getForm_body() + "");
        if (!report.getSubmit_button_text().equalsIgnoreCase("")) {
            tvSubmit = (TextView) inflator.getSubmitbutton(report.getSubmit_button_text(), llReportSubmitContainer);
        }

        try {
            selected_location = getIntent().getExtras().getLong("location_id");
            selected_property_id = getIntent().getExtras().getLong("property_id");
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }

    }

    private void setListData(String property) {
        try {
            if ((!property.equalsIgnoreCase("")) && (!property.equals(null)) && (!property.equalsIgnoreCase("null"))) {
                JSONObject jsonObject = new JSONObject(property);
                if (jsonObject.optJSONArray("property").length() > 0) {

                    alMasterData = new Gson().fromJson(jsonObject.optJSONArray("property").toString(), new TypeToken<List<Property>>() {
                    }.getType());

                    int n = alMasterData.size();
                    for (int i = 0; i < n; i++) {
                        // System.out.println("Priorty name === "+alPriortyList.get(i).getLocation_name());
                        if (!("," + alMasterData.get(i).getEnabled_reports() + ",").contains("," + report.getReport_id() + ",")) {
                            alMasterData.remove(i);
                            i--;
                            --n;
                        } else {
                            System.out.println("alMasterData name === " + alMasterData.get(i).getLocation_name());
                        }
                    }
                }
                if (alMasterData.size() == 0) {
                    alMasterData = new Gson().fromJson(jsonObject.optJSONArray("property").toString(), new TypeToken<List<Property>>() {
                    }.getType());
                }
                ArrayList<String> alTmp = new ArrayList<>();
                alUniqueLocation = new ArrayList<>();
                for (int i = 0; i < alMasterData.size(); i++) {
                    if (!alTmp.contains(alMasterData.get(i).getLocation_name())) {
                        alUniqueLocation.add(alMasterData.get(i));
                        alTmp.add(alMasterData.get(i).getLocation_name());
                    }
                }
                spLocation.setAdapter(new ReportLocationAdapter(ReportForms.this, alUniqueLocation));

                if (selected_location != 0) {

                    for (int i = 0; i < alUniqueLocation.size(); i++) {
                        if (selected_location == alUniqueLocation.get(i).getLocation_id()) {
                            WebUrl.ShowLog(selected_location + "====" + alUniqueLocation.get(i).getLocation_name() + "==" + alUniqueLocation.get(i).getLocation_id());
                            spLocation.setSelection(i);
                            spLocation.setSelected(false);
                            spLocation.setClickable(false);
                        }
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void inflateForm(String reportdata) {
        reportparams = new ArrayList<NameValuePair>();
        alEdittext = new ArrayList<EditText>();
        alRadiogroup = new ArrayList<RadioGroup>();
        alSpinner = new ArrayList<Spinner>();
        alCheckboxes = new ArrayList<ArrayList<CheckBox>>();
        etCounter = 0;
        rgcounter = 0;
        spCounter = 0;
        cbCounter = 0;

        try {
            JSONArray jsonArray = new JSONArray(reportdata);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if ((jsonObject.optString("field_type").equalsIgnoreCase("text") || jsonObject.optString("field_type").equalsIgnoreCase("number") || jsonObject.optString("field_type").equalsIgnoreCase("paragraph")) && (!jsonObject.optString("label").equalsIgnoreCase(""))) {
                    EditText et = inflator.getEditText(jsonObject, llReportContainer);
                    alEdittext.add(etCounter, et);
                    etCounter++;

                }
                if (jsonObject.optString("field_type").equalsIgnoreCase("radio")) {
                    RadioGroup rg = inflator.getRadioGroup(jsonObject, llReportContainer);
                    alRadiogroup.add(rgcounter, rg);
        /*        View radioButton = rg.findViewById(rg.getCheckedRadioButtonId());
                int radioId = rg.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) rg.getChildAt(radioId);
               String selection = (String) btn.getText().toString();
                System.out.println("selection radio=="+selection);
                     reportparams.add(new BasicNameValuePair(rg.getTag().toString(), selection));
*/
                    rgcounter++;
                }

                if (jsonObject.optString("field_type").equalsIgnoreCase("checkboxes")) {
                    alCheckboxes.add(cbCounter, inflator.getCheckBoxes(jsonObject, llReportContainer));


                    cbCounter++;
                }

                if (jsonObject.optString("field_type").equalsIgnoreCase("dropdown")) {
                    alSpinner.add(spCounter, inflator.getSpinner(jsonObject, llReportContainer));
                    spCounter++;
                }
                if (jsonObject.optString("field_type").equalsIgnoreCase("text") && jsonObject.optString("label").equalsIgnoreCase("")) {
                    inflator.magicSeperator(llReportContainer);
                }

                if (jsonObject.optString("field_type").equalsIgnoreCase("fieldset")) {
                    inflator.getTextView(jsonObject, llReportContainer);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private boolean validationForm(String reportdata) {
        reportparams = new ArrayList<NameValuePair>();
        boolean status = true;
        int etValidCounter = 0;
        int rgValidCounter = 0;
        int spinnerValidCounter = 0;
        int cbValidCounter = 0;
        ArrayList<ArrayList<CheckBox>> alValidCheckbox = new ArrayList<ArrayList<CheckBox>>();
        try {
            JSONArray jsonArray = new JSONArray(reportdata);


            if (location_id == 0) {
                Toast.makeText(ReportForms.this, "Location is required", Toast.LENGTH_LONG).show();
                status = false;

            } else if (property_id == 0) {
                Toast.makeText(ReportForms.this, "Property is required", Toast.LENGTH_LONG).show();
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if ((jsonObject.optString("field_type").equalsIgnoreCase("text") || jsonObject.optString("field_type").equalsIgnoreCase("number") || jsonObject.optString("field_type").equalsIgnoreCase("paragraph")) && (!jsonObject.optString("label").equalsIgnoreCase(""))) {


                    if (etValidCounter < etCounter) {
                        EditText et;
                        if (jsonObject.optBoolean("required") == true) {
                            et = alEdittext.get(etValidCounter);

                            if (et.getText().toString().equalsIgnoreCase("") || et.getText().toString().equalsIgnoreCase("null") || et.getText().toString().equalsIgnoreCase(null)) {
                                Toast.makeText(ReportForms.this, et.getTag().toString() + " is required", Toast.LENGTH_LONG).show();
                                status = false;
                                break;

                            } else {
                                reportparams.add(new BasicNameValuePair(getIncrementalCounter()+et.getTag().toString(), et.getText().toString()));

                            }

                        } else {
                            et = alEdittext.get(etValidCounter);
                            reportparams.add(new BasicNameValuePair(getIncrementalCounter()+et.getTag().toString(), et.getText().toString()));

                        }


                        etValidCounter++;
                    }

                }
                if (jsonObject.optString("field_type").equalsIgnoreCase("radio")) {

                    if (rgValidCounter < rgcounter) {
                        RadioGroup radioGroup;

                        if (jsonObject.optBoolean("required") == true) {
                            radioGroup = alRadiogroup.get(rgValidCounter);
                            if (radioGroup.getCheckedRadioButtonId() == -1) {
                                Toast.makeText(ReportForms.this, radioGroup.getTag().toString() + " is required", Toast.LENGTH_LONG).show();
                                status = false;
                                break;
                            } else {
                                reportparams.add(new BasicNameValuePair(radioGroup.getTag().toString(), ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString()));
                            }
                        } else {
                            radioGroup = alRadiogroup.get(rgValidCounter);
                            if (radioGroup.getCheckedRadioButtonId() != -1) {
                                reportparams.add(new BasicNameValuePair(radioGroup.getTag().toString(), ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString()));
                            }
                        }

                        rgValidCounter++;
                    }

                }

                if (jsonObject.optString("field_type").equalsIgnoreCase("checkboxes")) {

                    boolean isChecked = false;
                    String send_checkBoxes = "";
                    if (cbValidCounter < cbCounter) {
                        alValidCheckbox = alCheckboxes;
                        if (jsonObject.optBoolean("required") == true) {


                            for (int j = 0; j < alValidCheckbox.get(cbValidCounter).size(); j++) {

                                if (alValidCheckbox.get(cbValidCounter).get(j).isChecked()) {
                                    isChecked = true;
                                    break;

                                }
                            }
                            for (int j = 0; j < alValidCheckbox.get(cbValidCounter).size(); j++) {

                                //reportparams.add(new BasicNameValuePair(alValidCheckbox.get(cbValidCounter).get(j).getText().toString().trim(), alValidCheckbox.get(cbValidCounter).get(j).isChecked() + ""));

                                if (alValidCheckbox.get(cbValidCounter).get(j).isChecked()) {
                                    if (send_checkBoxes.toString().length() == 0) {
                                        send_checkBoxes += alValidCheckbox.get(cbValidCounter).get(j).getText().toString().trim();
                                    } else {
                                        send_checkBoxes += "," + alValidCheckbox.get(cbValidCounter).get(j).getText().toString().trim();
                                    }
                                }
                            }
                            if (!isChecked) {
                                Toast.makeText(ReportForms.this, alValidCheckbox.get(cbValidCounter).get(0).getTag().toString() + " is required", Toast.LENGTH_LONG).show();
                                status = false;
                                break;
                            }/*else{

                                    reportparams.add(new BasicNameValuePair(alValidCheckbox.get(cbValidCounter).get(0).getTag().toString(), alValidCheckbox.get(cbValidCounter).get(0).isChecked()+""));

                                }*/

                        } else {

                            for (int j = 0; j < alValidCheckbox.get(cbValidCounter).size(); j++) {

                                // reportparams.add(new BasicNameValuePair(alValidCheckbox.get(cbValidCounter).get(j).getText().toString().trim(), alValidCheckbox.get(cbValidCounter).get(j).isChecked() + ""));
                                if (send_checkBoxes.toString().length() == 0) {
                                    send_checkBoxes += alValidCheckbox.get(cbValidCounter).get(j).getText().toString().trim();
                                } else {
                                    send_checkBoxes += "," + alValidCheckbox.get(cbValidCounter).get(j).getText().toString().trim();
                                }

                            }
                        }
                        cbValidCounter++;

                    }


                    if (jsonObject.optString("label").equalsIgnoreCase("Areas Serviced (check all that apply)") && report.getReport_name().equalsIgnoreCase("Cleanup Report")) {
                        reportparams.add(new BasicNameValuePair("db_field_4", send_checkBoxes + ""));
                    } else if (jsonObject.optString("label").equalsIgnoreCase("Issues Noticed on Site (check all that apply)") && report.getReport_name().equalsIgnoreCase("Cleanup Report")) {

                        reportparams.add(new BasicNameValuePair("db_field_5", send_checkBoxes + ""));
                    } else if (jsonObject.optString("label").equalsIgnoreCase("Area Serviced (choose all that apply)") && report.getReport_name().equalsIgnoreCase("Spot Salting")) {

                        reportparams.add(new BasicNameValuePair("db_field_1", send_checkBoxes + ""));

                    }


                } else if (jsonObject.optString("field_type").equalsIgnoreCase("dropdown")) {

                    if (spinnerValidCounter <= spCounter) {

                        Spinner spinner;
                        if (jsonObject.optBoolean("required") == true) {
                            spinner = alSpinner.get(spinnerValidCounter);
                            if (spinner.getSelectedItem().toString() == null) {
                                Toast.makeText(ReportForms.this, spinner.getTag().toString() + " is required", Toast.LENGTH_LONG).show();
                                status = false;
                                break;
                            } else {
                                reportparams.add(new BasicNameValuePair(spinner.getTag().toString(), spinner.getSelectedItem().toString()));

                            }
                        } else {
                            spinner = alSpinner.get(spinnerValidCounter);
                            if (spinner.getSelectedItem().toString() != null) {
                                reportparams.add(new BasicNameValuePair(spinner.getTag().toString(), spinner.getSelectedItem().toString()));

                            }
                        }
                        spinnerValidCounter++;
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return status;
    }


    private void initListeners() {

        try {
            if (tvSubmit != null) {
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    public JSONArray jsonArray;

                    @Override
                    public void onClick(View v) {

                        if (validationForm(report.getForm_body() + "")) {
                            reportparams.add(new BasicNameValuePair("rid", report.getReport_id() + ""));
                            reportparams.add(new BasicNameValuePair("db_location", location_id + ""));
                            reportparams.add(new BasicNameValuePair("db_property", property_id + ""));
                            reportparams.add(new BasicNameValuePair("user_id", SPUser.getString(ReportForms.this, SPUser.USER_ID)));
                            jsonArray = new JSONArray();
                            for (int i = 0; i < reportparams.size(); i++) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put(reportparams.get(i).getName().toString(), reportparams.get(i).getValue().toString());
                                    System.out.println("reportparams ==" + i + "==" + reportparams.get(i).getName().toString() + "||" + reportparams.get(i).getValue().toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                jsonArray.put(jsonObject);


                            }
                            if (networkConnection.isOnline(ReportForms.this)) {
                                new CustomHttpClient(ReportForms.this).executeHttp(SPDomain.getString(ReportForms.this, SPDomain.DOMAIN_NAME) + WebUrl.SAVE_REPORT, reportparams, progressDialog, new CustomHttpClient.OnSuccess() {
                                    @Override
                                    public void onSucess(String result) {
                                        try {
                                            //WebUrl.ShowLog(result);
                                            //JSONObject jObject = new JSONObject(result);
                                            // ReportForms.this.finish();

                                            llReportContainer.removeAllViews();
                                            inflateForm(report.getForm_body() + "");
                                            startService(new Intent(ReportForms.this,BackgroundReportRefresh.class));
                                           // refreshList();


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new CustomHttpClient.OnFailure() {
                                    @Override
                                    public void onFailure(String result) {
                                        saveInLocal(jsonArray);
                                    }
                                }, CustomHttpClient.Method.POST, true);
                            } else {
                                saveInLocal(jsonArray);

                              /*  alEdittext = new ArrayList<EditText>();
                                alRadiogroup = new ArrayList<RadioGroup>();
                                 alSpinner = new ArrayList<Spinner>();
                                alCheckboxes = new ArrayList<ArrayList<CheckBox>>();*/
                                reportparams = new ArrayList<NameValuePair>();
                                llReportContainer.removeAllViews();
                                inflateForm(report.getForm_body() + "");

                            }

                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                location_id = alUniqueLocation.get(position).getLocation_id();

                alLocationProperty = new ArrayList<Property>();
                for (int i = 0; i < alMasterData.size(); i++) {
                    if (alMasterData.get(i).getLocation_id() == location_id) {
                        alLocationProperty.add(alMasterData.get(i));
                    }
                }


                spProperty.setAdapter(new ReportPropertyFromLocationAdapter(ReportForms.this, alLocationProperty));


              /*  ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                new CustomHttpClient(ReportForms.this).executeHttp(WebUrl.LOCATION_PROPERTIES+"&lid="+ alMasterData.get(position).getLocation_id()+"", params, progressDialog, new CustomHttpClient.OnSuccess() {
                    @Override
                    public void onSucess(String result) {
                        try {

                            JSONObject jObject = new JSONObject(result);
                            setPropertyData(jObject.optJSONArray("property").toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },null, CustomHttpClient.Method.POST);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spProperty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // property_id=alProperty.get(position).getId();
                property_id = alLocationProperty.get(position).getId();
                if (selected_property_id != 0) {
                    for (int i = 0; i < alLocationProperty.size(); i++) {

                        if (selected_property_id == alLocationProperty.get(i).getId()) {
                            WebUrl.ShowLog(selected_property_id + "====" + alLocationProperty.get(i).getJob_listing() + "==" + alLocationProperty.get(i).getLocation_id());

                            spProperty.setSelection(i);
                            spProperty.setSelected(false);
                            spProperty.setClickable(false);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvHeaderBackTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportForms.this.finish();
            }
        });

    }

    private void saveInLocal(JSONArray jsonArray) {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String formattedDate = df.format(c.getTime());
        dbHolder.addToTable(jsonArray + "", report.getReport_id() + "", location_id + "", property_id + "", formattedDate);
        Toast.makeText(getApplicationContext(), "Report Saved to DataBase", Toast.LENGTH_LONG).show();
    }


    private void setPropertyData(String property) {
        try {


            if ((!property.equalsIgnoreCase("")) && (!property.equals(null)) && (!property.equalsIgnoreCase("null"))) {


                JSONArray jsonArray = new JSONArray(property);
                if (jsonArray.length() > 0) {
                    alProperty = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<LocationProperties>>() {
                    }.getType());

                }
                spProperty.setAdapter(new ReportPropertyAdapter(ReportForms.this, alProperty));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ReportForms Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.jobnotes/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ReportForms Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.jobnotes/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    private int getIncrementalCounter(){
        counter++;
        return counter;
    }


   /* public void refreshList() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("uname", SPUser.getString(ReportForms.this, SPUser.USER_NAME)));
        params.add(new BasicNameValuePair("password", SPUser.getString(ReportForms.this, SPUser.USER_PASSWORD)));
                 *//*   params.add(new BasicNameValuePair("device_id", SPDevice.getValue(activity, SPDevice.DEVICE_ID)));
                    params.add(new BasicNameValuePair("device_token", SPGCM.getValue(activity, SPGCM.GCM_TOKEN) + "123456"));*//*
        new CustomHttpClient(ReportForms.this).executeHttp(SPDomain.getString(ReportForms.this, SPDomain.DOMAIN_NAME) + WebUrl.LOGIN, params, null, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                try {

                    JSONObject jObject = new JSONObject(result);
                    JSONObject jsonObject = jObject.optJSONObject("login_info");
                    if (jsonObject.optString("auto_login").equalsIgnoreCase("1")) {
                        SPUser.setString(ReportForms.this, SPUser.USER_ID, jsonObject.optString("id"));
                    }
                    SPUser.setString(ReportForms.this, SPUser.PROPERTIES, jObject.optJSONObject("properites") + "");
                    SPUser.setString(ReportForms.this, SPUser.REPORTS, jObject.optJSONObject("reports") + "");
                    SPUser.setString(ReportForms.this, SPUser.REPORT_INFO, jObject.optJSONArray("report_info").toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null, CustomHttpClient.Method.POST, false);

    }*/
}
