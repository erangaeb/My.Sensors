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
            sensorList.add(new Sensor("Temperature", "TEMPERATURE"));
        }
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_LIGHT) != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_LIGHT) , SensorManager.SENSOR_DELAY_NORMAL);
            sensorList.add(new Sensor("Light", "LIGHT"));
        }
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_PRESSURE) != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_PRESSURE) , SensorManager.SENSOR_DELAY_NORMAL);
            sensorList.add(new Sensor("Pressure", "PRESSURE"));
        }
        if (sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_RELATIVE_HUMIDITY) != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_RELATIVE_HUMIDITY) , SensorManager.SENSOR_DELAY_NORMAL);
            sensorList.add(new Sensor("Humidity", "HUMIDITY"));
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
                    //sensorList.get(0).setSensorvalue(Float.toString(sensorEvent.values[0]));
                    //adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_LIGHT:
                    // set light value
                    // light sensor return only one value (values[0])
                    sensorList.get(0).setSensorvalue(Float.toString(sensorEvent.values[0]));
                    adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_PRESSURE:
                    // set pressure
                    // pressure sensor return only one value (values[0])
                    sensorList.get(1).setSensorvalue(Float.toString(sensorEvent.values[0]));
                    adapter.reloadAdapter(sensorList);
                    break;
                case android.hardware.Sensor.TYPE_GYROSCOPE:
                    // set humidity
                    // gyroscope sensor return only one value (values[0])
                    //sensorList.get(3).setSensorvalue(Float.toString(sensorEvent.values[0]));
                    //adapter.reloadAdapter(sensorList);
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
