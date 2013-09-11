package com.wasn.Sensors.ui;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.wasn.Sensors.R;
import com.wasn.Sensors.pojo.Sensor;

import java.util.ArrayList;
import java.util.List;

/**
 * Display sensor list/ Fragment
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class SensorList extends Fragment implements SensorEventListener {

    // use to populate list
    private ListView sensorListView;
    private ArrayList<Sensor> sensorList;
    private SensorListAdapter adapter;

    // manager for sensors
    private SensorManager sensorManager;
    private List<android.hardware.Sensor> deviceSensorList;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // after created activity
        //  1. initialize sensors
        //  2. create sensor list
        initSensors();
        initSensorList();
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
     * Initialize sensor managers and sensor list
     * Get available sensors and current sensor data
     */
    private void initSensors() {
        sensorList = new ArrayList<Sensor>();

        // get all sensors manager
        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        deviceSensorList = sensorManager.getSensorList(android.hardware.Sensor.TYPE_ALL);

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
        // TODO create mechanism to display only available sensors and values
    }

    /**
     * Create sensor list
     */
    private void initSensorList() {
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
                    sensorList.get(0).setSensorValue(Float.toString(sensorEvent.values[0]));
                    adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_LIGHT:
                    // set light value
                    // light sensor return only one value (values[0])
                    sensorList.get(1).setSensorValue(Float.toString(sensorEvent.values[0]));
                    adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_PRESSURE:
                    // set pressure
                    // pressure sensor return only one value (values[0])
                    sensorList.get(2).setSensorValue(Float.toString(sensorEvent.values[0]));
                    adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_RELATIVE_HUMIDITY:
                    // set humidity
                    // humidity sensor return only one value (values[0])
                    sensorList.get(3).setSensorValue(Float.toString(sensorEvent.values[0]));
                    adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_ACCELEROMETER:
                    // set accelerometer
                    // use accelerometer only on x axis (values[0])
                    sensorList.get(4).setSensorValue(Float.toString(sensorEvent.values[0]));
                    adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_GRAVITY:
                    // set gravity
                    // use gravity on y axis (values[1])
                    sensorList.get(5).setSensorValue(Float.toString(sensorEvent.values[1]));
                    adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_LINEAR_ACCELERATION:
                    // set acceleration
                    // use values of x axis (values[0])
                    sensorList.get(6).setSensorValue(Float.toString(sensorEvent.values[0]));
                    adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_MAGNETIC_FIELD:
                    // set magnetic field
                    // use only value of x axis (values[0])
                    sensorList.get(7).setSensorValue(Float.toString(sensorEvent.values[0]));
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
}
