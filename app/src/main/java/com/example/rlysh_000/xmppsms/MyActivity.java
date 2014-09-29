package com.example.rlysh_000.xmppsms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

// the launched(main) activity
public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TelephonyManager is the class that includes telephony information
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number(); // Get the number of line1, the user's cell phone number
        String authFile = "authFile"; // This file holds the user's password
        String password = null; // Initialize the password variable
        new XmppTask().execute(); // Connect to the Xmpp Server
        //new checkIfUserExistsTask().execute(this.getApplicationContext()); //Not working yet
        try { // Read the password from authFile
            // todo: Don't use an authFile
            FileInputStream fis = openFileInput(authFile);
            password = IOUtils.toString(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);

        // if password exists, launch the service
        if(password!=null){
            /*
            This might not be the best way to handle user auth.
            Checking if user exists should take place on the server.
            The actual register user password is NOT needed by this app.
            The password is for the XMPP client on the user's computer.
            Therefore there is no need to write it to a file.

            I was tired when I wrote that auth system. It's unnecessary.
            The best way would be to just ask the server if mPhoneNumber@lryshw.com exists
             */

            //Create the intent to send to the XMPPService
            Intent xmppServiceIntent = new Intent(this, XMPPService.class);
            //Send the dataStream as a uri (?u=phoneNumber&p=password)
            xmppServiceIntent.setData(Uri.parse("?u=" + mPhoneNumber + "&p=" + password));

            this.startService(xmppServiceIntent); // Start the XMPPService
            setContentView(R.layout.main); //Show the main view
            TextView display = (TextView) findViewById(R.id.userName); // find the userName textView in the main layout
            display.setText(mPhoneNumber + "@rlyshw.com"); // set it to mPhoneNumber@rlyshw.com
        }
        else{ //If the user has not initialized a password (i.e. never run the app before), take them to the register screen
            Intent intent = new Intent(this, registerUser.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
