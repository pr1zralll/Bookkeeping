package ua.kpi.khilinskyi.bookkeeping.db.entity;

/**
 * Created by Pavel on 14.06.2016.
 */
public class Category {
    public String name;
    public float count;

    public Category(String name, float count) {
        this.name = name;
        this.count = count;
    }
}
