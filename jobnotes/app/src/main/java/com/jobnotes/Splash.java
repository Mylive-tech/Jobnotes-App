package com.jobnotes;

import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.jobnotes.Utility.SPDomain;
import com.jobnotes.Utility.SPUser;
import com.jobnotes.Utility.WebUrl;


/*import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;*/

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;


public class Splash extends AppCompatActivity {

    public static Splash splash;
    static boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash = Splash.this;
        if(!SPDomain.getBooleanValue(Splash.this,SPDomain.SHORTCUT)){
            ShortcutIcon();
        }
       /* if (isFirstTime) {
            isFirstTime = false;
            SPDomain.setString(Splash.this, SPDomain.DOMAIN_NAME, WebUrl.BASE_URL);
            Log.e("SPLASH==DOMAIN_NAME==", SPUser.getString(Splash.this,SPDomain.DOMAIN_NAME));
        }*/

        //FOR AUTO SIGN OUT (This function will compare time for IDLE 8 hours then logout new time saved on splash and old time saved in Home Activity)
        CheckTimeToSignOut();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent;


                if (SPUser.getString(Splash.this, SPUser.USER_ID).equalsIgnoreCase("")) {
                    intent = new Intent(Splash.this, LoginActivity.class);
                } else {
                    intent = new Intent(Splash.this, HomeActivity.class);
                }
                startActivity(intent);
                Splash.this.finish();
            }
        }, 2000);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void ShortcutIcon(){

        Intent shortcutIntent = new Intent(getApplicationContext(), Splash.class);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
        SPDomain.setBooleanValue(Splash.this,SPDomain.SHORTCUT,true);
    }

    private void CheckTimeToSignOut(){
        Calendar c=Calendar.getInstance();

        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat  df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        String formattedDate = df.format(c.getTime());
        String dateStart = SPUser.getString(Splash.this,SPUser.PREVIOUS_TIME);//"01/14/2012 09:29:58";
        String dateStop = formattedDate;//"01/15/2012 10:31:48";

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;


        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.");

            if(diffHours>=8){
                SPUser.clear(Splash.this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
