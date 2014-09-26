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

    public static final String LOG_TAG = "SMSReceiver";

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private Chat chat = XmppTask.chat;

    public static String replyTo;

    @Override
    public void onReceive(Context context, Intent intent){
        if(intent.getAction().equals(ACTION)){
            StringBuilder sb = new StringBuilder();
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                for(SmsMessage currentMessage : messages) {
                    replyTo = currentMessage.getDisplayOriginatingAddress();
                    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(currentMessage.getDisplayOriginatingAddress()));
                    String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};
                    Log.d(LOG_TAG, "[SMSApp] Almost worked!");
                    Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null, null);
                    String sender = null;
                    try {
                        while (cursor.moveToNext()) {
                            sender = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME)) + ": ";
                        }
                    } finally {
                        cursor.close();
                    }
                    sb.append(currentMessage.getDisplayMessageBody());
                    try {
                        chat.sendMessage(sender+currentMessage.getDisplayMessageBody());
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
