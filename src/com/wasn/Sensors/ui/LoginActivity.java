package com.wasn.Sensors.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.wasn.Sensors.R;
import com.wasn.Sensors.application.SensorApplication;
import com.wasn.Sensors.pojo.User;
import com.wasn.Sensors.service.WebSocketService;

/**
 * Activity class for login
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class LoginActivity extends Activity implements View.OnClickListener, Handler.Callback {

    // form fields
    private EditText username;
    private EditText password;
    private RelativeLayout login;

    SensorApplication application;

    /**
     * {@inheritDoc}
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        application = (SensorApplication) this.getApplication();

        initUI();
        application.setCallback(this);
    }

    /**
     * Initialize layout components
     */
    private void initUI() {
        username = (EditText) findViewById(R.id.login_layout_username);
        password = (EditText) findViewById(R.id.login_layout_password);
        login = (RelativeLayout) findViewById(R.id.edit_invoice_layout_mark_as_paid);

        login.setOnClickListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
        if (v==login) {
            //login();
            switchToHome();
        }
    }

    private void login() {
        if(!username.getText().toString().trim().equals("") && !password.getText().toString().trim().equals("")) {
            // create user and share in application
            application.setUser(new User(username.getText().toString().trim(), username.getText().toString().trim(),
                                                                                password.getText().toString().trim()));

            // open web socket and send username password fields
            // we are authenticate with web sockets
            if(!application.getWebSocketConnection().isConnected()) {
                Intent serviceIntent = new Intent(LoginActivity.this, WebSocketService.class);
                startService(serviceIntent);
            }
        }
    }

    private void switchToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    @Override
    public boolean handleMessage(Message message) {
        String payLoad = (String)message.obj;
        System.out.println("PAYLOAD AT LOGIN " + payLoad);

        // successful login returns "Hello"
        if(payLoad.equalsIgnoreCase("success")) {
            // un-register login activity from callback
            application.setCallback(null);
            Toast.makeText(LoginActivity.this, "Successfully login", Toast.LENGTH_LONG).show();
            switchToHome();

            return true;
        } else {
            Toast.makeText(LoginActivity.this, "Login fail", Toast.LENGTH_LONG).show();

            return false;
        }
    }
}
