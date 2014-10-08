package com.example.rlysh_000.xmppsms;

import android.os.AsyncTask;
import android.util.Log;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

/**
 * Created by rlysh_000 on 9/25/2014.
 * ASync task to ask the server to register the new phone
 */
public class CreateUser extends AsyncTask<String, Integer, Boolean>{
    /*
    Anything that connects to the internet should be done in an AsyncTask.
    This uses the connection established by XmppTask to register the user from registerUser
     */

    //What a useful little class. This call (annoyingly) differs from what is written in the smack
    // documentation. They should really get on that. accountManager probably has a method to check
    // if a user exists on the server. I should use that for useAuth
    public static AccountManager accountManager = AccountManager.getInstance(XmppTask.connection);

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            // Try to create an account on the server
            // Strings[0] is mPhoneNumber strings[1] is password (not necessary?)
            accountManager.createAccount(strings[0], strings[1]);
            VCard vcard = new VCard();
            vcard.setNickName(strings[0]);
            vcard.save(XmppTask.connection);
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
