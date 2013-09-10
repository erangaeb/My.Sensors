package com.wasn.Sensors.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.wasn.Sensors.R;
import com.wasn.Sensors.pojo.DrawerItem;

import java.util.ArrayList;

/**
 * Main activity class of MY.sensors
 * Implement navigation drawer here
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class HomeActivity extends FragmentActivity {

    // drawer components
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private HomeActionBarDrawerToggle homeActionBarDrawerToggle;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // set a custom shadow that overlays the main content when the drawer
        // opens
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        initDrawer();
        homeActionBarDrawerToggle = new HomeActionBarDrawerToggle(this, drawerLayout);
        drawerLayout.setDrawerListener(homeActionBarDrawerToggle);

        //FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        //    tx.replace(R.id.main, Fragment.instantiate(HomeActivity.this, "com.wasn.Sensors.ui.SensorListActivity"));
        //tx.commit();
    }

    /**
     * Initialize Drawer menu
     */
    private void initDrawer() {
        // initialize drawer content
        ArrayList<DrawerItem> drawerItemList = new ArrayList<DrawerItem>();
        drawerItemList.add(new DrawerItem("Sensors", R.drawable.sensors));
        drawerItemList.add(new DrawerItem("Friends", R.drawable.friends));
        drawerItemList.add(new DrawerItem("Sharing",R.drawable.share));

        DrawerAdapter adapter= new DrawerAdapter(HomeActivity.this, drawerItemList);

        drawerListView = (ListView) findViewById(R.id.drawer);
        if (drawerListView != null)
            drawerListView.setAdapter(adapter);

        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        homeActionBarDrawerToggle.syncState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        homeActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * {@inheritDoc}
     *
     * Called whenever we call invalidateOptionsMenu()
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerListView);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        menu.findItem(R.id.action_new).setVisible(!drawerOpen);

        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * The action bar home/up should open or close the drawer.
		 * ActionBarDrawerToggle will take care of this.
		 */
        if (homeActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle your other action bar items.
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle open/close behaviours of Navigation Drawer
     */
    private class HomeActionBarDrawerToggle extends ActionBarDrawerToggle {

        public HomeActionBarDrawerToggle(Activity mActivity, DrawerLayout mDrawerLayout){
            super(mActivity, mDrawerLayout, R.drawable.ic_drawer, R.string.ns_menu_open, R.string.ns_menu_close);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onDrawerClosed(View view) {
            getActionBar().setTitle("My.Sensors");
            invalidateOptionsMenu();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onDrawerOpened(View drawerView) {
            getActionBar().setTitle("My.Sensors");
            invalidateOptionsMenu();
        }
    }

    /**
     * Drawer click event handler
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Highlight the selected item, update the title, and close the drawer
            // update selected item and title, then close the drawer
            drawerListView.setItemChecked(position, true);
            drawerLayout.closeDrawer(drawerListView);

           /* if(position == 0) {
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.main, Fragment.instantiate(Drawyer.this, "com.wasn.mysensors.Friend"));
                tx.commit();
            } else if(position==1) {
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.main, Fragment.instantiate(Drawyer.this, "com.wasn.mysensors.Search"));
                tx.commit();
            }*/

        }

    }
}
