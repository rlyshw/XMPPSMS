package com.example.rlysh_000.xmppsms;

import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.util.dns.HostAddress;
import java.io.IOException;
import java.util.List;

import static com.example.rlysh_000.xmppsms.MyActivity.*;

/**
 * Created by rlysh_000 on 9/24/2014.
 */
public class XmppTask extends AsyncTask {
    public static ConnectionConfiguration config = new ConnectionConfiguration("rlyshw.com",5222,"rlyshw.com");
    public static XMPPConnection connection= new XMPPTCPConnection(config);
    public static Chat chat = ChatManager.getInstanceFor(connection).createChat("user1@rlyshw.com", new MessageListener() {
        @Override
        public void processMessage(Chat chat, Message message) {
            if(SMSActivity.replyTo!=null) {
                Log.d("SMSReceiver", "Message from: " + SMSActivity.replyTo);
                sendSMS(SMSActivity.replyTo, message.getBody());
            }
            else{
                Log.d("SMSReceiver", "No replyto!");
            }
        };
    });

    private static void sendSMS(String phoneNumber, String message){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber,null,message,null,null);
    }

    @Override
    protected Object doInBackground(Object... objects) {
        try {
            connection.connect();
            connection.login("guest", "password");
        } catch (SmackException.ConnectionException e) {
            e.printStackTrace();
            List<HostAddress> list = e.getFailedAddresses();
            Log.d("<ERROR>", list.get(0).getException().toString());
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
