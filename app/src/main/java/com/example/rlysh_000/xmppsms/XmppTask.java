package com.example.rlysh_000.xmppsms;

import android.os.AsyncTask;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;

/**
 * Created by rlysh_000 on 9/24/2014.
 *
 * This creates the initial connection to the server, it must be Asynced
 */
public class XmppTask extends AsyncTask {
    /* Async task just to connect to the XMPP server, rlyshw.com */

    //Connect to rlyshw.com, port 5222
    public static ConnectionConfiguration config = new ConnectionConfiguration("rlyshw.com",5222,"rlyshw.com");
    public static XMPPConnection connection= new XMPPTCPConnection(config);
    //Also declare the chat variable here, for some reason.
    public static Chat chat;


    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            connection.connect();
        } catch (SmackException.ConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        }
        return null;
    }
}
