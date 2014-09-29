package com.example.rlysh_000.xmppsms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;


public class registerUser extends Activity {
    /*
    This Activity handles registering the user with the server. This user is what the desktop XMPP
    client will use to recieve texts. It is not really needed by the application, outside of sending
     a register request to the server
     */
    public static String uPassword; //determine if this needs to or should exist
    String authFile = "authFile"; //Do I really need an authfile????

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // get user's phone number
        // I couldn't figure out how to make the phone number public static in one file
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register); // Change to the register layout
        TextView display = (TextView) findViewById(R.id.userName); //Get the userName TextView
        display.setText(mPhoneNumber + "@rlyshw.com"); // Change it to mPhoneNumber@rlyshw.com
    }

    public void createUser(View view) throws ExecutionException, InterruptedException, IOException {
        // I use this deceleration like 8 separate times.
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number(); //Should really figure out how to make ONE public static mPhoneNumber
        // Need to not use file operations in this app
        FileOutputStream fos = openFileOutput(authFile, Context.MODE_PRIVATE);
        EditText passwordField = (EditText) findViewById(R.id.userPassword);
        uPassword = passwordField.getText().toString();
        Log.d("PASSWORD: ",uPassword); //Debug purposes

        //This is important. It calls the AsyncTask to register the new
        // user with password uPassword, mPhoneNumber@rlyshw.com
        // This will be the user's credentials for connecting to the xmpp server
        AsyncTask<String, Integer, Boolean> register = new CreateUser().execute(mPhoneNumber, uPassword);

        try {
            // register.get() returns True when the CreateUser task completes successfully
            if(register.get()){
                // Write that password to a file.
                // MyActivity uses this file to check if the account exists on the server.
                // This is not the right way to check if the account exists on the server.
                fos.write(uPassword.getBytes());
                fos.close();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void startXMPP(View view) throws IOException, URISyntaxException {
        // This is called when the button is pressed
        // Essentially restarts the program
        Intent intent = new Intent(this, MyActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
