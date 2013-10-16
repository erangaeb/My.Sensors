package com.wasn.Sensors.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.wasn.Sensors.R;
import com.wasn.Sensors.application.SensorApplication;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Activity class for sharing
 * Implement sharing related functions
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class ShareActivity extends Activity {

    SensorApplication application;

    // location
    //  1. location provider
    //  2. location manager
    //  3. location listener
    private static final String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;
    LocationManager locationManager;
    Location lastKnowLocation;
    LocationListener locationListener;

    // layout components
    EditText emailEditText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_layout);

        application = (SensorApplication) getApplication();

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        initUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        //if(locationManager!=null)
            //locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        //if(locationManager!=null)
            //locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(locationManager != null) {
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    private void initUI() {
        emailEditText = (EditText) findViewById(R.id.share_layout_email_text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, HomeActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                break;
            case R.id.action_share:
                // share sensor data
                String query = "SHARE" + " " + "#gps" + " " + "@"+emailEditText.getText().toString().trim();
                if(application.getWebSocketConnection().isConnected())
                        application.getWebSocketConnection().sendTextMessage(query);
                finish();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize location mangers to listen location changes
     */
    private void initLocationListener() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        lastKnowLocation = locationManager.getLastKnownLocation(LOCATION_PROVIDER);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                new GetAddressTask(ShareActivity.this).execute(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }

    /**
     * Find address of given location
     * Geocoder.getFromLocation() method is synchronous, and may take a long time to do its work,
     * So you should call the method from the doInBackground() method of an AsyncTask
     */
    private class GetAddressTask extends AsyncTask<Location, Void, String> {
        Context mContext;
        public GetAddressTask(Context context) {
            super();
            mContext = context;
        }

        /**
         * Get a Geocoder instance, get the latitude and longitude
         * look up the address, and return it
         *
         * @params params One or more Location objects
         * @return A string containing the address of the current
         * location, or an empty string if no address can be found,
         * or an error message
         */
        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            // Get the current location from the input parameter list
            Location loc = params[0];
            // Create a list to contain the result address
            List<Address> addresses;
            try {
                // return 1 address.
                addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            } catch (IOException e1) {
                e1.printStackTrace();
                return "NOT_AVAILABLE";
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
                return "NOT_AVAILABLE";
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);

                // use address line and locality as location
                return address.getAddressLine(0) + ", " +address.getLocality();
            } else {
                return "NOT_AVAILABLE";
            }
        }

        @Override
        protected void onPostExecute(String address) {
            // add address lo sensor list
            if(!address.equalsIgnoreCase("NOT_AVAILABLE")) {
                // application.setLocation(address);
                // sensorValueTextView.setText(address);
            }
        }
    }
}
