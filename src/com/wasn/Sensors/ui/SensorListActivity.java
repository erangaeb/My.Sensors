package com.wasn.Sensors.ui;

import android.app.Activity;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ListView;
import com.wasn.Sensors.R;
import com.wasn.Sensors.pojo.Sensor;
import com.wasn.Sensors.service.TestUpdateService;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity class to display sensor list
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class SensorListActivity extends Activity implements SensorEventListener {

    // use to populate list
    private ListView sensorListView;
    private ArrayList<Sensor> sensorList;
    private SensorListAdapter adapter;

    // manager for sensors
    private SensorManager sensorManager;
    private List<android.hardware.Sensor> deviceSensors;

    // to handle empty view
    private ViewStub emptyView;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_list_layout);

        init();
    }

    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Create an empty adapter we will use to display the loaded data.
        mAdapter = new CustomArrayAdapter(getActivity());
        setListAdapter(mAdapter);

        // Start out with a progress indicator.
        setListShown(false);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sensor_list_layout, null);

        init(root);

        return root;
    }*/

    /**
     * Initialize activity components
     *  1. Initialize layout components
     *  2. Initialize list
     */
    private void init() {
        initUI();
        initSensors();
        populateList();

        new TestUpdateService(SensorListActivity.this).execute("TEST");
    }

    /**
     * Initialize UI components
     */
    private void initUI() {
        sensorListView = (ListView)findViewById(R.id.sensor_list_layout_sensor_list);

        // add header and footer for list
        View headerView = View.inflate(this, R.layout.list_header, null);
        View footerView = View.inflate(this, R.layout.list_header, null);
        sensorListView.addHeaderView(headerView);
        sensorListView.addFooterView(footerView);
    }

    /**
     * Initialize sensor managers
     * Get available sensors and current sensor data
     */
    private void initSensors() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ALL) , SensorManager.SENSOR_DELAY_NORMAL);
        deviceSensors = sensorManager.getSensorList(android.hardware.Sensor.TYPE_ALL);
    }

    /**
     * Display sensor list
     */
    private void populateList() {
        // fill sample data to list
        sensorList = new ArrayList<Sensor>();
        for (android.hardware.Sensor sensor: deviceSensors) {
            sensorList.add(new Sensor(sensor.getName(), sensor.getVendor()));
        }

        // construct list adapter
        adapter = new SensorListAdapter(SensorListActivity.this, sensorList);
        sensorListView.setAdapter(adapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Update changed sensor values in sensor list
     */
    public void updateSensors() {

    }
}
