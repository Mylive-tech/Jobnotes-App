package com.jobnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jobnotes.Pojo.Notes;
import com.jobnotes.Pojo.Property;
import com.jobnotes.Pojo.ReportInfo;
import com.jobnotes.Pojo.Reports;
import com.jobnotes.Utility.SPUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipl-1111 on 7/9/16.
 */
public class ReportListActivity extends Activity {

    private TextView text;
    Property property;
    private ArrayList<Reports> alReports;
    private LinearLayout llList;
    private TextView tvBack;
    private LayoutInflater inflater;
    private ArrayList<ReportInfo> alReportInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        initCoponents();
        initListner();
        text = (TextView) findViewById(R.id.text);
        property = (Property) getIntent().getSerializableExtra("property");
        setDataSideMenu(SPUser.getString(ReportListActivity.this, SPUser.REPORTS));
    }

    private void initListner() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initCoponents() {
        llList = (LinearLayout) findViewById(R.id.ll_report_list);
        tvBack = (TextView) findViewById(R.id.tv_back_report_list);
        inflater = getLayoutInflater();
    }

    private void setDataSideMenu(String string) {
        try {

            JSONObject jsonObject = new JSONObject(string);

            if ((!string.equalsIgnoreCase("")) && (!string.equals(null)) && (!string.equalsIgnoreCase("null"))) {

                if (jsonObject.optJSONArray("report").length() > 0) {

                    alReports = new ArrayList<Reports>();
                    for (int i = 0; i < jsonObject.optJSONArray("report").length(); i++) {
                        JSONObject jsonObject1 = jsonObject.optJSONArray("report").optJSONObject(i);
                        Reports pojo = new Reports();
                        pojo.setReport_id(jsonObject1.optLong("report_id"));
                        pojo.setSite_id(jsonObject1.optLong("site_id"));
                        pojo.setReport_name(jsonObject1.optString("report_name"));
                        pojo.setForm_description(jsonObject1.optString("form_description"));
                        pojo.setSend_to(jsonObject1.optString("send_to"));
                        pojo.setMail_subject(jsonObject1.optString("mail_subject"));
                        pojo.setSubmit_button_text(jsonObject1.optString("submit_button_text"));
                        pojo.setProperty_box_label(jsonObject1.optString("property_box_label"));
                        pojo.setSubmissions(jsonObject1.optString("submissions"));
                        pojo.setDate_added(jsonObject1.optString("date_added"));
                        pojo.setStatus(jsonObject1.optInt("status"));
                        pojo.setForm_body(jsonObject1.optString("form_body"));
                        if (pojo.getReport_id() != 0) {
                            if (("," + property.getEnabled_reports() + ",").contains("," + pojo.getReport_id() + ",")) {
                                alReports.add(pojo);
                            } else {
                                System.out.println("alMasterData name === " + property.getLocation_name());
                            }
                        }

                    }
                    alReportInfo = new Gson().fromJson(SPUser.getString(ReportListActivity.this, SPUser.REPORT_INFO), new TypeToken<List<ReportInfo>>() {
                    }.getType());
                    for (int i = 0; i < alReports.size(); i++) {
                        View revportView = inflater.inflate(R.layout.row_avaliable_property_reports, null);

                        TextView tvReportName = (TextView) revportView.findViewById(R.id.tv_row_avaliable_property_reports_name);
                        TextView tvAction = (TextView) revportView.findViewById(R.id.tv_row_avaliable_property_reports_action);
                        tvReportName.setText(alReports.get(i).getReport_name());
                        tvAction.setText("Not Reported");

                        for (int j = 0; j < alReportInfo.size(); j++) {

                            if (alReportInfo.get(j).getProperty_id() == property.getId() && alReportInfo.get(j).getReport_id() == alReports.get(i).getReport_id()) {
                            tvAction.setText("Reported"+"\n"+"Last on"+"\n"+alReportInfo.get(j).getSubmission_date());
                            break;
                            }

                     /*      tvAction.setTag(alReportInfo.get(finalJ).getProperty_id()+"");
                            tvReportName.setTag(alReportInfo.get(j).getProperty_id()+"");*/


                        }
                        final int finalI = i;
                        tvAction.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(ReportListActivity.this, ReportForms.class).putExtra("report", alReports.get(finalI)).putExtra("location_id",property.getLocation_id()).putExtra("property_id",property.getId()));
                            }
                        });
                        tvReportName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(ReportListActivity.this, ReportForms.class).putExtra("report", alReports.get(finalI)).putExtra("location_id",property.getLocation_id()).putExtra("property_id",property.getId()));
                            }
                        });

                        llList.addView(revportView);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
