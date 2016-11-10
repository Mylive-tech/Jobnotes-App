package com.jobnotes.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.jobnotes.Utility.SPDomain;
import com.jobnotes.Utility.SPUser;
import com.jobnotes.Utility.WebUrl;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lipl on 14/10/16.
 */
public class BackgroundReportRefresh extends Service {
    Activity activity;

    @Override
    public void onCreate() {
        System.out.println("NineService alram arraylist ----->" + "oncreate");
        new Sync().execute();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        //Log.v("Service stop","service stop");
        System.out.print("service stop");
        super.onDestroy();

    }

    @SuppressWarnings("deprecation")

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    public void refreshList() {
        try {
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("uname", SPUser.getString(BackgroundReportRefresh.this, SPUser.USER_NAME)));
            params.add(new BasicNameValuePair("password", SPUser.getString(BackgroundReportRefresh.this, SPUser.USER_PASSWORD)));

            HttpClient client = new DefaultHttpClient();
            HttpResponse r;
            HttpPost post = new HttpPost(SPDomain.getString(BackgroundReportRefresh.this, SPDomain.DOMAIN_NAME) + WebUrl.LOGIN);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);
            r = client.execute(post);
            String value = EntityUtils.toString(r.getEntity());
            JSONObject jObject = new JSONObject(value);
            JSONObject jsonObject = jObject.optJSONObject("login_info");
            if (jsonObject.optString("auto_login").equalsIgnoreCase("1")) {
                SPUser.setString(BackgroundReportRefresh.this, SPUser.USER_ID, jsonObject.optString("id"));
            }
            SPUser.setString(BackgroundReportRefresh.this, SPUser.PROPERTIES, jObject.optJSONObject("properites") + "");
            SPUser.setString(BackgroundReportRefresh.this, SPUser.REPORTS, jObject.optJSONObject("reports") + "");
            SPUser.setString(BackgroundReportRefresh.this, SPUser.REPORT_INFO, jObject.optJSONArray("report_info").toString());
            stopService(new Intent(BackgroundReportRefresh.this,BackgroundReportRefresh.class));
            onDestroy();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class Sync extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            refreshList();
            return null;
        }
    }
}

