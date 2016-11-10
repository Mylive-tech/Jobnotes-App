package com.jobnotes;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.jobnotes.Adapter.ReportsAdapter;
import com.jobnotes.Fragments.DashBoardFragment;
import com.jobnotes.Fragments.PropertyListingFragment;
import com.jobnotes.Fragments.PropertyViewDetaillFragment;
import com.jobnotes.Fragments.SpecialNotesFragment;
import com.jobnotes.Pojo.Property;
import com.jobnotes.Pojo.Reports;
import com.jobnotes.Utility.CustomHttpClient;
import com.jobnotes.Utility.FragmentEntry;
import com.jobnotes.Utility.Message;
import com.jobnotes.Utility.MultipartUtility;
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
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Bundle bundle;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    public FragmentEntry currentFragment;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private LinearLayout llSidebarListConatiner;
    private ListView lvSidebarList;
    private ArrayList<Reports> alReport;
    private ArrayList<Property> alProperty;
    private NetworkConnection networkConnection;
    private DBHolder dbHolder;
    private int id;
    private Cursor cursor;
    private ProgressDialog progressDialog;
    private Cursor cursorNotes;
    private int idNotes;
    private Cursor cursorPhotos;
    private Cursor cursorWorkStatus;
    private int idWorkStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        llSidebarListConatiner = (LinearLayout)/*navigationView.getHeaderView(0).*/findViewById(R.id.sidebar_list_container);
        lvSidebarList = (ListView)/*navigationView.getHeaderView(0).*/findViewById(R.id.sidebar_list);
        setDataSideMenu(SPUser.getString(HomeActivity.this, SPUser.REPORTS));
        navigationView.setNavigationItemSelectedListener(this);
        // getSupportActionBar().setTitle("Home"); //Custom title on action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.navigation_icon); //Custom Menu or home icon  on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo_taskbar);//Custom logo 2nd position  on action bar
         getSupportActionBar().setDisplayShowHomeEnabled(true); //Hide logo 2nd position  on action bar*//*


    //Managed fragment back control and customise navigation toolbar with images here
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager().getBackStackEntryCount() > 0) {


                   // getSupportActionBar().setHomeAsUpIndicator(R.mipmap.navigation_icon); //Custom Menu or home icon  on action bar
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WebUrl.current_frag="dashboard";
                            onBackPressed();
                        }
                    });
                } else {
                    //show hamburger
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                    getSupportActionBar().setHomeAsUpIndicator(R.mipmap.navigation_icon); //Custom Menu or home icon  on action bar
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setIcon(R.mipmap.logo_taskbar);//Custom logo 2nd position  on action bar
                    getSupportActionBar().setDisplayShowHomeEnabled(true); //Hide logo 2nd position  on action bar*/
                 //   getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    toggle.syncState();
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WebUrl.current_frag="dashboard";
                         //   getSupportActionBar().setHomeAsUpIndicator(R.mipmap.navigation_icon); //Custom Menu or home icon  on action bar
                            drawer.openDrawer(GravityCompat.START);
                        }
                    });

                    getSupportActionBar().setHomeAsUpIndicator(R.mipmap.navigation_icon); //Custom Menu or home icon  on action bar

                }


            }
        });
        initComponents();
        initListener();
        permission(0);
        refreshList(0);

        //  changeFragment(FragmentEntry.DASHBOARD, null);
    }

    @Override
    public void onBackPressed() {
        WebUrl.current_frag="dashboard";
        super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (networkConnection.isOnline(HomeActivity.this)) {
            SPUser.setInt(HomeActivity.this,SPUser.SCROLL_POSITION,1);
            sendRecordToServer();
            sendNotesToServer();
            sendPhotosToServer();
            sendWorkStatusToServer();
// notes
        }
    }

    private void sendRecordToServer() {
        cursor = dbHolder.GetAllRecords();
        if (cursor.getCount() > 0) {
            if (cursor.moveToNext()) {

                id = cursor.getInt(0);
                String reportdata = cursor.getString(1);
                ArrayList<NameValuePair> reportparams = new ArrayList<NameValuePair>();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(reportdata);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        Iterator iterator = jsonObject.keys();
                        while (iterator.hasNext()) {
                            String key = (String) iterator.next();
                            reportparams.add(new BasicNameValuePair(key, jsonObject.optString(key)));
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                new CustomHttpClient(HomeActivity.this).executeHttp(SPDomain.getString(HomeActivity.this, SPDomain.DOMAIN_NAME) + WebUrl.SAVE_REPORT, reportparams, null, new CustomHttpClient.OnSuccess() {
                    @Override
                    public void onSucess(String result) {
                        try {
                            //delete row coming firslipl

                            WebUrl.ShowLog("deleted report==== " + id);
                            dbHolder.deleteReport(id + "");
                            if (cursor.getCount() != 1) {
                                sendRecordToServer();
                            }else {
                                refreshList(0);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, null, CustomHttpClient.Method.POST, false);
            }
        }


    }


    public void sendNotesToServer() {
        cursorNotes = dbHolder.GetAllNotes();
        if (cursorNotes.getCount() > 0) {
            if (cursorNotes.moveToNext()) {

                idNotes = cursorNotes.getInt(0);
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

                try {
                    new CustomHttpClient(HomeActivity.this).executeHttp(SPDomain.getString(HomeActivity.this, SPDomain.DOMAIN_NAME) + WebUrl.ADD_NOTES + "&pid=" + cursorNotes.getString(1) + "&uid=" + cursorNotes.getString(2) + "&note=" + URLEncoder.encode(cursorNotes.getString(3), "UTF-8"), params, null, new CustomHttpClient.OnSuccess() {
                        @Override
                        public void onSucess(String result) {

                            try {
                                //delete row coming first

                                WebUrl.ShowLog("deleted Notes==== " + idNotes);
                                dbHolder.deleteNote(idNotes + "");
                                if (cursorNotes.getCount() != 1) {
                                    sendNotesToServer();
                                }else {
                                    refreshList(0);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, null, CustomHttpClient.Method.GET, false);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendPhotosToServer() {
        cursorPhotos = dbHolder.GetAllPhotos();
        if (cursorPhotos.getCount() > 0) {
            if (cursorPhotos.moveToNext()) {


                try {
                    Log.e(MySQLiteHelper.COLUMN_ID, cursorPhotos.getInt(0) + "");
                    Log.e(MySQLiteHelper.COLUMN_PROPERTY_ID, cursorPhotos.getInt(1) + "");
                    Log.e(MySQLiteHelper.COLUMN_PATH, cursorPhotos.getString(2) + "");
                    new PitureUploadTask(cursorPhotos.getInt(0) + "", cursorPhotos.getInt(1) + "", cursorPhotos.getString(2) + "").execute();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }


    private void sendWorkStatusToServer() {
        cursorWorkStatus = dbHolder.GetAllWorkStatus();
        WebUrl.ShowLog("cursorWorkStatus cursor count=="+cursorWorkStatus.getCount());
        if (cursorWorkStatus.getCount() > 0) {
            if (cursorWorkStatus.moveToNext()) {
                idWorkStatus = cursorWorkStatus.getInt(0);

                WebUrl.ShowLog("idWorkStatus cursor =="+cursorWorkStatus.getString(3));

                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

                try {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                            Locale.getDefault()).format(new Date());
                    new CustomHttpClient(HomeActivity.this).executeHttp(SPDomain.getString(HomeActivity.this, SPDomain.DOMAIN_NAME) + WebUrl.UPDATE_STATUS + "&status=" + cursorWorkStatus.getString(3) + "&pid=" + cursorWorkStatus.getString(1) + "&uid=" + cursorWorkStatus.getString(2) + "&date=" + URLEncoder.encode(timeStamp + "", "UTF-8"), params, null, new CustomHttpClient.OnSuccess() {
                        @Override
                        public void onSucess(String result) {

                            try {
                                //delete row coming firslipl

                                WebUrl.ShowLog("deleted idWorkStatus==== " + idWorkStatus);
                                dbHolder.deleteWorkStatus(idWorkStatus + "");
                                if (cursorWorkStatus.getCount() != 1) {
                                    sendWorkStatusToServer();
                                }else {
                                    refreshList(0);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, null, CustomHttpClient.Method.GET, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private void initListener() {
        lvSidebarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //  if(position==0) {
                startActivity(new Intent(HomeActivity.this, ReportForms.class).putExtra("report", alReport.get(position)));
                //  }
                //    changeFragment(FragmentEntry.REPORT, alReport.get(position));
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

            }
        });
    }


    private void setDataSideMenu(String report) {
        try {


            Log.e("REPORTS===", SPUser.getString(HomeActivity.this, SPUser.REPORTS));
            if ((!report.equalsIgnoreCase("")) && (!report.equals(null)) && (!report.equalsIgnoreCase("null"))) {
                JSONObject jsonObject = new JSONObject(report);
                if (jsonObject.optJSONArray("report").length() > 0) {

                    alReport = new ArrayList<Reports>();
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

                        alReport.add(pojo);
                    }

                   /* alReport = new Gson().fromJson(jsonObject.optJSONArray("report").toString(), new TypeToken<List<Reports>>() {
                    }.getType());*/
                    if (alReport != null) {
                        int n = alReport.size();
                        for (int i = 0; i < n; i++) {
                            if (alReport.get(i).getReport_id() == 0) {
                                alReport.remove(i);
                                i--;
                                --n;
                            }
                        }
                    }
                }

                lvSidebarList.setAdapter(new ReportsAdapter(HomeActivity.this, alReport));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initComponents() {


        this.fragmentManager = getFragmentManager();
        networkConnection = new NetworkConnection();
        dbHolder = new DBHolder(HomeActivity.this, HomeActivity.this);
        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);

        Calendar c=Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());
        SimpleDateFormat  df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
       // String formattedDate = "09/21/2016 12:36:48";//df.format(c.getTime());
        String formattedDate = df.format(c.getTime());
        SPUser.setString(HomeActivity.this,SPUser.PREVIOUS_TIME,formattedDate);
    }

    public void changeFragment(FragmentEntry fragmentEntry, Object object) {

        switch (fragmentEntry) {


            case DASHBOARD:
                SPUser.setInt(HomeActivity.this,SPUser.SCROLL_POSITION,1);

                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setHomeAsUpIndicator(R.mipmap.navigation_icon); //Custom Menu or home icon  on action bar
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setIcon(R.mipmap.logo_taskbar);//Custom logo 2nd position  on action bar
                getSupportActionBar().setDisplayShowHomeEnabled(true); //Hide logo 2nd position  on action bar*/

                this.currentFragment = fragmentEntry.DASHBOARD;
                WebUrl.current_frag="dashboard";
                fragment = new DashBoardFragment();
                // toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.side_nav_bar));
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Message.show(HomeActivity.this,"DASHBOARD");
                  /*      Toast.makeText(getApplicationContext(),"Back Clicked",Toast.LENGTH_LONG).show();
                        changeFragment(FragmentEntry.MAP,null);*/

                    }
                });
                break;

            case PROPERTY_LISTING:

                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_arrow); //Custom Menu or home icon  on action bar
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(false);

                // getSupportActionBar().setIcon(R.mipmap.logo_taskbar);//Custom logo 2nd position  on action bar
                WebUrl.current_frag="property_listing";
                Property p = (Property) object;
                getSupportActionBar().setTitle(p.getLocation_name() + "");
                this.currentFragment = fragmentEntry.PROPERTY_LISTING;
                fragment = new PropertyListingFragment();
                bundle = new Bundle();
                bundle.putLong("location_id", p.getLocation_id());
                fragment.setArguments(bundle);
             /*   final Property location_properties_data = (Property) bundle.getSerializable("location_properties_data");
                getSupportActionBar().setTitle(location_properties_data.getLocation_name());*/
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SPUser.setInt(HomeActivity.this,SPUser.SCROLL_POSITION,1);
                        WebUrl.current_frag="dashboard";
                        Message.show(HomeActivity.this,"PROPERTY_LISTING");
                        onBackPressed();
                        if (true) {
                            return;
                        }

                        changeFragment(FragmentEntry.DASHBOARD, null);
                        setSupportActionBar(toolbar);
                        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomeActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                        drawer.setDrawerListener(toggle);
                        toggle.syncState();
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.navigation_icon); //Custom Menu or home icon  on action bar
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setIcon(R.mipmap.logo_taskbar);//Custom logo 2nd position  on action bar
                        getSupportActionBar().setDisplayShowHomeEnabled(true); //Hide logo 2nd position  on action bar*/
                    }
                });
                break;


            case VIEW_DETAIL:
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_arrow); //Custom Menu or home icon  on action bar
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(false);

                // getSupportActionBar().setIcon(R.mipmap.logo_taskbar);//Custom logo 2nd position  on action bar
                WebUrl.current_frag="veiw_detail";
                Property property = (Property) object;
                getSupportActionBar().setTitle(property.getLocation_name() + "");
                this.currentFragment = fragmentEntry.VIEW_DETAIL;
                fragment = new PropertyViewDetaillFragment();
                bundle = new Bundle();
                bundle.putSerializable("property", property);
                fragment.setArguments(bundle);
             /*   final Property location_properties_data = (Property) bundle.getSerializable("location_properties_data");
                getSupportActionBar().setTitle(location_properties_data.getLocation_name());*/
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Message.show(HomeActivity.this,"VIEW_DETAIL");
                        WebUrl.current_frag="dashboard";
                        onBackPressed();
                        if (true) {
                            return;
                        }
                        changeFragment(FragmentEntry.DASHBOARD, null);
                        setSupportActionBar(toolbar);
                        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomeActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                        drawer.setDrawerListener(toggle);
                        toggle.syncState();
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.navigation_icon); //Custom Menu or home icon  on action bar
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setIcon(R.mipmap.logo_taskbar);//Custom logo 2nd position  on action bar
                        getSupportActionBar().setDisplayShowHomeEnabled(true); //Hide logo 2nd position  on action bar*/
                    }
                });

                break;

            case SPECIAL_NOTES:
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_arrow); //Custom Menu or home icon  on action bar
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(false);

                // getSupportActionBar().setIcon(R.mipmap.logo_taskbar);//Custom logo 2nd position  on action bar
                WebUrl.current_frag="special_notes";
                Property propertySpecialNotes = (Property) object;
                getSupportActionBar().setTitle(propertySpecialNotes.getLocation_name() + "");
                this.currentFragment = fragmentEntry.SPECIAL_NOTES;
                fragment = new SpecialNotesFragment();
                bundle = new Bundle();
                bundle.putSerializable("property", propertySpecialNotes);
                fragment.setArguments(bundle);
             /*   final Property location_properties_data = (Property) bundle.getSerializable("location_properties_data");
                getSupportActionBar().setTitle(location_properties_data.getLocation_name());*/
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebUrl.current_frag="dashboard";
                        Message.show(HomeActivity.this,"SPECIAL_NOTES");
                        onBackPressed();
                        if (true) {
                            return;
                        }
                        changeFragment(FragmentEntry.DASHBOARD, null);
                        setSupportActionBar(toolbar);
                        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomeActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                        drawer.setDrawerListener(toggle);
                        toggle.syncState();
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.navigation_icon); //Custom Menu or home icon  on action bar
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setIcon(R.mipmap.logo_taskbar);//Custom logo 2nd position  on action bar
                        getSupportActionBar().setDisplayShowHomeEnabled(true); //Hide logo 2nd position  on action bar*/
                    }
                });
                break;

         /*   default:
                this.currentFragment = fragmentEntry.DASHBOARD;
                fragment = new DashBoardFragment();
                break;*/
        }
        System.out.println("currentFragment " + fragment.getClass().toString() + " " + currentFragment);
        this.transaction = this.fragmentManager.beginTransaction();

        if (!this.fragmentManager.popBackStackImmediate(this.currentFragment.toString(), 0)) {
            this.transaction.addToBackStack(this.currentFragment.toString());
        }

        this.transaction.replace(R.id.f, fragment, currentFragment.toString());
        this.transaction.commit();

    }

   /* @Override
    public void onBackPressed() {
        if (currentFragment == null) {
            currentFragment = FragmentEntry.DASHBOARD;
        }
        switch (currentFragment) {
            case VIEW_DETAIL:
                Message.show(HomeActivity.this,"VIEW_DETAIL in Back");
                fragmentManager.popBackStack();
                currentFragment = FragmentEntry.PROPERTY_LISTING;
                break;
            case SPECIAL_NOTES:
                Message.show(HomeActivity.this,"VIEW_DETAIL in Back");
                currentFragment = FragmentEntry.VIEW_DETAIL;
                fragmentManager.popBackStack();
                break;
            case PROPERTY_LISTING:
                Message.show(HomeActivity.this,"VIEW_DETAIL in Back");
                fragmentManager.popBackStack();
                currentFragment =FragmentEntry.DASHBOARD;
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.openDrawer(GravityCompat.START);
                        }

                        setSupportActionBar(toolbar);
                        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomeActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                        drawer.setDrawerListener(toggle);
                        toggle.syncState();

                    }
                });

                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setHomeAsUpIndicator(R.mipmap.navigation_icon); //Custom Menu or home icon  on action bar
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setIcon(R.mipmap.logo_taskbar);
                getSupportActionBar().setDisplayShowHomeEnabled(true);


                break;

            default:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    super.onBackPressed();
                }
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setHomeAsUpIndicator(R.mipmap.navigation_icon); //Custom Menu or home icon  on action bar
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setIcon(R.mipmap.logo_taskbar);
                getSupportActionBar().setDisplayShowHomeEnabled(true); //Hide logo 2nd position  on action bar*//*
                break;
        }

    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SPUser.clear(HomeActivity.this);
            dbHolder.deleteAll();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void refreshList(final int i) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("uname", SPUser.getString(HomeActivity.this, SPUser.USER_NAME)));
        params.add(new BasicNameValuePair("password", SPUser.getString(HomeActivity.this, SPUser.USER_PASSWORD)));
                 /*   params.add(new BasicNameValuePair("device_id", SPDevice.getValue(activity, SPDevice.DEVICE_ID)));
                    params.add(new BasicNameValuePair("device_token", SPGCM.getValue(activity, SPGCM.GCM_TOKEN) + "123456"));*/
        new CustomHttpClient(HomeActivity.this).executeHttp(SPDomain.getString(HomeActivity.this, SPDomain.DOMAIN_NAME) + WebUrl.LOGIN, params, null, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                try {

                    JSONObject jObject = new JSONObject(result);
                    JSONObject jsonObject = jObject.optJSONObject("login_info");
                    if (jsonObject.optString("auto_login").equalsIgnoreCase("1")) {
                        SPUser.setString(HomeActivity.this, SPUser.USER_ID, jsonObject.optString("id"));
                    }
                    SPUser.setString(HomeActivity.this, SPUser.PROPERTIES, jObject.optJSONObject("properites") + "");
                    SPUser.setString(HomeActivity.this, SPUser.REPORTS, jObject.optJSONObject("reports") + "");
                    SPUser.setString(HomeActivity.this, SPUser.REPORT_INFO, jObject.optJSONArray("report_info").toString());
                    setDataSideMenu(SPUser.getString(HomeActivity.this, SPUser.REPORTS));
                    if(i!=0){
                        changeFragment(FragmentEntry.DASHBOARD, null);}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null, CustomHttpClient.Method.POST, false);

    }


    private class PitureUploadTask extends AsyncTask<Void, Void, Void> {

        private final String id;
        private final String property_id;
        private final String imagePath;
        private String serverImageName;

        public PitureUploadTask(String id, String property_id, String imagePath) {
            this.id = id;
            this.property_id = property_id;
            this.imagePath = imagePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                MultipartUtility mu = new MultipartUtility(SPDomain.getString(HomeActivity.this, SPDomain.DOMAIN_NAME) + WebUrl.UPLOAD_PHOTOS,
                        "UTF-8");
                mu.addFormField("pid", property_id + "");
                mu.addFormField("uid",  SPUser.getString(HomeActivity.this, SPUser.USER_ID) + "");
                if (imagePath != null) {
                    mu.addFilePart("0", imagePath);
                }

                serverImageName = mu.finish();

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("httpUser Exception", e.toString());
                serverImageName = null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(serverImageName + "");
                if (jsonObject.optBoolean("status")) {

                    dbHolder.deletePhotos(id);
                    if (cursorPhotos.getCount() != 1) {
                        sendPhotosToServer();
                    }else {
                        refreshList(0);
                    }
                    //   Toast.makeText(HomeActivity.this,jsonObject.optString("message"),Toast.LENGTH_LONG).show();
                } else {
                    //    Toast.makeText(HomeActivity.this,jsonObject.optString("message"),Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 12) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(HomeActivity.this, "Please grant permission to continue...", Toast.LENGTH_SHORT).show();

            }
        }

    }

    public void permission(int i) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
// Here, thisActivity is the current activity
            if ((ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED)) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    if (i == 0) {
                        ActivityCompat.requestPermissions(HomeActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                12);
                    } else if (i == 1) {
                        ActivityCompat.requestPermissions(HomeActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                12);
                    }
                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(HomeActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            12);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
//                processCaptureOfImage(code);
            }
        } else {
            //          processCaptureOfImage(code);
        }


    }

    public boolean isCameraGranded() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
// Here, thisActivity is the current activity
            if ((ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED)) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    public boolean isReadWriteGranded() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
// Here, thisActivity is the current activity
            if ((ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}