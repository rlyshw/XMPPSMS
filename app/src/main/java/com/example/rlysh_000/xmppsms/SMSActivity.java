package com.example.rlysh_000.xmppsms;

import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;


public class SMSActivity extends BroadcastReceiver {
    /*
    This is never actually called by any of the other code, it is called
    automatically by the android system when an sms is received.
    */
    //Debugging stuff. This is not very consistent with other used logging methods
    public static final String LOG_TAG = "SMSReceiver";

    //Define the action that we are waiting on
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private Chat chat = XmppTask.chat; // import the chat from the XmppTask
    /*
    There is an issue here. chat may not be initialized when it is called here. For instance, if the user
    gets a text before the XMPPService actually has the chance to connect and log in, it would cause
    lots of issues. Not exactly sure how to fix it short of disabling this receiver until XMPPService
    gets a chance to run and initialize the chat variable with an actual chat.
     */

    public static String replyTo; // Initialize the replyTo variable so that XMPPService can use it

    @Override
    public void onReceive(Context context, Intent intent){
        /*
        This uses a bunch of code for reading a text, all of which I pulled from stackOverflow
         */
        if(intent.getAction().equals(ACTION)){ //This determines if the action is actually an SMS
            StringBuilder sb = new StringBuilder(); //What even is a string builder???
            Bundle bundle = intent.getExtras(); // Okay, get the extra info that accompanies this intent call
            String sender = null; //I don't remember if I put this here or not.
            if(bundle != null){
                SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent); //list of message(s??)
                /*
                I thought we were only interested in the last received message, so I don't know
                what's in this list. I'm not entirely clear on how all this works.
                 */
                for(SmsMessage currentMessage : messages) {//iterate through that list of sms.
                    replyTo = currentMessage.getDisplayOriginatingAddress(); // Assign replyTo to the most recent person to text the user

                    //More confusing stuff!
                    //Somehow associates the number with an actual contact name
                    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(currentMessage.getDisplayOriginatingAddress()));
                    String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};
                    Log.d(LOG_TAG, "[SMSApp] Almost worked!");
                    Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null, null);
                    //Involves a query to the ContactsContract.PhoneLookup database
                    try {
                        while (cursor.moveToNext()) { //move to next i dunno
                            //Assign the sender string to the black magic return of the DB query!
                            sender = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME)) + ": ";
                        }
                    } finally {
                        cursor.close(); //good riddance
                    }
                    sb.append(currentMessage.getDisplayMessageBody()); //append the sms body to the string builder, dunno why
                    try {
                        if(sender!=null) {
                            //send to the XMPP chat the sender: message
                            chat.sendMessage(sender + currentMessage.getDisplayMessageBody());
                        }
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.i(LOG_TAG, "[SMSApp] onReceiveIntent: " + sb);
            Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
