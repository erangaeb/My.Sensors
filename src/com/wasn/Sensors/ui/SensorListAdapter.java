package com.wasn.Sensors.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.wasn.Sensors.R;
import com.wasn.Sensors.application.SensorApplication;
import com.wasn.Sensors.pojo.Sensor;

import java.util.ArrayList;

/**
 * Adapter class to display sensor list
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class SensorListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Sensor> sensorList;
    private SensorApplication application;

    /**
     * Initialize context variables
     *
     * @param context activity context
     * @param sensorList sharing user list
     */
    public SensorListAdapter(Context context, ArrayList<Sensor> sensorList) {
        //application = (Pay2nApplication) context.getApplicationContext();

        this.context = context;
        this.sensorList = sensorList;
    }

    /**
     * Reload content in adapter
     */
    public void reloadAdapter(ArrayList<Sensor> sensorList) {
        this.sensorList = sensorList;
        notifyDataSetChanged();
    }
    /**
     * Get size of sensor list
     * @return userList size
     */
    @Override
    public int getCount() {
        return sensorList.size();
    }

    /**
     * Get specific item from sensor list
     * @param i item index
     * @return list item
     */
    @Override
    public Object getItem(int i) {
        return sensorList.get(i);
    }

    /**
     * Get sensor list item id
     * @param i item index
     * @return current item id
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Create list row view
     * @param i index
     * @param view current list item view
     * @param viewGroup parent
     * @return view
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        final ViewHolder holder;

        final Sensor sensor = (Sensor) getItem(i);

        if (view == null) {
            //inflate sensor list row layout
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.sensor_list_row_layout, viewGroup, false);

            //create view holder to store reference to child views
            holder = new ViewHolder();
            holder.sensorName = (TextView) view.findViewById(R.id.sensor_list_row_layout_sensor_name);
            holder.sensorValue = (TextView) view.findViewById(R.id.sensor_list_row_layout_sensor_value);

            view.setTag(holder);
        } else {
            //get view holder back_icon
            holder = (ViewHolder) view.getTag();
        }

        // bind text with view holder content view for efficient use
        holder.sensorName.setText(sensor.getSensorName());
        holder.sensorValue.setText(sensor.getSensorvalue());

        return view;
    }

    /**
     * Keep reference to children view to avoid unnecessary calls
     */
    static class ViewHolder {
        TextView sensorName;
        TextView sensorValue;
    }

}
