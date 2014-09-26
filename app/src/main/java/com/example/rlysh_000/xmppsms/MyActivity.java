package com.example.rlysh_000.xmppsms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jivesoftware.smack.SmackAndroid;

public class MyActivity extends Activity {
    public final static String EXTRA_MESSAGE = "com.example.rlysh_000.xmppsms.MESSAGE";
    int counter;
    Button add, sub;
    TextView display;

    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SmackAndroid.init(getApplicationContext());
        new XmppTask().execute();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        counter = 0;
        add = (Button) findViewById(R.id.buttonAdd);
        sub = (Button) findViewById(R.id.buttonSubtract);
        display = (TextView) findViewById(R.id.what);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                display.setText("Your total is: "+counter);
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter--;
                display.setText("Your total is: "+counter);
            }
        });
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
