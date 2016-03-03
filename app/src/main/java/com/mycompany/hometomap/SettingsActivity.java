package com.mycompany.hometomap;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by max on 2/25/16.
 */
public class SettingsActivity extends AppCompatActivity {

    private static String userName;
    private static String emergencyNumber;
    private EditText userNameText;
    private EditText emergencyNumberText;

    public static String getUserName()
    {
        return (userName == null ? "" : userName);
    }

    public static String getEmergencyNumber()
    {
        return (emergencyNumber == null ? "" : emergencyNumber);
    }

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_settings);

        userNameText = (EditText) findViewById(R.id.userName);
        emergencyNumberText = (EditText) findViewById(R.id.userEmergencyNumber);

        if (userName != null)
            userNameText.setText(userName);
        if (emergencyNumber != null)
            emergencyNumberText.setText(emergencyNumber);
    }

    public void onSettingsSaveClick(View view)
    {
        userName = userNameText.getText().toString();
        emergencyNumber = emergencyNumberText.getText().toString();

        Snackbar.make(view, "Information saved", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Bluetooth.ReconnectBluetooth(this);
    }
}
