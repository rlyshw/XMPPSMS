package com.example.rlysh_000.xmppsms;

import android.os.AsyncTask;
import android.util.Log;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

/**
 * Created by rlysh_000 on 9/25/2014.
 * ASync task to ask the server to register the new phone
 */
public class CreateUser extends AsyncTask<String, Integer, Boolean>{
    public static AccountManager accountManager = AccountManager.getInstance(XmppTask.connection);
    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            accountManager.createAccount(strings[0], strings[1]);
            return true;
        } catch (XMPPException e1) {
            Log.d(e1.getMessage(), e1.toString());
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
