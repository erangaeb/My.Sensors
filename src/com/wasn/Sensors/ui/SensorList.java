package com.wasn.Sensors.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.wasn.Sensors.R;
import com.wasn.Sensors.application.SensorApplication;
import com.wasn.Sensors.pojo.Sensor;
import com.wasn.Sensors.service.LocationService;

import java.util.ArrayList;

/**
 * Display sensor list/ Fragment
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class SensorList extends Fragment implements Handler.Callback {

    SensorApplication application;

    // use to populate list
    private ListView sensorListView;
    private ArrayList<Sensor> sensorList;
    private SensorListAdapter adapter;

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

        // two sensor types to display
        //  1. My Sensors
        //  2. Friends Sensors
        if(SensorApplication.SENSOR.equalsIgnoreCase(SensorApplication.MY_SENSORS)) {
            // display my sensors
            //  1. initialize my sensors
            //  2. initialize location listener
            //  3. create list view
            initMySensors();
            initSensorListView();
            getActivity().getActionBar().setTitle("My.SenZors");
        } else {
            // display friends sensors
            //  1. initialize friends sensor list
            //  2. create list view
            initFriendsSensors();
            initSensorListView();
            getActivity().getActionBar().setTitle("Friends.SenZors");
        }

        // set call back
        application.setCallback(this);
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

        sensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sensor sensor = sensorList.get(position-1);

                if(sensor.isMySensor()) {
                    // start location service to get my location
                    Intent serviceIntent = new Intent(getActivity(), LocationService.class);
                    getActivity().startService(serviceIntent);
                } else {
                    // friend sensor
                    // so need to get request to server
                    // send query to get data
                    if(application.getWebSocketConnection().isConnected())
                        application.getWebSocketConnection().sendTextMessage("GET #gps " + sensor.getSensorName());
                }
            }
        });
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

        // initially add location sensor
        sensorList.add(new Sensor("My.Location", "Tap Here", true, true));

        // TODO add other important sensors to list
    }

    private void initFriendsSensors() {
        sensorList = application.getFiendSensorList();
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

    @Override
    public boolean handleMessage(Message message) {
        String payLoad = (String)message.obj;
        System.out.println("PAYLOAD AT SENSOR LIST " + payLoad);

        // data query
        // reload adapter to get new value
        if(!payLoad.equalsIgnoreCase("fail"))
            adapter.reloadAdapter(application.getFiendSensorList());

        return false;
    }

}
