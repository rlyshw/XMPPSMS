package com.example.rlysh_000.xmppsms;

import android.app.IntentService;
import android.app.Notification;
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
    /*
    This is the background service to receive messages from the desktop client
     */

    public XMPPService() {
        super("XMPPService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //dunno what this method does exactly but it should keep this service from dying
        startForeground(1,new Notification());

        String dataString = intent.getDataString(); //get the encoded uri from the service call
        //new AsyncLogin().execute(); //login to the XMPP server, uses account guest:password@rlyshw.com
        //todo: replace login with the user account of the contact that sent the text
        //ie: (sender@rlyshw.com)

        //Create userAccount from the dataString uri, mPhoneNumber@rlyshw.com
        String userAccount = Uri.parse(dataString).getQueryParameter("u")+"@rlyshw.com";
        //Create a chat with userAccount with MessageListener on connection
        XmppTask.chat = ChatManager.getInstanceFor(XmppTask.connection).createChat(userAccount, new MessageListener() {
            /*
            Listens for messages from chat with userAccount.
             */
            private void sendSMS(String phoneNumber, String message){
                /* sends an sms to phoneNumber*/
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(phoneNumber,null,message,null,null);
            }

            public void processMessage(Chat chat, Message message) {
                /* Processes incoming XMPP message in chat*/
                // If there is someone to reply to,
                if (SMSActivity.replyTo != null) {
                    /*
                    Once the chat is made specific to each contact, the replyTo variable won't matter,
                    the userInfo from the chat sender(the contact) will be used instead.
                     */
                    Log.d("SMSReceiver", "Message from: " + SMSActivity.replyTo); //Debugging purposes
                    sendSMS(SMSActivity.replyTo, message.getBody());//send sms to reply, with the XMPP message
                } else {
                    Log.d("SMSReceiver", "No replyto!");
                }
            }
        });
    }
}
