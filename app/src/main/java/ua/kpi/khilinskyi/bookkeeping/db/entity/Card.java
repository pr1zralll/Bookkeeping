package ua.kpi.khilinskyi.bookkeeping.db.entity;

/**
 * Created by Pavel on 08.06.2016.
 */
public class Card {
    public int id;
    public String name;
    public double balance=0;

    public Card(int id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return id+". "+name+" ("+balance+")";
    }
}
