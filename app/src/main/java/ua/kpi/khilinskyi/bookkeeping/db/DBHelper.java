package ua.kpi.khilinskyi.bookkeeping.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pavel on 01.06.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Bookkeeping.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Card
        db.execSQL("create table Card ("
                + "id integer primary key autoincrement,"
                + "name text unique"
                + ");");
        //Category
        db.execSQL("create table Category ("
                + "id integer primary key autoincrement,"
                + "type text not null,"
                + "name text not null unique"
                + ");");
        //Income
        db.execSQL("create table Income ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "count real,"
                + "date integer,"
                + "category_id integer,"
                + "card_id integer,"
                + "FOREIGN KEY(category_id) REFERENCES Category(id),"
                + "FOREIGN KEY(card_id) REFERENCES Card(id)"
                + ");");
        //Purchase
        db.execSQL("create table Purchase ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "price real,"
                + "date integer,"
                + "category_id integer,"
                + "card_id integer,"
                + "FOREIGN KEY(category_id) REFERENCES Category(id),"
                + "FOREIGN KEY(card_id) REFERENCES Card(id)"
                + ");");
        //sms
        db.execSQL("create table Sms ("
                + "id integer primary key autoincrement,"
                + "text text,"
                + "number text,"
                + "date integer,"
                + "category_id integer"
                + ");");
        //info
        db.execSQL("create table Info ("
                + "id integer primary key autoincrement,"
                + "phone text unique,"
                + "format text,"
                + "card text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
