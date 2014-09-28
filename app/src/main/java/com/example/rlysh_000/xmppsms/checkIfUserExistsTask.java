package com.example.rlysh_000.xmppsms;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.search.ReportedData.Row;
import org.jivesoftware.smackx.xdata.FormField;

import java.util.Iterator;
import java.util.List;

/**
 * Created by rlysh_000 on 9/27/2014.
 * Makes a search to see if the specified user exists on the server
 *
 * Doesn't work yet. The server doesn't support searching for some reason.
 */
public class checkIfUserExistsTask extends AsyncTask<Context,Integer,Boolean>{
    UserSearchManager userSearchManager = new UserSearchManager(XmppTask.connection);

    @Override
    protected Boolean doInBackground(Context... contexts) {
        TelephonyManager tMgr = (TelephonyManager) contexts[0].getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        try {
            Form searchForm = userSearchManager.getSearchForm("vjud.rlyshw.com");
            Form answerForm = searchForm.createAnswerForm();
            FormField formField = answerForm.getFields().get(0);
            Log.i("FORM", formField.getVariable());
            answerForm.setAnswer("user", mPhoneNumber + "@rlyshw.com");
            ReportedData data = userSearchManager.getSearchResults(answerForm,"vjud.rlyshw.com");
            if(data.getRows() != null)
            {
                for (Row row : data.getRows()) {
                    Iterator iterator = row.getValues("jid").iterator();
                    if (iterator.hasNext()) {
                        String value = iterator.next().toString();
                        Log.i("Iterator values......", " " + value);
                        Log.i("USER EXISTS","USER EXISTS");
                    }
                    //Log.i("Iteartor values......"," "+value);
                }
                Log.i("USER DOESNT EXISTS","NO USER");
            }
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
