package com.wasn.Sensors.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.TextView;
import com.wasn.Sensors.R;
import com.wasn.Sensors.application.SensorApplication;
import com.wasn.Sensors.pojo.Sensor;

import java.util.ArrayList;

/**
 * Display sensor list/ Fragment
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class SensorList extends Fragment {
    SensorApplication application;

    // use to populate list
    private ListView sensorListView;
    private ArrayList<Sensor> sensorList;
    private SensorListAdapter adapter;

    // manager for sensors
    private SensorManager sensorManager;

    // two sensor types to display
    //  1. My Sensors
    //  2. Friends Sensors
    private String sensorType;

    // empty view when display on no sensors available
    ViewStub emptyView;

    // set custom font
    Typeface face;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        application = (SensorApplication) getActivity().getApplication();

        initEmptyView();

        // get extra values from intent and determine which sensors to displaying
        // two sensor types to display
        //  1. My Sensors
        //  2. Friends Sensors
        this.sensorType = getArguments().getString(SensorApplication.SENSOR_TYPE, SensorApplication.MY_SENSORS);
        if(sensorType.equals(SensorApplication.MY_SENSORS)) {
            // display my sensors
            //  1. initialize my sensors
            //  2. initialize location listener
            //  3. create list view
            initMySensors();
            initSensorListView();
        } else {
            // display friends sensors
            //  1. initialize friends sensor list
            //  2. create list view
            initFriendsSensors();
            initSensorListView();
        }
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
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Initialize UI components
     */
    private void initUI(View view) {
        sensorListView = (ListView)view.findViewById(R.id.sensor_list_layout_sensor_list);

        // initialize custom font
        face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/vegur_2.otf");

        // add header and footer for list
        View headerView = View.inflate(this.getActivity(), R.layout.list_header, null);
        View footerView = View.inflate(this.getActivity(), R.layout.list_header, null);
        sensorListView.addHeaderView(headerView);
        sensorListView.addFooterView(footerView);
    }

    /**
     * Initialize empty view for list view
     * empty view need to be display when no sensors available
     */
    private void initEmptyView() {
        emptyView = (ViewStub) getActivity().findViewById(R.id.sensor_list_layout_empty_view);
        View inflatedEmptyView = emptyView.inflate();
        TextView emptyText = (TextView) inflatedEmptyView.findViewById(R.id.empty_text);
        emptyText.setText("No friends sensors available");
        emptyText.setTypeface(face);
    }

    /**
     * Initialize sensor managers and sensor list
     * Get available sensors and current sensor data
     */
    private void initMySensors() {
        // get all sensors manager
        sensorList = new ArrayList<Sensor>();
        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);

        // initially add location sensor
        sensorList.add(new Sensor("Location", "LOCATION", true));

        // Listen for environment sensors
        //  1. TYPE_AMBIENT_TEMPERATURE - Ambient air temperature
        //  2. TYPE_LIGHT - Illuminance
        //  3. TYPE_PRESSURE - Ambient air pressure
        //  4. TYPE_RELATIVE_HUMIDITY - Ambient relative humidity
        // not using TYPE_TEMPERATURE since its deprecated
        if(sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            sensorList.add(new Sensor("Temperature", "TEMPERATURE", true));
        } else {
            sensorList.add(new Sensor("Temperature", "NOT AVAILABLE", false));
        }
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_LIGHT) != null) {
            sensorList.add(new Sensor("Light", "LIGHT", true));
        } else {
            sensorList.add(new Sensor("Light", "NOT AVAILABLE", false));
        }
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_PRESSURE) != null) {
            sensorList.add(new Sensor("Pressure", "PRESSURE", true));
        } else {
            sensorList.add(new Sensor("Pressure", "NOT AVAILABLE", false));
        }
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_RELATIVE_HUMIDITY) != null) {
            sensorList.add(new Sensor("Humidity", "HUMIDITY",true));
        } else {
            sensorList.add(new Sensor("Humidity", "NOT AVAILABLE", false));
        }

        // Listen for available Motion sensors
        //  1. TYPE_ACCELEROMETER - Acceleration force along the x axis (m/s2)
        //  2. TYPE_GRAVITY - Force of gravity along the y axis (m/s2)
        //  3. TYPE_LINEAR_ACCELERATION - Acceleration force along the x axis (m/s2)
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER) != null) {
            sensorList.add(new Sensor("Accelerometer", "ACCELEROMETER", true));
        } else {
            sensorList.add(new Sensor("Accelerometer", "NOT AVAILABLE", false));
        }
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_GRAVITY) != null) {
            sensorList.add(new Sensor("Gravity", "GRAVITY", true));
        } else {
            sensorList.add(new Sensor("Gravity", "NOT AVAILABLE", false));
        }
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_LINEAR_ACCELERATION) != null) {
            sensorList.add(new Sensor("Linear Acceleration", "ACCELERATION", true));
        } else {
            sensorList.add(new Sensor("Linear Acceleration", "NOT AVAILABLE", false));
        }

        // Listen for available Position sensors
        //  1. TYPE_MAGNETIC_FIELD - Geomagnetic field strength along the x axis (μT)
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_MAGNETIC_FIELD) != null) {
            sensorList.add(new Sensor("Magnetic Field", "MAGNETIC FIELD", true));
        } else {
            sensorList.add(new Sensor("Magnetic Field", "NOT AVAILABLE", false));
        }

        // TODO add other important sensors to list
    }

    private void initFriendsSensors() {
        sensorList = new ArrayList<Sensor>();
        //sensorList.add(new Sensor("Temperature @Vijitha", "27.5", true));
    }

    /**
     * Create sensor list
     */
    private void initSensorListView() {
        // construct list adapter
        if(sensorList.size()>0) {
            adapter = new SensorListAdapter(SensorList.this.getActivity(), sensorList);
            sensorListView.setAdapter(adapter);
        } else {
            sensorListView.setEmptyView(emptyView);
        }
    }
}
