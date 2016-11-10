package com.jobnotes;

import android.app.Activity;
import android.graphics.Color;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.jobnotes.Adapter.ReportSpinnerAdapter;
import com.jobnotes.Utility.Fonts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.graphics.Color.parseColor;

/**
 * Created by lipl on 28/7/16.
 */
public class MagicInflator {

    Activity activity;

    public MagicInflator(Activity activity) {
        this.activity = activity;

    }

    public EditText getEditText(JSONObject jsonObject, LinearLayout llReportContainer) {
        View child = activity.getLayoutInflater().inflate(R.layout.report_container_layout, null);
        LinearLayout llSubReportContainer = (LinearLayout) child.findViewById(R.id.ll_subreport_container);


        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(activity);
        tv.setLayoutParams(lparams);
        tv.setText(jsonObject.optString("label"));
        tv.setTextColor(parseColor("#46505c"));
        tv.setTypeface(Fonts.getBoldFont(activity));
        tv.setTextSize(15f);
        llSubReportContainer.addView(tv);

        LinearLayout.LayoutParams etparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        EditText editText = new EditText(activity);
        //etparams.setMargins(0, 10, 0, 20);
        editText.setLayoutParams(etparams);
        if (jsonObject.optString("field_type").equalsIgnoreCase("text")) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (jsonObject.optString("field_type").equalsIgnoreCase("number")) {
            editText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            editText.setKeyListener(DigitsKeyListener.getInstance(false,true));
        } else if (jsonObject.optString("field_type").equalsIgnoreCase("paragraph")) {
            editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        }
        editText.setBackgroundResource(R.drawable.field_bg);
        editText.setPadding(10, 5, 10, 5);
        editText.setTextSize(13f);
        editText.setId(jsonObject.optString("label").hashCode());

        editText.setTag(jsonObject.optString("label"));
        editText.setHint("Type here");

        llSubReportContainer.addView(editText);
        llReportContainer.addView(llSubReportContainer);
       /* try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return editText;
    }


    public Spinner getSpinner(JSONObject jsonObject, LinearLayout llReportContainer) {
        View child = activity.getLayoutInflater().inflate(R.layout.report_container_layout, null);
        LinearLayout llSubReportContainer = (LinearLayout) child.findViewById(R.id.ll_subreport_container);


        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(activity);
        tv.setLayoutParams(lparams);
        tv.setText(jsonObject.optString("label"));
        tv.setTextColor(parseColor("#46505c"));
        tv.setTypeface(Fonts.getBoldFont(activity));
        tv.setTextSize(15f);
        llSubReportContainer.addView(tv);

        LinearLayout.LayoutParams spinnerparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Spinner spinner = new Spinner(activity);
        spinner.setLayoutParams(spinnerparams);
        spinner.setPadding(2, 2, 2, 2);
        spinner.setTag(jsonObject.optString("label"));
        spinner.setId(jsonObject.optString("label").hashCode());
        spinner.setBackgroundResource(R.drawable.spinner_background);
        JSONObject jfeildOptionObject = jsonObject.optJSONObject("field_options");
        JSONArray jOptionArray = jfeildOptionObject.optJSONArray("options");
        ArrayList<String> alSpinner = new ArrayList<String>();
        for (int i = 0; i < jOptionArray.length(); i++) {
            JSONObject jsonOptionObject = jOptionArray.optJSONObject(i);
            alSpinner.add(jsonOptionObject.optString("label"));
        }
        spinner.setAdapter(new ReportSpinnerAdapter(activity, alSpinner));
        llSubReportContainer.addView(spinner);
        llReportContainer.addView(llSubReportContainer);
        return spinner;
    }


    public RadioGroup getRadioGroup(JSONObject jsonObject, LinearLayout llReportContainer) {
        View child = activity.getLayoutInflater().inflate(R.layout.report_container_layout, null);
        LinearLayout llSubReportContainer = (LinearLayout) child.findViewById(R.id.ll_subreport_container);


        TextView tv = new TextView(activity);
        LinearLayout.LayoutParams tvparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        tv.setLayoutParams(tvparams);

        tv.setText(jsonObject.optString("label"));
        tv.setTextColor(parseColor("#46505c"));
        tv.setTypeface(Fonts.getBoldFont(activity));
        tv.setTextSize(15f);
        llSubReportContainer.addView(tv);

        RadioGroup radioGroup = new RadioGroup(activity);

        LinearLayout.LayoutParams rgparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        radioGroup.setLayoutParams(rgparams);
        radioGroup.setTag(jsonObject.optString("label"));
        radioGroup.setId(jsonObject.optString("label").hashCode());
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        JSONObject jfeildOptionObject = jsonObject.optJSONObject("field_options");
        JSONArray jOptionArray = jfeildOptionObject.optJSONArray("options");

        for (int i = 0; i < jOptionArray.length(); i++) {
            JSONObject jsonOptionObject = jOptionArray.optJSONObject(i);
            LinearLayout.LayoutParams rbparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            RadioButton rb = new RadioButton(activity);
            rb.setText(jsonOptionObject.optString("label"));
            //rb.setId(jsonOptionObject.optString("label").hashCode());
            rb.setTextColor(parseColor("#46505c"));
            rb.setTextSize(14f);
            rb.setPadding(20, 0, 0, 0);
            rb.setButtonDrawable(R.drawable.radio_selecter);
            rb.setLayoutParams(rbparams);
            radioGroup.addView(rb);
        }


        llSubReportContainer.addView(radioGroup);
        llReportContainer.addView(llSubReportContainer);
        return radioGroup;
    }


    public ArrayList<CheckBox> getCheckBoxes(JSONObject jsonObject, LinearLayout llReportContainer) {
        View child = activity.getLayoutInflater().inflate(R.layout.report_container_layout, null);
        LinearLayout llSubReportContainer = (LinearLayout) child.findViewById(R.id.ll_subreport_container);

        TextView tv = new TextView(activity);
        LinearLayout.LayoutParams tvparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        tv.setLayoutParams(tvparams);

        tv.setText(jsonObject.optString("label"));
        tv.setTextColor(parseColor("#46505c"));
        tv.setTypeface(Fonts.getBoldFont(activity));
        tv.setTextSize(15f);
        llSubReportContainer.addView(tv);


        JSONObject jfeildOptionObject = jsonObject.optJSONObject("field_options");
        JSONArray jOptionArray = jfeildOptionObject.optJSONArray("options");
        ArrayList<CheckBox> alCheckBoxes = new ArrayList<CheckBox>();

        for (int i = 0; i < jOptionArray.length(); i++) {

            JSONObject jsonOptionObject = jOptionArray.optJSONObject(i);
            LinearLayout.LayoutParams rbparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            CheckBox cb = new CheckBox(activity);
            cb.setText(jsonOptionObject.optString("label"));
            cb.setTag(jsonObject.optString("label"));
            cb.setId(jsonOptionObject.optString("label").hashCode());
            cb.setTextColor(parseColor("#46505c"));
            cb.setTextSize(16f);
            cb.setPadding(20, 0, 0, 0);
            cb.setButtonDrawable(R.drawable.cb_selecter);
            cb.setLayoutParams(rbparams);
            llSubReportContainer.addView(cb);
            alCheckBoxes.add(cb);
        }

        llReportContainer.addView(llSubReportContainer);

        return alCheckBoxes;
    }


    public TextView getTextView(JSONObject jsonObject, LinearLayout llReportContainer) {
        View child = activity.getLayoutInflater().inflate(R.layout.report_container_layout, null);
        LinearLayout llSubReportContainer = (LinearLayout) child.findViewById(R.id.ll_subreport_container);


        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(activity);
        tv.setLayoutParams(lparams);
        tv.setText(jsonObject.optString("label"));
        tv.setTextColor(parseColor("#026cf3"));
        tv.setTypeface(Fonts.getBoldFont(activity));
        tv.setTextSize(18f);
        llSubReportContainer.addView(tv);
        llReportContainer.addView(llSubReportContainer);
        return tv;
    }


    public void magicSeperator(LinearLayout llReportContainer) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, 15);
        TextView tv = new TextView(activity);
        tv.setLayoutParams(lparams);
        tv.setBackgroundColor(Color.parseColor("#d5e1f1"));
        llReportContainer.addView(tv);

    }

    public TextView getSubmitbutton(String buttonText, LinearLayout llReportContainer) {

        View child = activity.getLayoutInflater().inflate(R.layout.report_container_layout, null);
        LinearLayout llSubReportContainer = (LinearLayout) child.findViewById(R.id.ll_subreport_container);
        llSubReportContainer.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tv = new TextView(activity);
        tv.setLayoutParams(lparams);
        tv.setGravity(Gravity.CENTER);

        tv.setTypeface(Fonts.getBoldFont(activity));
        tv.setPadding(20, 10, 20, 10);
        tv.setTextColor(Color.parseColor("#ffffff"));
        tv.setText(buttonText);
        tv.setBackgroundColor(Color.parseColor("#026cf3"));
        llSubReportContainer.addView(tv);
        llReportContainer.addView(llSubReportContainer);
        return tv;
    }
}
