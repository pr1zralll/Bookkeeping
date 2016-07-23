package ua.kpi.khilinskyi.bookkeeping.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.util.SortedList;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;


import java.sql.Array;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Locale;

import ua.kpi.khilinskyi.bookkeeping.db.entity.Card;
import ua.kpi.khilinskyi.bookkeeping.db.entity.Category;
import ua.kpi.khilinskyi.bookkeeping.db.entity.Entity;
import ua.kpi.khilinskyi.bookkeeping.db.entity.Sms;
import ua.kpi.khilinskyi.bookkeeping.db.tools.Parcer;

/**
 * Created by Pavel on 01.06.2016.
 */
public class DBController {
    public static DBController controller;
    private final Context context;
    private DBHelper dbHelper;
    public SQLiteDatabase db=null;
    public DBController(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        controller=this;
    }
    public void createDB(){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public void addIncome(int card, String selectedCat, String name, double count, long date){
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("count",count);
        values.put("date",date);
        values.put("card_id",card);
        values.put("category_id",getCategoryID(selectedCat));
        if(db.insert("Income",null, values)==-1)
            Toast.makeText(context,"can`t insert to db",Toast.LENGTH_SHORT).show();
        else Toast.makeText(context,"insert ok",Toast.LENGTH_SHORT).show();
    }
    public void addPurchase(int category, int selectedCard, String name, double price, long date){
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("price",price);
        values.put("date",date);
        values.put("category_id",category);
        values.put("card_id",selectedCard);
        if(db.insert("Purchase",null, values)==-1)
            Toast.makeText(context,"can`t insert to db",Toast.LENGTH_SHORT).show();
        else Toast.makeText(context,"insert ok",Toast.LENGTH_SHORT).show();
    }
    public void info(String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
        Log.i("info: ",str);
    }
    public void info(int str){
        Toast.makeText(context,String.valueOf(str),Toast.LENGTH_SHORT).show();
        Log.i("info: ",String.valueOf(str));
    }
    public String[] getCategoriesName(String type){
        Cursor c =null;
        if(type.equals("всі")){
            c = db.query("Category",new String[]{"name"},null,null,null,null,"name");
        }else
            c = db.query("Category",new String[]{"name"},"type = '"+type+"'",null,null,null,"name");
        if(c.getCount()<1) return new String[]{};
        String[] ret = new String[c.getCount()];
        c.moveToFirst();
        for (int i=0; i < ret.length;i++){
            ret[i]=c.getString(0);
            c.moveToNext();
        }
        return ret;
    }
    public String[] getCardsName(){
        Cursor c = db.query("Card",new String[]{"name"},null,null,null,null,"name");
        if(c.getCount()<1) return new String[]{};
        String[] ret = new String[c.getCount()];
        c.moveToFirst();
        for (int i=0; i < ret.length;i++){
            ret[i]=c.getString(0);
            c.moveToNext();
        }
        return ret;
    }

    public int getCategoryID(String selectedItem) {
        Cursor c = db.rawQuery("select id from Category where name = '"+selectedItem+"'",null);
        c.moveToFirst();
        if(c.getCount()<1) {
            Log.e("DBController","id not found");
            return 0;
        }
        return c.getInt(0);
    }

    public void AddCard(String s) {
        ContentValues cv = new ContentValues();
        cv.put("name",s);
        db.insert("Card",null,cv);
    }

    public void AddCategory(String s, String type) {
        ContentValues cv2 = new ContentValues();
        cv2.put("name",s);
        cv2.put("type",type);
            if(db.insert("Category",null,cv2)==-1) {
                info("exists");
                return;
            }

        info("insert ok");
    }

    public Card[] getBalance(){
        Cursor c = db.rawQuery("select a.id,a.name,a.count,b.price\n" +
                "from (select Card.id as id, Card.name as name, sum(Income.count) as count\n" +
                "from Card\n" +
                "left join Income\n" +
                "on Card.id = Income.card_id\n" +
                "group by Card.id)as a, (select Card.id as id, Card.name as name, sum(Purchase.price) as price\n" +
                "from Card\n" +
                "left join Purchase\n" +
                "on Card.id = Purchase.card_id\n" +
                "group by Card.id) as b\n" +
                "where a.id = b.id",null);
        if(c.getCount()<1) return null;
            c.moveToFirst();
        Card[] cards = new Card[c.getCount()];
        for (int i = 0; i < c.getCount(); i++) {
            cards[i] = new Card(c.getInt(0),c.getString(1),c.getDouble(2)-c.getDouble(3));
            c.moveToNext();
        }
        return cards;
    }


    public int getCardID(String s) {
        Cursor c = db.rawQuery("select id from Card where name = '"+s+"'",null);
        c.moveToFirst();
        if(c.getCount()<1) {
            Log.e("DBController","id not found");
            return 0;
        }
        return c.getInt(0);
    }

    public Entity[] getHistoryEntity(String time, String category) {
        long t = 0;
        Calendar i = getTime(time);

        t = i.getTimeInMillis();

        Cursor income;

        Cursor purchase = null;
        if(category=="всі"){
            purchase = db.rawQuery("select Purchase.price, Purchase.date, Purchase.name from Purchase where Purchase.date > "+ t+" order by 2,3",null);
            income = db.rawQuery("select Income.count, Income.date, Income.name from Income where Income.date > "+ t+" order by 2,3",null);
        }else {
            purchase = db.rawQuery("select Purchase.price, Purchase.date, Purchase.name from Purchase where Purchase.date > "+ t+" and Purchase.category_id = "+DBController.controller.getCategoryID(category)+" order by 2,3",null);
            income = db.rawQuery("select Income.count, Income.date, Income.name from Income where Income.date > "+ t+" and Income.category_id = "+DBController.controller.getCategoryID(category)+" order by 2,3",null);
        }

        ArrayList<Entity> entities = new ArrayList<>();
        income.moveToFirst();
        purchase.moveToFirst();

        if(income.getCount()>0)
            do {
                double count = income.getDouble(0);
                long date = income.getLong(1);
                if(date==0)continue;
                String name = income.getString(2);
                entities.add(new Entity("+", count, name, date));
            } while (income.moveToNext());
        if(purchase.getCount()>0)
            do {
                double count = purchase.getDouble(0);
                long date = purchase.getLong(1);
                if(date==0)continue;
                String name = purchase.getString(2);
                entities.add(new Entity("-", count, name, date));
            } while (purchase.moveToNext());

        Entity[] entities2 = new Entity[entities.size()];
        entities2 = entities.toArray(entities2);
        Arrays.sort(entities2, new Comparator<Entity>() {
            public int compare(Entity a, Entity b){
                return (int) (b.date-a.date);
            };
        });
        return entities2;
    }

    public String[] getHistory(String time, String category) {
        Entity[] entities2 = getHistoryEntity(time,category);
        String[] strings = new String[entities2.length];
        for (int j = 0; j < entities2.length; j++) {
            strings[j]=entities2[j].toString();
        }
        return strings;
    }

    private Calendar getTime(String time) {
        Calendar i = Calendar.getInstance();
        switch (time){
            case "За місяць":
                i.roll(Calendar.MONTH,-1);
                break;
            case "За день":
                i.roll(Calendar.DAY_OF_MONTH,-1);
                break;
            case "За тиждень":
                i.roll(Calendar.WEEK_OF_MONTH,-1);
                break;
            case "За рік":
                i.roll(Calendar.YEAR,-1);
                break;
            case "З моменту останнього доходу":
                i.setTimeInMillis(getLastIncome());
                break;
            case "Поточний місяць":
                i.set(i.get(Calendar.YEAR),i.get(Calendar.MONTH),1);
            default:
                break;
        }
        return i;
    }

    private long getLastIncome() {

        Cursor c = db.rawQuery("select date from Income order by 1 desc",null);
        if(c.getCount()<1)
            return 0;
        c.moveToFirst();
        long date = c.getLong(0);
        return date;
    }

    public String[] getDohodu(String time, String category) {
        long t = 0;
        Calendar i = getTime(time);
        t = i.getTimeInMillis();

        Cursor income = db.rawQuery("select Income.count, Income.date, Income.name from Income where Income.date > "+ t+" order by 2,3",null);

        ArrayList<Entity> entities = new ArrayList<>();
        income.moveToFirst();
        try {
            do {
                double count = income.getDouble(0);
                long date = income.getLong(1);
                String name = income.getString(2);
                entities.add(new Entity("+", count, name, date));
            } while (income.moveToNext());
        }catch (Exception e){
            return null;
        }

        Entity[] entities2 = new Entity[entities.size()];
        entities2 = entities.toArray(entities2);
        Arrays.sort(entities2, new Comparator<Entity>() {
            public int compare(Entity a, Entity b){
                return (int) (b.date-a.date);
            };
        });
        String[] strings = new String[entities2.length];
        for (int j = 0; j < entities2.length; j++) {
            strings[j]=entities2[j].toString();
        }
        return strings;
    }

    public String[] getVitratu(String time, String category) {
        long t = 0;
        Calendar i = getTime(time);
        t = i.getTimeInMillis();
        Cursor purchase;
        if(category=="всі")
            purchase = db.rawQuery("select Purchase.price, Purchase.date, Purchase.name from Purchase where Purchase.date >= "+ t+" order by 2,3",null);
        else
            purchase = db.rawQuery("select Purchase.price, Purchase.date, Purchase.name from Purchase where Purchase.date >= "+ t+" and Purchase.category_id = "+DBController.controller.getCategoryID(category)+" order by 2,3",null);
        ArrayList<Entity> entities = new ArrayList<>();
        purchase.moveToFirst();
        try {
            do {
                double count = purchase.getDouble(0);
                long date = purchase.getLong(1);
                String name = purchase.getString(2);
                entities.add(new Entity("-", count, name, date));
            } while (purchase.moveToNext());
        }catch (Exception e){
            return null;
        }
        Entity[] entities2 = new Entity[entities.size()];
        entities2 = entities.toArray(entities2);
        Arrays.sort(entities2, new Comparator<Entity>() {
            public int compare(Entity a, Entity b){
                return (int) (b.date-a.date);
            };
        });
        String[] strings = new String[entities2.length];
        for (int j = 0; j < entities2.length; j++) {
            strings[j]=entities2[j].toString();
        }
        return strings;
    }

    public Category[] getCategoryCosts(String time, String type) {
        Cursor c = null;
        Calendar calendar = getTime(time);
        long t = calendar.getTimeInMillis();
        if(type == "витрати"){
            c = db.rawQuery("select Category.name, sum(Purchase.price)\n" +
                    "from Category, Purchase\n" +
                    "where Category.id = Purchase.category_id \n" +
                    "and Purchase.date > '"+t+"'\n" +
                    "group by Category.name " +
                    "order by 2",null);
            if(c.getCount()<1)
                return null;
            Category[] categories = new Category[c.getCount()];
            int i=0;
            c.moveToFirst();
            do {
                categories[i++]= new Category(c.getString(0), (float) c.getDouble(1));
            }while (c.moveToNext());
            return categories;
        }
        if(type == "доходи"){
            c = db.rawQuery("select Category.name, sum(Income.count)\n" +
                    "from Category, Income\n" +
                    "where Category.id = Income.category_id \n" +
                    "and Income.date > '"+t+"'\n" +
                    "group by Category.name " +
                    "order by 2",null);
            if(c.getCount()<1)
                return null;
            Category[] categories = new Category[c.getCount()];
            int i=0;
            c.moveToFirst();
            do {
                categories[i++]= new Category(c.getString(0), (float) c.getDouble(1));
            }while (c.moveToNext());
            return categories;
        }
        return new Category[0];
    }

    public void removeItem(String name, String typeOper, String selectedCategory) {
        String type = name.substring(0,1);
        String count = name.split(" ")[1];
        String date = name.split(" ")[3];
        String nameE;
        try{
        nameE = name.split(" ")[5];}
        catch (Exception e){nameE = "";}
        String[] dat = new String[3];
        int j = 0;
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < date.length(); i++) {
            if(date.charAt(i)!='.'){
                temp.append(date.charAt(i));
            }else {
                dat[j++]=temp.toString();
                temp = new StringBuilder();
            }
        }
        dat[j++]=temp.toString();

        int yy= Integer.parseInt(dat[2]);
        int mm= Integer.parseInt(dat[1]);
        int dd= Integer.parseInt(dat[0]);
        Calendar i = Calendar.getInstance();
        i.set(yy,mm,dd,0,0,0);
        long t = i.getTimeInMillis();

        if(type.equals("+")){
            db.delete("Income",  "count = '"+count+"' and date >= '"+t+"' and name = '"+nameE+"'",null);
        }else {
            db.delete("Purchase","price = '"+count+"' and date >= '"+t+"' and name = '"+nameE+"'",null);
        }

    }

    public void removeCat(String name) {
        db.delete("Category","name = '"+name+"'",null);
    }

    public void removeCard(String name) {
        db.delete("Card","name = '"+name+"'",null);
    }

    public String[] getTables() {
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'",null);
        String[] strings = new String[c.getCount()];
        c.moveToFirst();
        int i=0;
        do{
            strings[i++]=c.getString(0);
        }while (c.moveToNext());
        return strings;
    }

    public void addSmsWorker(String p, String f, String selected_card) {
        ContentValues cv = new ContentValues();
        cv.put("phone",p);
        cv.put("format",f);
        cv.put("card",selected_card);
        db.insert("Info",null,cv);
    }

    public void removeInfo(String phone) {
        db.delete("Info","phone = '"+phone+"'",null);
    }

    public String[] getPhones() {
        Cursor c = db.rawQuery("SELECT phone FROM Info",null);
        if(c.getCount()<1)
            return new String[]{"none"};
        String[] strings = new String[c.getCount()];
        c.moveToFirst();
        int i=0;
        do{
            strings[i++]=c.getString(0);
        }while (c.moveToNext());
        return strings;
    }

    public String[] getSMS() {
        Cursor c = db.rawQuery("select * from Sms order by date",null);
        if(c.getCount()<1)
            return null;
        Sms[] smses = new Sms[c.getCount()];
        c.moveToFirst();
        int i = 0;
        do {
            int id = c.getInt(0);
            String text = c.getString(1);
            String number = c.getString(2);
            long date = c.getLong(3);
            smses[i++]= new Sms(id,text,number,date);
        }while (c.moveToNext());
        String[] strs = new String[c.getCount()];
        for (int j = 0; j < c.getCount(); j++) {
            strs[j]=smses[j].toString();
        }
        return strs;
    }

    public void Move(String from, String to, Double how) {
        from = from.split(" ")[1];
        to = to.split(" ")[1];
        ContentValues cv = new ContentValues();
        cv.put("price",how);
        cv.put("card_id",getCardID(from));
        db.insert("Purchase",null,cv);
        cv.clear();
        cv.put("count",how);
        cv.put("card_id",getCardID(to));
        db.insert("Income",null,cv);
    }

    public void addSms(String sms_from, String sms_body, long date) {
        if(Parcer.tryParce(sms_from,sms_body)==null){
            ContentValues cv = new ContentValues();
            cv.put("text",sms_body);
            cv.put("number",sms_from);
            cv.put("date",date);
            db.insert("Sms",null,cv);
        }
        else {
            int cat_id = getCategoryID(Parcer.tryParce(sms_from,sms_body));
            String type = getType(cat_id);
            if(type.equals("доходи")){
                addIncome(getCardID(getCard(sms_from)),Parcer.tryParce(sms_from,sms_body),Parcer.getName(sms_body),Parcer.getCount(sms_body),System.currentTimeMillis());
            }else {
                addPurchase(cat_id,getCardID(getCard(sms_from)),Parcer.getName(sms_body),Parcer.getCount(sms_body),System.currentTimeMillis());
            }
        }
    }

    private String getCard(String sms_from) {
        Cursor c = db.rawQuery("select card from Info where number = '"+sms_from+"'",null);
        c.moveToFirst();
        return c.getString(0);
    }

    public String getType(int cat_id) {
        Cursor c = db.rawQuery("select type from Category where id = '"+cat_id+"'",null);
        c.moveToFirst();
        return c.getString(0);
    }

    public String getType(String s) {
        Cursor c = db.rawQuery("select type from Category where name = '"+s+"'",null);
        c.moveToFirst();
        return c.getString(0);
    }

    public void insert(String sum, String info, String sCar, String sCat, long d, String mes) {
        String type = getType(sCat);
        if(type.equals("доходи")){
            addIncome(getCardID(sCar),sCat,info,Double.parseDouble(sum),d);
        }else {
            addPurchase(getCategoryID(sCat),getCardID(sCar),info,Double.parseDouble(sum),d);
        }
        db.delete("Sms","id = '"+mes.split(" ")[1]+"'",null);
    }
}
