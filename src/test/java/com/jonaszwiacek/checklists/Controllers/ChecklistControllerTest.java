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
import java.util.Arrays;
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
        this.mockMvc.perform(get("/lists"))
                .andExpect(status()
                        .is(HttpStatus.OK.value()))
                .andExpect(content()
                        .json(emptyJSONArray));
    }

    @Test
    public void postChecklist_allowsAddingChecklist() throws Exception {
        String checklist = "Zakupy";
        String expectedChecklists = "[\"Zakupy\"]";

        this.mockMvc.perform(get("/lists"))
                .andExpect(status()
                        .is(HttpStatus.OK.value()))
                .andExpect(content()
                        .json(emptyJSONArray));
        this.mockMvc.perform(post("/lists").content(checklist))
                .andExpect(status()
                        .is(HttpStatus.CREATED.value()));

        this.mockMvc.perform(get("/lists"))
                .andExpect(status()
                        .is(HttpStatus.OK.value()))
                .andExpect(content()
                        .json(expectedChecklists));
    }

    @Test
    public void postChecklist_rejectsAdddingIfAlreadyExists() throws Exception {
        String checklist = "Zakupy";

        this.mockMvc.perform(post("/lists").content(checklist))
                .andExpect(status().is(HttpStatus.CREATED.value()));
        this.mockMvc.perform(post("/lists").content(checklist))
                .andExpect(status().is(HttpStatus.CONFLICT.value()));
    }

    @Test
    public void deleteChecklists_removesGivenChecklist() throws Exception {
        List<String> checklists = new ArrayList<>(asList("Zakupy", "Prezenty", "Do zrobienia do środy"));
        String expectedChecklists = "[\"Zakupy\", \"Prezenty\"]";
        String checkListToDelete = "Do zrobienia do środy";
        checklists.forEach((checklist) -> {
            try {
                mockMvc.perform(post("/lists").contentType(MediaType.APPLICATION_JSON_UTF8).content(checklist));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        this.mockMvc.perform(delete("/lists/{checklist}", checkListToDelete))
                .andExpect(status().is(HttpStatus.OK.value()));
        this.mockMvc.perform(get("/lists"))
                .andExpect(status()
                        .is(HttpStatus.OK.value()))
                .andExpect(content()
                        .json(expectedChecklists));
    }

    @Test
    public void deleteChecklists_returnsNotFoundWhenNoMatchingChecklist() throws Exception {
        String checkListToDelete = "Non existing";
        this.mockMvc.perform(delete("/lists/{checklist}", checkListToDelete))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void getItems_returnsEmptyListWithNewlyCreatedChecklist() throws Exception {
        String checklist = "Zakupy";

        this.mockMvc.perform(post("/lists").content(checklist))
                .andExpect(status().is(HttpStatus.CREATED.value()));
        this.mockMvc.perform(get("/lists/{checklist}/items", checklist))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().json(emptyJSONArray));
    }

    @Test
    public void postItems_addItemAndReturnsItsID() throws Exception {
        String checklist = "Zakupy";
        String item = "Jabłka";

        this.mockMvc.perform(post("/lists").content(checklist))
                .andExpect(status()
                        .is(HttpStatus.CREATED.value()));
        MvcResult result = this.mockMvc.perform(post("/lists/{checklist}/items", checklist).content(item))
                .andExpect(status()
                        .is(HttpStatus.CREATED.value()))
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(isNumber(response));
    }

    @Test
    public void postItems_returnsNotFoundWhenNoMatchingChecklist() throws Exception {
        String checklist = "Zakupy";
        String item = "Jabłka";

        this.mockMvc.perform(post("/lists/{checklist}/items", checklist).content(item))
                .andExpect(status()
                        .is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void getItems_returnsListWithItems() throws Exception {
        String checklist = "Zakupy";
        String item = "Jabłka";
        String expectedItems = "[{name: Jabłka, checked: false}]";

        this.mockMvc.perform(post("/lists").content(checklist))
                .andExpect(status()
                        .is(HttpStatus.CREATED.value()));
        this.mockMvc.perform(post("/lists/{checklist}/items", checklist).content(item))
                .andExpect(status()
                        .is(HttpStatus.CREATED.value()));
        this.mockMvc.perform(get("/lists/{checklist}/items", checklist))
                .andExpect(status()
                        .is(HttpStatus.OK.value()))
                .andExpect(content()
                        .json(expectedItems));
    }

    @Test
    public void getItems_returnsListWithMultipleItems() throws Exception {
        String checklist = "Zakupy";
        List<String> items = new ArrayList<>(Arrays.asList("Jabłka", "Jajka"));
        String expectedItems = "[{name: Jabłka, checked: false}, {name: Jajka, checked: false}]";

        this.mockMvc.perform(post("/lists").content(checklist))
                .andExpect(status()
                        .is(HttpStatus.CREATED.value()));

        items.forEach((item) -> {
                    try {
                        this.mockMvc.perform(post("/lists/{checklist}/items", checklist).content(item))
                                .andExpect(status()
                                        .is(HttpStatus.CREATED.value()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

        this.mockMvc.perform(get("/lists/{checklist}/items", checklist))
                .andExpect(status()
                        .is(HttpStatus.OK.value()))
                .andExpect(content()
                        .json(expectedItems));
    }


    private boolean isNumber(String response) {
        return NUMBER_PATTTERN.matcher(response).matches();
    }

}
