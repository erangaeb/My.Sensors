package com.wasn.Sensors.utils;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * Utility class to handle activity related common functions
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class ActivityUtils {

    /**
     * Hide keyboard
     * Need to hide soft keyboard in following scenarios
     *  1. When starting background task
     *  2. When exit from activity
     *  3. On button submit
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getApplicationContext().getSystemService(activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
