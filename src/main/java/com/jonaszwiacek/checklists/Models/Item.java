package com.jonaszwiacek.checklists.Models;

public class Item {
    private static long ID_COUNTER = 0;

    private String name;
    private long id;
    private boolean checked;


    public Item(String name, boolean checked) {
        id = ID_COUNTER++;
        this.name = name;
        this.checked = checked;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public boolean isChecked() {
        return checked;
    }
}
