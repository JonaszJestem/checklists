package com.jonaszwiacek.checklists.Controllers;

import com.jonaszwiacek.checklists.Models.Item;
import com.jonaszwiacek.checklists.Services.ChecklistService;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public String addChecklist(@RequestBody String checklist) {
        checklistService.newChecklist(checklist);

        return "New checklist inserted.";
    }

    @DeleteMapping("/{checklist}")
    public String deleteChecklist(@PathVariable String checklist) {
        checklistService.deleteChecklist(checklist);

        return "OK.";
    }

    @GetMapping("/{checklist}/items")
    public List<Item> getItems(@PathVariable String checklist) {
        return checklistService.getItems(checklist);
    }

    @PostMapping("/{checklist}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public long addItem(@PathVariable String checklist, @RequestBody String itemName) {
        return checklistService.addItem(checklist, itemName);
    }

    @DeleteMapping("/{checklist}/items/{itemId}")
    public String deleteItem(@PathVariable String checklist, @PathVariable long itemId) {
        checklistService.deleteItem(checklist, itemId);

        return "OK.";
    }

    @PatchMapping("/{checklist}/items/{itemId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String markItem(@PathVariable String checklist, @PathVariable long itemId, @RequestBody String shouldBeChecked) {
        boolean checked = Boolean.parseBoolean(shouldBeChecked);
        checklistService.markItem(checklist, itemId, checked);

        return "OK.";
    }
}
