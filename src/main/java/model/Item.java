package model;

import java.util.List;

public class Item {
    public final String by;
    public final int id;
    public final List<Integer> kids;
    public final String type;

    public Item(String by, Integer id, List<Integer> kids, String type) {
        this.by = by;
        this.id = id;
        this.kids = kids;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Item{" +
                "by='" + by + '\'' +
                ", id=" + id +
                ", kids=" + kids +
                ", type='" + type + '\'' +
                '}';
    }
}
