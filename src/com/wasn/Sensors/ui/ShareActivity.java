package com.wasn.Sensors.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import com.wasn.Sensors.R;
import com.wasn.Sensors.application.SensorApplication;

/**
 * Activity class for sharing
 * Implement sharing related functions
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class ShareActivity extends Activity {

    SensorApplication application;

    // layout components
    EditText emailEditText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_layout);

        application = (SensorApplication) getApplication();

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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // navigate to home with effective navigation
                NavUtils.navigateUpFromSameTask(this);
                ShareActivity.this.overridePendingTransition(R.anim.stay_in, R.anim.bottom_out);

                return true;
            case R.id.action_share:
                // share sensor data
                // and go back
                String query = "SHARE" + " " + "#gps" + " " + "@"+emailEditText.getText().toString().trim();
                if(application.getWebSocketConnection().isConnected())
                    application.getWebSocketConnection().sendTextMessage(query);

                super.onBackPressed();
                ShareActivity.this.overridePendingTransition(R.anim.stay_in, R.anim.bottom_out);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ShareActivity.this.overridePendingTransition(R.anim.stay_in, R.anim.bottom_out);
    }
}
