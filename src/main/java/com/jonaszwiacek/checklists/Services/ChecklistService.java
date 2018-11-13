package com.jonaszwiacek.checklists.Services;

import com.jonaszwiacek.checklists.Models.ChecklistRepository;
import com.jonaszwiacek.checklists.Models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChecklistService {
    private final ChecklistRepository checklistRepository;

    @Autowired
    public ChecklistService(ChecklistRepository checklistRepository) {
        this.checklistRepository = checklistRepository;
    }


    public int newChecklist(String name) {
        if (checklistExists(name)) {
            return 409;
        } else {
            checklistRepository.add(name);
            return 201;
        }
    }

    private boolean checklistExists(String name) {
        return checklistRepository.getByName(name) != null;
    }


    public int removeChecklist(String name) {
        if (!checklistExists(name)) {
            return 404;
        } else {
            checklistRepository.deleteByName(name);
            return 200;
        }
    }


    public List<Item> getItems(String checklistName) {
        return checklistRepository.getItemsByChecklistName(checklistName);
    }

    public List<String> getChecklists() {
        return checklistRepository.getAll();
    }

    public long addItem(String checklist, String itemName) {
        Item item = checklistRepository.addItemToChecklistByName(checklist, itemName);
        if(item != null) {
            return item.getId();
        }
        return -1;
    }
}
