package com.wasn.Sensors.service;

import android.os.AsyncTask;
import com.wasn.Sensors.ui.SensorListActivity;

/**
 * Created with IntelliJ IDEA.
 * User: eranga
 * Date: 9/9/13
 * Time: 5:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestUpdateService extends AsyncTask<String, String, String> {
    SensorListActivity activity;

    public TestUpdateService(SensorListActivity activity) {
        this.activity = activity;
    }


    @Override
    protected String doInBackground(String... strings) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "test";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);    //To change body of overridden methods use File | Settings | File Templates.
        System.out.println("updating..");
        activity.updateSensors();
    }
}
