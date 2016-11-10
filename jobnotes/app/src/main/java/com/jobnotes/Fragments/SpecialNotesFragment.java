package com.jobnotes.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.jobnotes.Adapter.NotesAdapter;
import com.jobnotes.Adapter.StringNotesAdapter;
import com.jobnotes.DBHolder;
import com.jobnotes.HomeActivity;
import com.jobnotes.Pojo.Notes;
import com.jobnotes.Pojo.Property;
import com.jobnotes.R;
import com.jobnotes.Utility.CustomHttpClient;
import com.jobnotes.Utility.Fonts;
import com.jobnotes.Utility.NetworkConnection;
import com.jobnotes.Utility.SPDomain;
import com.jobnotes.Utility.SPUser;
import com.jobnotes.Utility.WebUrl;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SpecialNotesFragment extends Fragment {
    private Activity activity;
    private ProgressDialog progressDialog;
    private Property property;
    private Typeface boldFonts;
    private Typeface mediumFonts;
    private ListView lvSpecialNotes;
    private TextView tvAddNotes, tvSpecialNotesTitle;
    private DBHolder dbHolder;
    private ArrayList<Notes> alNotes=new ArrayList<Notes>();
  //  private ArrayList<String> alNotes;
    private Cursor cursorNotes;
    private NetworkConnection networkConnection;
    private SimpleDateFormat df;
    private Calendar c;

    //  private String[] arrNotes;
    public SpecialNotesFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_notes,
                container, false);
        activity = getActivity();

        networkConnection = new NetworkConnection();
        initComponents(view);
        initListeners();
     //   alNotes = new ArrayList<>();
   /*     if (!networkConnection.isOnline(activity)) {
            cursorNotes = dbHolder.GetPropertyNotes(property.getId());
            if (cursorNotes.getCount() > 0) {
                if (cursorNotes.moveToNext()) {
                    Notes notes = new Notes();
                    notes.setNote(cursorNotes.getString(2));
                    alNotes.add(notes);

                }
            }
            lvSpecialNotes.setAdapter(new NotesAdapter(activity, alNotes));
        }*/
        //else {
   /*         ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            new CustomHttpClient(activity).executeHttp(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.DISPLAY_NOTES + "&pid=" + property.getId(), params, progressDialog, new CustomHttpClient.OnSuccess() {
                @Override
                public void onSucess(String result) {
                    try {

                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.optJSONArray("result").length() > 0) {

                            alNotes = new Gson().fromJson(jsonObject.optJSONArray("result").toString(), new TypeToken<List<Notes>>() {
                            }.getType());
show notes from login
                            lvSpecialNotes.setAdapter(new NotesAdapter(activity, alNotes));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, null, CustomHttpClient.Method.GET, true);*/
      //  }
            if (property.isHide()) {
                tvAddNotes.setVisibility(View.GONE);
            }
            return view;
        }


    @Override
    public void onResume() {
        super.onResume();
        if (networkConnection.isOnline(activity)) {
       ((HomeActivity) activity).sendNotesToServer();
  }
    }

    private void initComponents(View view) {
        dbHolder = new DBHolder(activity, activity);
        Bundle bundle = this.getArguments();

        property = (Property) bundle.getSerializable("property");
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);
        boldFonts = Fonts.getBoldFont(activity);
        mediumFonts = Fonts.getMediumFont(activity);

        lvSpecialNotes = (ListView) view.findViewById(R.id.lv_add_notes_special_notes);
        tvSpecialNotesTitle = (TextView) view.findViewById(R.id.tv_add_notes_special_notes_text);
        tvAddNotes = (TextView) view.findViewById(R.id.tv_add_notes);


        tvSpecialNotesTitle.setTypeface(mediumFonts);
        tvAddNotes.setTypeface(boldFonts);



       /* JsonData gsonData = new Gson().fromJson(property.getNotes(), String.class);
        for (int j = 0; j < gsonData.getCount(); j++)
        {
            String customerid = gsonData.get(j).getCustomerId();
            String customername = gsonData.get(j).getCustomerName();
            String location = gsonData.get(j).getLocation();
        }*/


      /*  alNotes= new Gson().fromJson(property.getNotes().toString(), new TypeToken<List<Notes>>() {
        }.getType());
        property.getNotes();*/
         c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

      //  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

        String formattedDate = df.format(c.getTime());
        WebUrl.ShowLog("formattedDate=="+formattedDate);
        setNotesInList();
        try {


            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();


            new CustomHttpClient(activity).executeHttp(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.DISPLAY_NOTES + "&pid=" + property.getId(), params, null, new CustomHttpClient.OnSuccess() {
                @Override
                public void onSucess(String result) {
                    try {
                        setNotesOnSucessInList(result);
                        updateInLocal();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, null, CustomHttpClient.Method.GET, true);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setNotesInList() {
        try {


        //if (alNotes!=null){
         alNotes = new ArrayList<>();
        alNotes.addAll(property.getNotes());
       // }


        int n =alNotes.size();
        for (int i = 0; i < n; i++) {
            /*if (alNotes.get(i).getNote().toString().equalsIgnoreCase("none")||alNotes.get(i).getNote().toString().equalsIgnoreCase("null")||alNotes.get(i).getNote().toString().equalsIgnoreCase("")||alNotes.get(i).getNote().toString()==null) {
                alNotes.remove(i);
                i--;
                --n;
            }*/
            if ((!alNotes.get(i).getNote().toString().equalsIgnoreCase("none"))||(!alNotes.get(i).getNote().toString().equalsIgnoreCase("null"))||(!alNotes.get(i).getNote().toString().equalsIgnoreCase(""))||(alNotes.get(i).getNote().toString()!=null)||(!alNotes.get(i).getNote().toString().equalsIgnoreCase(null))) {

            }else {
                alNotes.remove(i);
                i--;
                --n;
            }
        }
       /*append data in list from local DB below*/
        cursorNotes = dbHolder.GetPropertyNotes(property.getId());
        if (cursorNotes.getCount() > 0) {
            while (cursorNotes.moveToNext()) {
                WebUrl.ShowLog("cursorNotes=="+cursorNotes.getString(1)+"==="+cursorNotes.getString(3));
                Notes notes = new Notes();
                notes.setname("");
                notes.setDate(df.format(c.getTime()));
                notes.setNote(cursorNotes.getString(3));
                alNotes.add(notes);

            }
            cursorNotes.close();
        }
            lvSpecialNotes.setAdapter(new NotesAdapter(activity, alNotes));
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /*Set data after Adding new notes (ONLINE) onSucess*/
    private void setNotesOnSucessInList(String result) {
      try {

            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getBoolean("status")){


            if (jsonObject.optJSONArray("result").length() > 0) {

                alNotes.clear();
                alNotes=new ArrayList<Notes>();
                JSONArray jsonArray=jsonObject.optJSONArray("result");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.optJSONObject(i);
                    Notes notes=new Notes();
                    notes.setDate(jsonObject1.optString("date"));
                    notes.setname(jsonObject1.optString("staff_name"));
                    notes.setNote(jsonObject1.optString("note"));
                    alNotes.add(notes);
                }
              //  alNotes = new Gson().fromJson(jsonObject.optJSONArray("result").toString(), new TypeToken<List<Notes>>() {}.getType());
                // /*append data in list from local DB below*/
                cursorNotes = dbHolder.GetPropertyNotes(property.getId());
                if (cursorNotes.getCount() > 0) {
                    while (cursorNotes.moveToNext()) {
                        WebUrl.ShowLog("cursorNotes=="+cursorNotes.getString(1)+"==="+cursorNotes.getString(3));
                        Notes notes = new Notes();
                        notes.setname("");
                        notes.setDate(df.format(c.getTime()));
                        notes.setNote(cursorNotes.getString(3));
                        alNotes.add(notes);

                    }
                    cursorNotes.close();
                }

                lvSpecialNotes.setAdapter(new NotesAdapter(activity, alNotes));
                updateInLocal();
            }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initListeners() {


        tvAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.add_notes_dialog);

                TextView tvDialogTitle = (TextView) dialog.findViewById(R.id.tv_add_notes_dialog_title);
                final EditText etNotes = (EditText) dialog.findViewById(R.id.et_add_notes_dialog);
                TextView tvSubmit = (TextView) dialog.findViewById(R.id.tv_add_notes_dialog_submit);
                tvDialogTitle.setTypeface(Fonts.getMediumFont(activity));
                etNotes.setTypeface(Fonts.getMediumFont(activity));
                tvSubmit.setTypeface(Fonts.getBoldFont(activity));

                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etNotes.getText().length() != 0) {


                            try {
                            Notes notes=new Notes();
                            notes.setDate(df.format(c.getTime()));
                            notes.setname("");
                            notes.setNote(etNotes.getText().toString());
                            alNotes.add(notes);
                            lvSpecialNotes.setAdapter(new NotesAdapter(activity, alNotes));


                            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();



                                new CustomHttpClient(activity).executeHttp(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.ADD_NOTES + "&pid=" + property.getId() + "&uid=" + SPUser.getString(activity, SPUser.USER_ID) + "&note=" + URLEncoder.encode(etNotes.getText().toString(), "UTF-8"), params, null, new CustomHttpClient.OnSuccess() {
                                    @Override
                                    public void onSucess(String result) {
                                        try {


                                            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();


                                            new CustomHttpClient(activity).executeHttp(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.DISPLAY_NOTES + "&pid=" + property.getId(), params, null, new CustomHttpClient.OnSuccess() {
                                                @Override
                                                public void onSucess(String result) {
                                                    try {
                                                         setNotesOnSucessInList(result);
                                                        updateInLocal();

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, null, CustomHttpClient.Method.GET, true);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new CustomHttpClient.OnFailure() {
                                    @Override
                                    public void onFailure(String result) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(result);
                                            if (jsonObject.optString("message").equals("Please Provide Internet.")) {
                                                dbHolder.addNotes(property.getId() + "", SPUser.getString(activity, SPUser.USER_ID) + "", etNotes.getText().toString());
                                                Toast.makeText(activity, "Notes Saved to DataBase", Toast.LENGTH_LONG).show();
                                                setNotesInList();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, CustomHttpClient.Method.GET, true);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();

                        } else {
                            Toast.makeText(activity, "Notes is required", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialog.show();

            }
        });


    }

    private void updateInLocal() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("uname", SPUser.getString(activity, SPUser.USER_NAME)));
        params.add(new BasicNameValuePair("password", SPUser.getString(activity, SPUser.USER_PASSWORD)));

                 /*   params.add(new BasicNameValuePair("device_id", SPDevice.getValue(activity, SPDevice.DEVICE_ID)));
                    params.add(new BasicNameValuePair("device_token", SPGCM.getValue(activity, SPGCM.GCM_TOKEN) + "123456"));*/
        new CustomHttpClient(activity).executeHttp(SPDomain.getString(activity, SPDomain.DOMAIN_NAME) + WebUrl.LOGIN, params, null, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                try {

                    JSONObject jObject = new JSONObject(result);
                    JSONObject jsonObject = jObject.optJSONObject("login_info");
                    if (jsonObject.optString("auto_login").equalsIgnoreCase("1")) {
                        SPUser.setString(activity, SPUser.USER_ID, jsonObject.optString("id"));
                    }
                    SPUser.setString(activity, SPUser.PROPERTIES, jObject.optJSONObject("properites") + "");
                    SPUser.setString(activity, SPUser.REPORTS, jObject.optJSONObject("reports") + "");
                    SPUser.setString(activity, SPUser.REPORT_INFO, jObject.optJSONArray("report_info").toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(4000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            setNotesInList();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null, CustomHttpClient.Method.POST, true);
    }


}
