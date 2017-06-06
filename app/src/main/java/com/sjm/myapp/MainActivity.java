package com.sjm.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sjm.myapp.Fragment.AddCust_Fragment;
import com.sjm.myapp.Fragment.Backup;
import com.sjm.myapp.Fragment.BackupRestore;
import com.sjm.myapp.Fragment.Changerent;
import com.sjm.myapp.Fragment.Exp_Customer;
import com.sjm.myapp.Fragment.GetReport;
import com.sjm.myapp.Fragment.NewMonth_rent;
import com.sjm.myapp.Fragment.Search_Fragment;
import com.sjm.myapp.Fragment.Update_Customer;
import com.sjm.myapp.Fragment.manageExp;
import com.sjm.myapp.Fragment.send_Alert;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle(getString(R.string.search).toUpperCase());
        Search_Fragment fragmentMenu = new Search_Fragment();
        replaceFragment(fragmentMenu, Search_Fragment.class.getSimpleName());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            startActivity(new Intent(MainActivity.this,Setting.class));

        }
        else if (id == R.id.action_addlicence) {
            startActivity(new Intent(MainActivity.this,AddLicence.class));

        }
        else if (id == R.id.action_contactus) {
            startActivity(new Intent(MainActivity.this,ContactUs.class));

        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.search) {
            // Handle the camera action
            setTitle(getString(R.string.search).toUpperCase());
            Search_Fragment fragmentMenu = new Search_Fragment();
            replaceFragment(fragmentMenu, Search_Fragment.class.getSimpleName());
        } else if (id == R.id.add_cust) {
            setTitle(getString(R.string.add_cust).toUpperCase());
            AddCust_Fragment addCust_fragment = new AddCust_Fragment();
            replaceFragment(addCust_fragment, AddCust_Fragment.class.getSimpleName());

        } else if (id == R.id.up_cust) {
            setTitle(getString(R.string.up_cust).toUpperCase());
            Update_Customer addCust_fragment = new Update_Customer();
            replaceFragment(addCust_fragment, Update_Customer.class.getSimpleName());
        } else if (id == R.id.new_mon_rent) {
            setTitle(getString(R.string.new_mon_rent).toUpperCase());
            NewMonth_rent addCust_fragment = new NewMonth_rent();
            replaceFragment(addCust_fragment, NewMonth_rent.class.getSimpleName());

        } else if (id == R.id.get_report) {
            setTitle(getString(R.string.get_report).toUpperCase());
            GetReport addCust_fragment = new GetReport();
            replaceFragment(addCust_fragment, GetReport.class.getSimpleName());

        } else if (id == R.id.send_alert) {
            setTitle(getString(R.string.send_alert).toUpperCase());
            send_Alert addCust_fragment = new send_Alert();
            replaceFragment(addCust_fragment, send_Alert.class.getSimpleName());

        } else if (id == R.id.exp_cust) {
            setTitle(getString(R.string.exp_cust).toUpperCase());
            Exp_Customer addCust_fragment = new Exp_Customer();
            replaceFragment(addCust_fragment, Exp_Customer.class.getSimpleName());

        } else if (id == R.id.backup) {
            setTitle(getString(R.string.backup).toUpperCase());
            Backup addCust_fragment = new Backup();
            replaceFragment(addCust_fragment, Backup.class.getSimpleName());

        } else if (id == R.id.rest_back) {

            setTitle(getString(R.string.rest_back).toUpperCase());
            BackupRestore addCust_fragment = new BackupRestore();
            replaceFragment(addCust_fragment, BackupRestore.class.getSimpleName());

        } else if (id == R.id.change_rent) {
            setTitle(getString(R.string.change_rent).toUpperCase());
            Changerent changerent = new Changerent();
            replaceFragment(changerent, Changerent.class.getSimpleName());

        } else if (id == R.id.man_exp) {
            setTitle(getString(R.string.man_exp).toUpperCase());
            manageExp manage = new manageExp();
            replaceFragment(manage, manageExp.class.getSimpleName());

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        boolean isPopped = fm.popBackStackImmediate(tag, 0);
        if (!isPopped && fm.findFragmentByTag(tag) == null) {
            Fragment currentFragment = fm.findFragmentById(R.id.content);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content, fragment, tag);
            ft.addToBackStack(tag);
            ft.commit();
        }
    }

}
