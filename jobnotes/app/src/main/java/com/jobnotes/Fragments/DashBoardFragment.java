package com.jobnotes.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jobnotes.Adapter.PriortyListAdapter;
import com.jobnotes.Adapter.PropertyAdapter;
import com.jobnotes.HomeActivity;
import com.jobnotes.Pojo.Property;
import com.jobnotes.R;
import com.jobnotes.Utility.FragmentEntry;
import com.jobnotes.Utility.SPUser;
import com.jobnotes.Utility.WebUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * A placeholder fragment containing a simple view.
 */
public class DashBoardFragment extends Fragment {
    ArrayList<Property> alProperty;
    Context context;
    private LayoutInflater inflater;
    private Activity activity;
    private ArrayList<Property> alPriortyList;
    private ListView lvDashboardLocation;
    private ListView lvPriortyListing;
    private ProgressDialog progressDialog;
    private ArrayList<Property> alSortedPropertyList;
    public boolean doubleBackToExitPressedOnce;

    public DashBoardFragment() {


    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard);
        initComponents(this);
        initListeners();

        activity=this;

       // progressDialog.show();
        setListData(SPUser.getString(DashBoardFragment.this, SPUser.PROPERTIES));

        ListUtils.setDynamicHeight(lvDashboardLocation);
        ListUtils.setListViewHeightBasedOnItems(lvPriortyListing);
      //  progressDialog.dismiss();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard,
                container, false);

        activity = getActivity();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);
        this.inflater = inflater;
        initComponents(view);
        initListeners();
        progressDialog.show();
        setListData(SPUser.getString(activity, SPUser.PROPERTIES));

        ListUtils.setDynamicHeight(lvDashboardLocation);
        ListUtils.setListViewHeightBasedOnItems(lvPriortyListing);
        progressDialog.dismiss();
        //  new LoadData().execute();

        // setListData(SPUser.getString(activity, SPUser.PROPERTIES));
         /*   alProperty=new ArrayList<>();
        for (int i=0;i<30;i++){
            Property pojo=new Property();
            pojo.setLocation_name("Location - "+i);
            alProperty.add(pojo);
        }
        lvDashboardLocation.setAdapter(new PropertyAdapter(activity,alProperty));*//*
      *//* ListUtils.setDynamicHeight(lvDashboardLocation);
        ListUtils.setListViewHeightBasedOnItems(lvPriortyListing);*/
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        WebUrl.current_frag = "dashboard";
        SPUser.setInt(activity, SPUser.SCROLL_POSITION, 1);
    }

    private void setListData(String property) {
        try {
            if ((!property.equalsIgnoreCase("")) && (!property.equals(null)) && (!property.equalsIgnoreCase("null"))) {
                JSONObject jsonObject = new JSONObject(property);
                if (jsonObject.optJSONArray("property").length() > 0) {

                    alProperty = new Gson().fromJson(jsonObject.optJSONArray("property").toString(), new TypeToken<List<Property>>() {
                    }.getType());

                }
                alSortedPropertyList = new ArrayList<Property>();
                LinkedHashMap<Long, Property> stringHashMap = new LinkedHashMap<>();
                for (int i = 0; i < alProperty.size(); i++) {
                    stringHashMap.put(alProperty.get(i).getLocation_id(), alProperty.get(i));
                }

                Iterator entries = stringHashMap.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    Long key = (Long) entry.getKey();
                    Property value = (Property) entry.getValue();
                    alSortedPropertyList.add(value);
                }

                lvDashboardLocation.setAdapter(new PropertyAdapter(activity, alSortedPropertyList));
                alPriortyList = new ArrayList<Property>();
                alPriortyList.addAll(alProperty);
                int n = alPriortyList.size();
                for (int i = 0; i < n; i++) {
                    // System.out.println("Priorty name === "+alPriortyList.get(i).getLocation_name());
                    if (alPriortyList.get(i).getPriority_status() == 0) {
                        alPriortyList.remove(i);
                        i--;
                        --n;
                    } else {
                        System.out.println("Priorty name === " + alPriortyList.get(i).getLocation_name());
                    }
                }
                lvPriortyListing.setAdapter(new PriortyListAdapter(activity, alPriortyList));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents(View view) {
        lvDashboardLocation = (ListView) view.findViewById(R.id.lv_dashboard_location);
        lvPriortyListing = (ListView) view.findViewById(R.id.lv_priorty_listing);
    }

    private void initListeners() {
        lvDashboardLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public boolean doubleBackToExitPressedOnce;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //    Toast.makeText(activity, alProperty.get(position)+"", Toast.LENGTH_SHORT).show();
                if (WebUrl.current_frag.equalsIgnoreCase("dashboard")) {
                    // Toast.makeText(activity,"If "+WebUrl.current_frag,Toast.LENGTH_LONG).show();
                    SPUser.setInt(activity, SPUser.SCROLL_POSITION, 0);
                    ((HomeActivity) activity).changeFragment(FragmentEntry.PROPERTY_LISTING, alSortedPropertyList.get(position));
                    // activity.startActivity(new Intent(activity, PriortyScreen.class).putExtra("property", (Serializable) alProperty.get(position)));
                } else {
                    //  Toast.makeText(activity,"else "+WebUrl.current_frag,Toast.LENGTH_LONG).show();
                }
            }
        });
   /*     lvDashboardLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((HomeActivity) activity).changeFragment(FragmentEntry.PROPERTY_LISTING, alSortedPropertyList.get(position));
                return false;
            }
        });*/
    }

    class BackPraced implements Runnable {
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }


        public static boolean setListViewHeightBasedOnItems(ListView listView) {

            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter != null) {

                int numberOfItems = listAdapter.getCount();

                // Get total height of all items.
                int totalItemsHeight = 0;
                for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                    View item = listAdapter.getView(itemPos, null, listView);
                    item.measure(0, 0);
                    totalItemsHeight += item.getMeasuredHeight();
                }

                // Get total height of all item dividers.
                int totalDividersHeight = listView.getDividerHeight() *
                        (numberOfItems - 1) + 500;

                // Set list height.
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = totalItemsHeight + totalDividersHeight;
                listView.setLayoutParams(params);
                listView.requestLayout();

                return true;

            } else {
                return false;
            }

        }

    }

    private class LoadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String property = SPUser.getString(activity, SPUser.PROPERTIES);
                if ((!property.equalsIgnoreCase("")) && (!property.equals(null)) && (!property.equalsIgnoreCase("null"))) {
                    JSONObject jsonObject = new JSONObject(property);
                    if (jsonObject.optJSONArray("property").length() > 0) {

                        alProperty = new Gson().fromJson(jsonObject.optJSONArray("property").toString(), new TypeToken<List<Property>>() {
                        }.getType());

                    }
                    alPriortyList = new ArrayList<Property>();
                    alPriortyList.addAll(alProperty);
                    int n = alPriortyList.size();
                    for (int i = 0; i < n; i++) {
                        if (alPriortyList.get(i).getPriority_status() == 0) {
                            alPriortyList.remove(i);
                            i--;
                            --n;
                        } else {
                            System.out.println("Priorty name === " + alPriortyList.get(i).getLocation_name());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ListUtils.setDynamicHeight(lvDashboardLocation);
            ListUtils.setListViewHeightBasedOnItems(lvPriortyListing);
            lvDashboardLocation.setAdapter(new PropertyAdapter(activity, alProperty));
            lvPriortyListing.setAdapter(new PriortyListAdapter(activity, alPriortyList));
            progressDialog.cancel();
        }
    }
}
