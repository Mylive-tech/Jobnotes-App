package com.jobnotes.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jobnotes.Adapter.LocationPropertyAdapter;
import com.jobnotes.Pojo.Property;
import com.jobnotes.R;
import com.jobnotes.Utility.SPUser;
import com.jobnotes.Utility.WebUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PropertyListingFragment extends Fragment {
    ArrayList<Property> alProperty;
    Context context;
    private LayoutInflater inflater;
    private Activity activity;
    private ListView lvProperty;
    private ProgressDialog progressDialog;
    private long location_id;
    private Property property;
    private ArrayList<Property> alLocation;

    public PropertyListingFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_listing,
                container, false);
        activity = getActivity();

        this.inflater = inflater;
        initComponents(view);
        initListeners();
        WebUrl.current_frag="property_listing";

        view.setClickable(true);
        // Restore previous state (including selected item index and scroll position)

       /* view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/
        //  setListData(SPUser.getString(activity,SPUser.PROPERTIES));
/*        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        new CustomHttpClient(activity).executeHttp(WebUrl.LOCATION_PROPERTIES+"&lid="+ location_id, params, progressDialog, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                try {

                    JSONObject jObject = new JSONObject(result);
                    setListData(jObject.optJSONArray("property").toString());

                 } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },null, CustomHttpClient.Method.POST);*/
        return view;
    }



/*

    private void setListData(String property) {
        try {


        if((!property.equalsIgnoreCase(""))&&(!property.equals(null))&&(!property.equalsIgnoreCase("null")))
        {


            JSONArray jsonArray=new JSONArray(property);
            if (jsonArray.length()>0){
                alProperty = new Gson().fromJson(jsonArray.toString(),new TypeToken<List<LocationProperties>>(){}.getType());

            }
            lvProperty.setAdapter(new LocationPropertyAdapter(activity,alProperty));
        }





        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
*/

    @Override
    public void onResume() {
        super.onResume();

        setListData(SPUser.getString(activity, SPUser.PROPERTIES));
        try {
            lvProperty.setSelection(SPUser.getInt(activity,SPUser.SCROLL_POSITION));
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void setListData(String property) {
        try {
            if ((!property.equalsIgnoreCase("")) && (!property.equals(null)) && (!property.equalsIgnoreCase("null"))) {
                JSONObject jsonObject = new JSONObject(property);
                if (jsonObject.optJSONArray("property").length() > 0) {

                    alLocation = new Gson().fromJson(jsonObject.optJSONArray("property").toString(), new TypeToken<List<Property>>() {
                    }.getType());

                }
                 alProperty = new ArrayList<Property>();
                for (int i = 0; i < alLocation.size(); i++) {
                    if (alLocation.get(i).getLocation_id() == location_id) {
                        alProperty.add(alLocation.get(i));
                    }
                }
                lvProperty.setAdapter(new LocationPropertyAdapter(activity, alProperty));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void initComponents(View view) {

        Bundle bundle = this.getArguments();
        location_id = bundle.getLong("location_id");
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);

        lvProperty = (ListView) view.findViewById(R.id.lv_property);


    }

    private void initListeners() {

      /*  lvDashboardLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // ((HomeActivity)activity).changeFragment(FragmentEntry.MAP,null);
                activity.startActivity(new Intent(activity, PriortyScreen.class).putExtra("property", (Serializable) alProperty.get(position)));

            }
        });*/
    }
}
