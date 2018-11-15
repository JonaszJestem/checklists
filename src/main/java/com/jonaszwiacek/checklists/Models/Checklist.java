package com.jonaszwiacek.checklists.Models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "Checklists")
public class Checklist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String name;
    @OneToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "checklist_id")
    private List<Item> items;


    public Checklist() {
        items = new ArrayList<>();
    }

    public Checklist(String name) {
        this();
        this.name = name;
    }

    public void addItem(String itemName) {
        Item item = new Item(itemName);
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Checklist checklist = (Checklist) o;
        return Objects.equals(name, checklist.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Checklist{" +
                "id=" + id +
                ", items=" + items +
                ", name='" + name + '\'' +
                '}';
    }
}
