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
    public static String uPassword;
    String authFile = "authFile";
    TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        display = (TextView) findViewById(R.id.userName);
        display.setText(mPhoneNumber + "@rlyshw.com");
    }

    public void createUser(View view) throws ExecutionException, InterruptedException, IOException {
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        FileOutputStream fos = openFileOutput(authFile, Context.MODE_PRIVATE);
        EditText passwordField = (EditText) findViewById(R.id.userPassword);
        uPassword = passwordField.getText().toString();
        Log.d("PASSWORD: ",uPassword);
        AsyncTask<String, Integer, Boolean> register = new CreateUser().execute(mPhoneNumber, uPassword);
        try {
            if(register.get()){
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
