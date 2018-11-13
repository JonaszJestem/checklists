package com.jonaszwiacek.checklists.Controllers;

import com.jonaszwiacek.checklists.Models.Item;
import com.jonaszwiacek.checklists.Services.ChecklistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/lists")
public class ChecklistController {
    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    private final ChecklistService checklistService;

    @GetMapping
    public List<String> getChecklists() {
        return checklistService.getChecklists();
    }

    @PostMapping
    public ResponseEntity addChecklist(@RequestBody String checklist) {
        int responseStatus = checklistService.newChecklist(checklist);
        return new ResponseEntity(HttpStatus.valueOf(responseStatus));
    }

    @DeleteMapping("/{checklist}")
    public ResponseEntity removeChecklist(@PathVariable String checklist) {
        int responseStatus = checklistService.removeChecklist(checklist);
        return new ResponseEntity(HttpStatus.valueOf(responseStatus));
    }

    @GetMapping("/{checklist}/items")
    public List<Item> getItems(@PathVariable String checklist) {
        return checklistService.getItems(checklist);
    }

    @PostMapping("/{checklist}/items")
    public ResponseEntity<Long> addItem(@PathVariable String checklist, @RequestBody String itemName) {
        long itemID = checklistService.addItem(checklist, itemName);
        if (itemID != -1) {
            return new ResponseEntity<>(itemID, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
