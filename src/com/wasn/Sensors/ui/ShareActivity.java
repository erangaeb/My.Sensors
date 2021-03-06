package com.wasn.Sensors.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.wasn.Sensors.R;
import com.wasn.Sensors.application.SensorApplication;
import com.wasn.Sensors.utils.ActivityUtils;

/**
 * Activity class for sharing
 * Implement sharing related functions
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class ShareActivity extends Activity implements Handler.Callback {

    SensorApplication application;

    // layout components
    EditText emailEditText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_layout);

        application = (SensorApplication) getApplication();
        application.setCallback(this);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        initUI();
    }

    private void initUI() {
        emailEditText = (EditText) findViewById(R.id.share_layout_email_text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // navigate to home with effective navigation
                NavUtils.navigateUpFromSameTask(this);
                ShareActivity.this.overridePendingTransition(R.anim.stay_in, R.anim.bottom_out);

                ActivityUtils.hideSoftKeyboard(this);
                return true;
            case R.id.action_share:
                // share sensor data
                share();


                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Share action
     */
    private void share() {
        String email = emailEditText.getText().toString().trim();
        String query = "SHARE" + " " + "#gps" + " " + "@"+emailEditText.getText().toString().trim();

        // validate sher attribute first
        if(!email.equalsIgnoreCase("")) {
            // construct query and send to server vis socket
            if(application.getWebSocketConnection().isConnected())
                application.getWebSocketConnection().sendTextMessage(query);

            ActivityUtils.hideSoftKeyboard(this);
        } else {
            Toast.makeText(ShareActivity.this, "Make sure non empty email address", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ShareActivity.this.overridePendingTransition(R.anim.stay_in, R.anim.bottom_out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handleMessage(Message message) {
        // we handle string messages only from here
        if(message.obj instanceof String) {
            String payLoad = (String)message.obj;

            // successful login returns "Hello"
            if(payLoad.equalsIgnoreCase("success")) {
                Toast.makeText(ShareActivity.this, "Sensor has been shared successfully", Toast.LENGTH_LONG).show();
                ShareActivity.this.finish();
                ShareActivity.this.overridePendingTransition(R.anim.stay_in, R.anim.bottom_out);

                return true;
            } else {
                Toast.makeText(ShareActivity.this, "Sharing fail", Toast.LENGTH_LONG).show();
            }
        }

        return false;
    }
}
