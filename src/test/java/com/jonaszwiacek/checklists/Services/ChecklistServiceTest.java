package com.jonaszwiacek.checklists.Services;

import com.jonaszwiacek.checklists.Models.Item;
import com.jonaszwiacek.checklists.Services.Exceptions.ChecklistNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ChecklistServiceTest {

    @Autowired
    private ChecklistService checklistService;

    @Test
    public void addsNewChecklists() {
        String checklist = "checklist";

        checklistService.newChecklist(checklist);
        List<String> checklists = checklistService.getChecklists();

        assertTrue(checklists.contains(checklist));
    }

    @Test
    public void deletesExistingChecklist() {
        String checklist = "checklist";

        checklistService.newChecklist(checklist);
        checklistService.deleteChecklist(checklist);
        List<String> checklists = checklistService.getChecklists();

        assertFalse(checklists.contains(checklist));
    }

    @Test(expected = ChecklistNotFoundException.class)
    public void returnsNotFoundWhenNoChecklistToDelete() {
        String checklist = "checklist";

        checklistService.deleteChecklist(checklist);
        checklistService.getChecklists();
    }

    @Test
    public void addsItemsToChecklist() {
        String checklist = "checklist";
        String item = "Jabłka";

        checklistService.newChecklist(checklist);
        checklistService.addItem(checklist, item);
        List<Item> items = checklistService.getItems(checklist);

        assertEquals("Jabłka", items.get(0).name);
    }

    @Test
    public void marksItemsFromChecklist() {
        String checklist = "checklist";
        String item = "Jabłka";
        boolean checked = true;

        checklistService.newChecklist(checklist);
        long itemId = checklistService.addItem(checklist, item);
        checklistService.markItem(checklist, itemId, checked);
        List<Item> items = checklistService.getItems(checklist);

        assertTrue(items.get(0).isChecked());
    }

    @Test
    public void deletesItemsFromChecklist() {
        String checklist = "checklist";
        String item = "Jabłka";

        checklistService.newChecklist(checklist);
        long itemId = checklistService.addItem(checklist, item);
        checklistService.deleteItem(checklist, itemId);
        List<Item> items = checklistService.getItems(checklist);

        assertTrue(items.isEmpty());
    }
}
