package ua.kpi.khilinskyi.bookkeeping.db.tools;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import ua.kpi.khilinskyi.bookkeeping.db.DBController;

/**
 * Created by Pavel on 17.06.2016.
 */
public class SMSService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String sms_body = intent.getExtras().getString("sms_body");
        String sms_from = intent.getExtras().getString("sms_from");
        DBController.controller.addSms(sms_from,sms_body,System.currentTimeMillis());
        showNotification(sms_body);
        return START_STICKY;
    }

    private void showNotification(String sms_body) {
        Log.i("sms",sms_body);
        Toast.makeText(getBaseContext(),sms_body,Toast.LENGTH_LONG).show();
    }
}
