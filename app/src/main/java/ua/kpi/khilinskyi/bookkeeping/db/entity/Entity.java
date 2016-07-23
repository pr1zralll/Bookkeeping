package ua.kpi.khilinskyi.bookkeeping.db.entity;

import java.util.Calendar;

/**
 * Created by Pavel on 12.06.2016.
 */
public class Entity {
    public String type;
    public double count;
    public String name;
    public long date;

    public Entity(String type, double count, String name, long date) {
        this.type = type;
        this.count = count;
        this.name = name;
        this.date = date;
    }

    @Override
    public String toString() {
        return type+" "+count+" : "+getDate()+" - "+name;
    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String d = day+"."+month+"."+year;
        return d;
    }
}
