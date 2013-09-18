package com.wasn.Sensors.ui;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.wasn.Sensors.R;
import com.wasn.Sensors.application.SensorApplication;
import com.wasn.Sensors.pojo.Sensor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Display sensor list/ Fragment
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class SensorList extends Fragment implements SensorEventListener {
    SensorApplication application;

    // use to populate list
    private ListView sensorListView;
    private ArrayList<Sensor> sensorList;
    private SensorListAdapter adapter;

    // manager for sensors
    private SensorManager sensorManager;

    // location
    //  1. location provider
    //  2. location manager
    //  3. location listener
    private static final String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;
    LocationManager locationManager;
    Location lastKnowLocation;
    LocationListener locationListener;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        application = (SensorApplication) getActivity().getApplication();

        // after created activity
        //  1. initialize sensor list
        //  2. initialize sensors
        //  3. create sensor list
        //  4. initialize location listener
        initSensorList();
        initSensors();
        initLocationListener();
        initSensorListView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sensor_list_layout, null);
        initUI(root);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();

        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        // register for get location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    /**
     * Initialize UI components
     */
    private void initUI(View view) {
        sensorListView = (ListView)view.findViewById(R.id.sensor_list_layout_sensor_list);

        // add header and footer for list
        View headerView = View.inflate(this.getActivity(), R.layout.list_header, null);
        View footerView = View.inflate(this.getActivity(), R.layout.list_header, null);
        sensorListView.addHeaderView(headerView);
        sensorListView.addFooterView(footerView);
    }

    /**
     * Initialize sensor list to be display
     * Initially add location sensor to list
     */
    private void initSensorList() {
        sensorList = new ArrayList<Sensor>();
        if(application.getLocation().equalsIgnoreCase("NOT AVAILABLE")) {
            sensorList.add(new Sensor("Location", application.getLocation(), false));
        } else {
            sensorList.add(new Sensor("Location", application.getLocation(), true));
        }
    }

    /**
     * Initialize sensor managers and sensor list
     * Get available sensors and current sensor data
     */
    private void initSensors() {
        // get all sensors manager
        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);

        // Listen for environment sensors
        //  1. TYPE_AMBIENT_TEMPERATURE - Ambient air temperature
        //  2. TYPE_LIGHT - Illuminance
        //  3. TYPE_PRESSURE - Ambient air pressure
        //  4. TYPE_RELATIVE_HUMIDITY - Ambient relative humidity
        // not using TYPE_TEMPERATURE since its deprecated
        if(sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE) , SensorManager.SENSOR_DELAY_NORMAL);
            sensorList.add(new Sensor("Temperature", "TEMPERATURE", true));
        } else {
            sensorList.add(new Sensor("Temperature", "NOT AVAILABLE", false));
        }
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_LIGHT) != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_LIGHT) , SensorManager.SENSOR_DELAY_NORMAL);
            sensorList.add(new Sensor("Light", "LIGHT", true));
        } else {
            sensorList.add(new Sensor("Light", "NOT AVAILABLE", false));
        }
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_PRESSURE) != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_PRESSURE) , SensorManager.SENSOR_DELAY_NORMAL);
            sensorList.add(new Sensor("Pressure", "PRESSURE", true));
        } else {
            sensorList.add(new Sensor("Pressure", "NOT AVAILABLE", false));
        }
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_RELATIVE_HUMIDITY) != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_RELATIVE_HUMIDITY) , SensorManager.SENSOR_DELAY_NORMAL);
            sensorList.add(new Sensor("Humidity", "HUMIDITY",true));
        } else {
            sensorList.add(new Sensor("Humidity", "NOT AVAILABLE", false));
        }

        // Listen for available Motion sensors
        //  1. TYPE_ACCELEROMETER - Acceleration force along the x axis (m/s2)
        //  2. TYPE_GRAVITY - Force of gravity along the y axis (m/s2)
        //  3. TYPE_LINEAR_ACCELERATION - Acceleration force along the x axis (m/s2)
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER) != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER) , SensorManager.SENSOR_DELAY_NORMAL);
            sensorList.add(new Sensor("Accelerometer", "ACCELEROMETER", true));
        } else {
            sensorList.add(new Sensor("Accelerometer", "NOT AVAILABLE", false));
        }
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_GRAVITY) != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_GRAVITY) , SensorManager.SENSOR_DELAY_NORMAL);
            sensorList.add(new Sensor("Gravity", "GRAVITY", true));
        } else {
            sensorList.add(new Sensor("Gravity", "NOT AVAILABLE", false));
        }
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_LINEAR_ACCELERATION) != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_LINEAR_ACCELERATION) , SensorManager.SENSOR_DELAY_NORMAL);
            sensorList.add(new Sensor("Linear Acceleration", "ACCELERATION", true));
        } else {
            sensorList.add(new Sensor("Linear Acceleration", "NOT AVAILABLE", false));
        }

        // Listen for available Position sensors
        //  1. TYPE_MAGNETIC_FIELD - Geomagnetic field strength along the x axis (μT)
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_MAGNETIC_FIELD) != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_MAGNETIC_FIELD) , SensorManager.SENSOR_DELAY_NORMAL);
            sensorList.add(new Sensor("Magnetic Field", "MAGNETIC FIELD", true));
        } else {
            sensorList.add(new Sensor("Magnetic Field", "NOT AVAILABLE", false));
        }

        // TODO add other important sensors to list
    }

    /**
     * Initialize location mangers to listen location changes
     */
    private void initLocationListener() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        lastKnowLocation = locationManager.getLastKnownLocation(LOCATION_PROVIDER);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                new GetAddressTask(getActivity()).execute(location);
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
     * Create sensor list
     */
    private void initSensorListView() {
        // construct list adapter
        adapter = new SensorListAdapter(SensorList.this.getActivity(), sensorList);
        sensorListView.setAdapter(adapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        synchronized (this) {
            switch (sensorEvent.sensor.getType()) {
                case android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE:
                    // set temperature
                    // temperature sensor return only one value (values[0])
                    sensorList.get(1).setSensorValue(Float.toString(sensorEvent.values[0]));
                    adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_LIGHT:
                    // set light value
                    // light sensor return only one value (values[0])
                    sensorList.get(2).setSensorValue(Float.toString(sensorEvent.values[0]));
                    adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_PRESSURE:
                    // set pressure
                    // pressure sensor return only one value (values[0])
                    sensorList.get(3).setSensorValue(Float.toString(sensorEvent.values[0]));
                    adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_RELATIVE_HUMIDITY:
                    // set humidity
                    // humidity sensor return only one value (values[0])
                    sensorList.get(4).setSensorValue(Float.toString(sensorEvent.values[0]));
                    adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_ACCELEROMETER:
                    // set accelerometer
                    // use accelerometer only on x axis (values[0])
                    sensorList.get(5).setSensorValue(Float.toString(sensorEvent.values[0]));
                    adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_GRAVITY:
                    // set gravity
                    // use gravity on y axis (values[1])
                    sensorList.get(6).setSensorValue(Float.toString(sensorEvent.values[1]));
                    adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_LINEAR_ACCELERATION:
                    // set acceleration
                    // use values of x axis (values[0])
                    sensorList.get(7).setSensorValue(Float.toString(sensorEvent.values[0]));
                    adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_MAGNETIC_FIELD:
                    // set magnetic field
                    // use only value of x axis (values[0])
                    sensorList.get(8).setSensorValue(Float.toString(sensorEvent.values[0]));
                    adapter.reloadAdapter(sensorList);
                    break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int i) {
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
                application.setLocation(address);
                sensorList.get(0).setSensorValue(address);
                sensorList.get(0).setAvailable(true);
                adapter.reloadAdapter(sensorList);
            }
        }
    }
}
