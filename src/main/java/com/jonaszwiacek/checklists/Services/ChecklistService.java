package com.jonaszwiacek.checklists.Services;

import com.jonaszwiacek.checklists.Models.Checklist;
import com.jonaszwiacek.checklists.Models.Item;
import com.jonaszwiacek.checklists.Repositories.ChecklistRepository;
import com.jonaszwiacek.checklists.Repositories.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ChecklistService {
    private final ChecklistRepository checklistRepository;
    private final ItemsRepository itemsRepository;

    @Autowired
    public ChecklistService(ChecklistRepository checklistRepository, ItemsRepository itemsRepository) {
        this.checklistRepository = checklistRepository;
        this.itemsRepository = itemsRepository;
    }


    public void newChecklist(String name) {
        if (!checklistRepository.existsByName(name)) {
            Checklist checklist = new Checklist(name);
            checklistRepository.save(checklist);
        } else {
            throw new ChecklistAlreadyExistsException();
        }
    }

    public void deleteChecklist(String name) {
        if (checklistRepository.existsByName(name)) {
            checklistRepository.deleteByName(name);
        } else {
            throw new ChecklistNotFoundException();
        }
    }


    public List<Item> getItems(String checklistName) {
        Checklist checklist = checklistRepository.findByName(checklistName);
        if (checklist != null) {
            return checklist.getItems();
        }
        return Collections.emptyList();
    }


    public List<String> getChecklists() {
        List<Checklist> checklists = checklistRepository.findAll();
        return checklists.stream()
                .map((checklist -> checklist.name))
                .collect(Collectors.toList());
    }

    public long addItem(String checklistName, String itemName) {
        Checklist checklist = checklistRepository.findByName(checklistName);
        if (checklist != null) {
            checklist.addItem(itemName);
            checklistRepository.save(checklist);
            return findItemId(checklistName, itemName);
        }
        return -1;
    }

    public void markItem(String checklistName, long itemId, boolean checked) {
        Checklist checklist = checklistRepository.findByName(checklistName);

        List<Item> items = checklist.getItems();
        Optional<Item> item = items.stream().filter(it -> it.getId() == itemId).findFirst();

        if (item.isPresent()) {
            Item retrievedItem = item.get();
            items.remove(retrievedItem);

            retrievedItem.mark(checked);
            items.add(retrievedItem);

            checklist.setItems(items);
            checklistRepository.save(checklist);
        } else {
            throw new ItemNotFoundException();
        }
    }


    public void deleteItem(String checklistName, long itemId) {
        Checklist checklist = checklistRepository.findByName(checklistName);

        List<Item> items = checklist.getItems();
        Optional<Item> item = items.stream().filter(it -> it.getId() == itemId).findFirst();

        if (item.isPresent()) {
            Item retrievedItem = item.get();
            items.remove(retrievedItem);

            checklist.setItems(items);
            checklistRepository.save(checklist);
        } else {
            throw new ItemNotFoundException();
        }
    }

    private long findItemId(String checklistName, String itemName) {
        Checklist checklist = checklistRepository.findByName(checklistName);
        Optional<Item> item = checklist.getItems().stream()
                .filter(i -> i.getName().equals(itemName))
                .findFirst();
        return item.isPresent() ? item.get().getId() : -1;
    }
}
