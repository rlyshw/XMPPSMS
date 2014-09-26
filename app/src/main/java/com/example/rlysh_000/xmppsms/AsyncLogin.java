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
    /*Asynchronously login to connected server on connection using specified account info*/
    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            //Using guest account for now
            XmppTask.connection.login("guest", "password");
            /*
            at some point, it will login using the specified contact account.
             */
        } catch (SmackException.ConnectionException e){
            //Debugging stuff
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
