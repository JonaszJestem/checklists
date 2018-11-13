package com.jonaszwiacek.checklists.Models;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;


@Repository
public class ChecklistRepository {
    private List<Checklist> checklists = new ArrayList<>();


    public void deleteByName(String name) {
        Checklist checklistToFind = new Checklist(name);
        checklists.removeIf((checklist -> checklist.equals(checklistToFind)));
    }


    public List<Item> getItemsByChecklistName(String checklistName) {
        Checklist checklistToFind = new Checklist(checklistName);

        Optional<Checklist> foundChecklist = checklists.stream()
                .filter(checklist -> checklist.equals(checklistToFind)).findFirst();

        return foundChecklist.isPresent() ? foundChecklist.get().items : emptyList();
    }

    public List<String> getAll() {
        return checklists.stream().map((checklist -> checklist.name)).collect(Collectors.toList());
    }

    public void add(String name) {
        checklists.add(new Checklist(name));
    }

    public Checklist getByName(String name) {
        Checklist checklistToFind = new Checklist(name);

        Optional<Checklist> foundChecklist = checklists.stream()
                .filter(checklist -> checklist.equals(checklistToFind)).findFirst();

        return foundChecklist.orElse(null);
    }

    public Item addItemToChecklistByName(String checklist, String itemName) {
        Checklist resolvedChecklist = getByName(checklist);
        if(resolvedChecklist != null) {
            Item item = new Item(itemName, false);
            resolvedChecklist.items.add(item);
            return item;
        }
        return null;
    }
}
