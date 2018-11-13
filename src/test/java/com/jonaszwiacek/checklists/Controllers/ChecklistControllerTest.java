package com.jonaszwiacek.checklists.Controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ChecklistControllerTest {
    private static final Pattern NUMBER_PATTTERN = Pattern.compile("[0-9]+");
    @Autowired
    private MockMvc mockMvc;
    private String emptyJSONArray = "[]";

    @Test
    public void getChecklist_isEmptyInTheBeginning() throws Exception {
        getLists(HttpStatus.OK, emptyJSONArray);
    }

    @Test
    public void postChecklist_allowsAddingChecklist() throws Exception {
        String checklist = "Zakupy";
        String expectedChecklists = "[\"Zakupy\"]";

        getLists(HttpStatus.OK, emptyJSONArray);
        addList(checklist, 201);
        getLists(HttpStatus.OK, expectedChecklists);
    }

    @Test
    public void postChecklist_rejectsAdddingIfAlreadyExists() throws Exception {
        String checklist = "Zakupy";

        addList(checklist, 201);
        addList(checklist, 409);
    }

    @Test
    public void deleteChecklists_removesGivenChecklist() throws Exception {
        List<String> checklists = new ArrayList<>(asList("Zakupy", "Prezenty", "Do zrobienia do środy"));
        String expectedChecklists = "[\"Zakupy\", \"Prezenty\"]";
        String checkListToDelete = "Do zrobienia do środy";
        addFakeChecklists(checklists);


        deleteList(checkListToDelete, 200);
        getLists(HttpStatus.OK, expectedChecklists);
    }

    @Test
    public void deleteChecklists_returnsNotFoundWhenNoMatchingChecklist() throws Exception {
        String checkListToDelete = "Non existing";
        deleteList(checkListToDelete, 404);
    }

    @Test
    public void getItems_returnsEmptyListWithNewlyCreatedChecklist() throws Exception {
        String checklist = "Zakupy";

        addList(checklist, 201);
        getItems(checklist, 200, emptyJSONArray);
    }

    @Test
    public void postItems_addItemAndReturnsItsID() throws Exception {
        String checklist = "Zakupy";
        String item = "Jabłka";

        addList(checklist, 201);
        MvcResult result = postItemsAndGetReturnValue(checklist, item, HttpStatus.CREATED);
        String response = result.getResponse().getContentAsString();

        assertTrue(isNumber(response));
    }

    @Test
    public void postItems_returnsNotFoundWhenNoMatchingChecklist() throws Exception {
        String checklist = "Zakupy";
        String item = "Jabłka";

        postItems(checklist, item, HttpStatus.NOT_FOUND);
    }

    @Test
    public void getItems_returnsListWithItems() throws Exception {
        String checklist = "Zakupy";
        String item = "Jabłka";

        addList(checklist, 201);
        this.mockMvc.perform(get("/lists/{checklist}/items", checklist))
                .andExpect(status().is(200));
    }


    private void addList(String checklist, int expectedStatus) throws Exception {
        this.mockMvc.perform(post("/lists").content(checklist)).andExpect(status().is(expectedStatus));
    }

    private void deleteList(String checklist, int expectedStatus) throws Exception {
        this.mockMvc.perform(delete("/lists/{checklist}", checklist)).andExpect(status().is(expectedStatus));
    }

    private MvcResult postItemsAndGetReturnValue(String checklist, String item, HttpStatus expectedStatus) throws Exception {
        return this.mockMvc.perform(post("/lists/{checklist}/items", checklist).content(item))
                .andExpect(status().is(expectedStatus.value())).andReturn();
    }

    private void postItems(String checklist, String item, HttpStatus expectedStatus) throws Exception {
        this.mockMvc.perform(post("/lists/{checklist}/items", checklist).content(item))
                .andExpect(status().is(expectedStatus.value()));
    }

    private void addFakeChecklists(List<String> checklists) {
        checklists.forEach((checklist) -> {
            try {
                mockMvc.perform(post("/lists").contentType(MediaType.APPLICATION_JSON_UTF8).content(checklist));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    private void getLists(HttpStatus expectedStatus, String expectedContent) throws Exception {
        this.mockMvc.perform(get("/lists")).andExpect(status().is(expectedStatus.value())).andExpect(content().json(expectedContent));
    }

    private boolean isNumber(String response) {
        return NUMBER_PATTTERN.matcher(response).matches();
    }

    private void getItems(String checklist, int expectedStatusCode, String expectedContent) throws Exception {
        this.mockMvc.perform(get("/lists/{checklist}/items", checklist))
                .andExpect(status().is(expectedStatusCode))
                .andExpect(content().json(expectedContent));
    }
}
