package ua.kpi.khilinskyi.bookkeeping.db.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import ua.kpi.khilinskyi.bookkeeping.db.DBController;

/**
 * Created by Pavel on 17.06.2016.
 */
public class SMSMonitor extends BroadcastReceiver {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null &&
                ACTION.compareToIgnoreCase(intent.getAction()) == 0) {
            SmsMessage smsMessage;
            if (Build.VERSION.SDK_INT >= 19) { //KITKAT
                SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                smsMessage = msgs[0];
            } else {
                Object pdus[] = (Object[]) intent.getExtras().get("pdus");
                smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
            }
            Log.e("sms","sms ok");
            String sms_from = smsMessage.getDisplayOriginatingAddress();
            if (TestNumber(sms_from)) {

                String body = smsMessage.getMessageBody();
                Intent mIntent = new Intent(context, SMSService.class);
                mIntent.putExtra("sms_from", sms_from);
                mIntent.putExtra("sms_body", body);
                context.startService(mIntent);

                abortBroadcast();
            }
        }
    }
    static String[] ps = DBController.controller.getPhones();

    private boolean TestNumber(String sms_from) {
        for (String s :ps) {
            if(sms_from.equals(s)){
                return true;
            }
        }
        return false;
    }
}