package com.wasn.Sensors.service;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Find address of given location
 * Geocoder.getFromLocation() method is synchronous, and may take a long time to do its work,
 * So you should call the method from the doInBackground() method of an AsyncTask
 */
public class GetAddressTask extends AsyncTask<Location, Void, String> {
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
