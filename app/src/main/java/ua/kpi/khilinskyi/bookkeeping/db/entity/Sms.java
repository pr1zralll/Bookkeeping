package ua.kpi.khilinskyi.bookkeeping.db.entity;

/**
 * Created by Pavel on 22.06.2016.
 */
public class Sms {
    public int id;
    public String text;
    public String number;
    public long date;

    public Sms(int id, String text, String number, long date) {
        this.id = id;
        this.text = text;
        this.number = number;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Sms{" +
                " " + id +
                " " + text +
                " " + number +
                " " + date +
                " }";
    }
}
