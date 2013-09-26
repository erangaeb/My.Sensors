package com.wasn.Sensors.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.wasn.Sensors.R;
import com.wasn.Sensors.application.SensorApplication;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;

/**
 * Activity class for login
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class LoginActivity extends Activity implements View.OnClickListener {

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
            login();
        }
    }

    private void login() {
        if(!username.getText().toString().trim().equals("") && !password.getText().toString().trim().equals("")) {
            // open web socket and send username password fields
            // we are authenticate with web sockets
            if(!application.getWebSocketConnection().isConnected())
                connectToWebSocket();
        }
    }

    private void connectToWebSocket() {
        try {
            application.getWebSocketConnection().connect(SensorApplication.WEB_SOCKET_URI, new WebSocketConnectionHandler() {
                @Override
                public void onOpen() {
                    application.getWebSocketConnection().sendTextMessage(username.getText().toString());
                    application.getWebSocketConnection().sendTextMessage(password.getText().toString());
                    switchToHome();
                    System.out.println("Connected");
                }

                @Override
                public void onTextMessage(String payload) {
                    Message message = Message.obtain();
                    message.obj = payload;
                    application.getHandler().sendMessage(message);
                    System.out.println("Payload " + payload);
                }

                @Override
                public void onClose(int code, String reason) {

                }
            });
        } catch (WebSocketException e) {
            System.out.println(e);
        }
    }

    private void switchToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}
