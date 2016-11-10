package com.jobnotes;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.jobnotes.Fragments.AdvancedSettingFragment;
import com.jobnotes.Fragments.LoginFragment;
import com.jobnotes.Utility.FragmentEntry;
import com.jobnotes.Utility.SPDomain;
import com.jobnotes.Utility.SPUser;

public class LoginActivity extends AppCompatActivity {
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private FragmentEntry currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.fragmentManager = getFragmentManager();
        if (!SPDomain.getString(LoginActivity.this, SPDomain.DOMAIN_NAME).equalsIgnoreCase("")){
        //changeFragment(FragmentEntry.ADVANCED_SETTING,null);
            changeFragment(FragmentEntry.LOGIN,null);
        }else {
            changeFragment(FragmentEntry.ADVANCED_SETTING,null);
        }

    }

    public void changeFragment(FragmentEntry fragmentEntry, Object object) {

        switch (fragmentEntry) {
            case LOGIN:
                this.currentFragment = fragmentEntry.LOGIN;
                fragment = new LoginFragment();
                break;

            case ADVANCED_SETTING:
                this.currentFragment = fragmentEntry.ADVANCED_SETTING;
                fragment = new AdvancedSettingFragment();
                break;
            default:
                this.currentFragment = fragmentEntry.LOGIN;
                fragment = new LoginFragment();
                break;
        }

        this.transaction = this.fragmentManager.beginTransaction();

        /*if (!this.fragmentManager.popBackStackImmediate(this.currentFragment.toString(), 0)) {
            this.transaction.addToBackStack(this.currentFragment.toString());
        }
*/
        this.transaction.replace(R.id.fragment, fragment, currentFragment.toString());
        this.transaction.commit();

    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            //fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-1).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
}
