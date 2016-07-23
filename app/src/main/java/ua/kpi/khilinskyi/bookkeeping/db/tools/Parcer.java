package ua.kpi.khilinskyi.bookkeeping.db.tools;

import android.database.Cursor;

import ua.kpi.khilinskyi.bookkeeping.db.DBController;

/**
 * Created by Pavel on 23.06.2016.
 */
public class Parcer {
    //return category
    private static String getFormat(String phone){
        Cursor c = DBController.controller.db.rawQuery("select format from Info where phone ='"+phone+"'",null);
        c.moveToFirst();
        return c.getString(0);
    }
    static double count = 0;
    public static String tryParce(String sms_from, String sms){
        String format = getFormat(sms_from);//$ ? @
        return null;
        /// TODO: 23.06.2016
    }

    public static String getName(String sms_body) {
        return "auto add from sms";
    }

    public static double getCount(String sms_body) {
        return count;
    }
}
