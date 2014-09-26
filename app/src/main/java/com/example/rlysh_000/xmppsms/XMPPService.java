package com.example.rlysh_000.xmppsms;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by rlysh_000 on 9/25/2014.
 * This is the background service that handles all the background operations
 */
public class XMPPService extends IntentService {

    public XMPPService() {
        super("XMPPService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String dataString = intent.getDataString();
        new AsyncLogin().execute();
        XmppTask.chat = ChatManager.getInstanceFor(XmppTask.connection).createChat(Uri.parse(dataString).getQueryParameter("u")+"@rlyshw.com", new MessageListener() {
            private void sendSMS(String phoneNumber, String message){
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(phoneNumber,null,message,null,null);
            }
            public void processMessage(Chat chat, Message message) {
                if (SMSActivity.replyTo != null) {
                    Log.d("SMSReceiver", "Message from: " + SMSActivity.replyTo);
                    sendSMS(SMSActivity.replyTo, message.getBody());
                } else {
                    Log.d("SMSReceiver", "No replyto!");
                }
            }
        });
    }
}
