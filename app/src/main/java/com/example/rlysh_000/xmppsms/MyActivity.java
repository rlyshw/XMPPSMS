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

// the launched activity
public class MyActivity extends Activity {
    TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        String authFile = "authFile";
        String password = null;
        new XmppTask().execute();
        try {
            FileInputStream fis = openFileInput(authFile);
            password = IOUtils.toString(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        if(password!=null){
            Intent xmppServiceIntent = new Intent(this, XMPPService.class);
            xmppServiceIntent.setData(Uri.parse("?u=" + mPhoneNumber + "&p=" + password));
            this.startService(xmppServiceIntent);
            setContentView(R.layout.main);
            display = (TextView) findViewById(R.id.userName);
            display.setText(mPhoneNumber + "@rlyshw.com");
        }
        else{
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
