package com.example.rlysh_000.xmppsms;

import android.os.AsyncTask;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * Created by rlysh_000 on 9/26/2014.
 * Uses Async to commit the login
 */
public class AsyncLogin extends AsyncTask<String, Integer, Boolean>{
    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            XmppTask.connection.login("guest", "password");
        } catch (SmackException.ConnectionException e){
            //List<HostAddress> list = e.getFailedAddresses();
            //Log.d("<ERROR>", list.get(0).getException().toString());
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
