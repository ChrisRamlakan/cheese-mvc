package org.launchcode.controllers.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.launchcode.controllers.IntegrationTestConfig;
import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@IntegrationTestConfig
public class CategoryRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryDao categoryDao;

    private MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON, Charset.forName("utf8"));
    private Category classic;
    private Category notClassic;

    /**
     *  Put KNOWN data in the test database.
     *  Also keep a list of the objects that should be in the database and use those to test results.
     *
     *  This will run before EACH TEST, so that each test has clean, known data to use. This prevents
     *  data pollution from test to test.
     */
    @Before
    public void before() {
        this.classic = categoryDao.save(new Category("Classic"));
        this.notClassic = categoryDao.save(new Category("Not Classic"));
    }

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(jsonContentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(this.classic.getName())))
                .andExpect(jsonPath("$[0].id", is(this.classic.getId())));
                // TODO: make sure category doesn't contain nested cheeses
    }

    @Test
    public void postNewCheese() throws Exception {
        String json = "{\"name\":\"Danger cheeses\"}";
        mockMvc.perform(post("/api/categories").content(json).contentType(jsonContentType))
                .andExpect(content().contentType(jsonContentType))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", not(empty())))
                .andExpect(jsonPath("$.name", is("Danger cheeses")));
    }

    @Test
    public void deleteCheese() throws Exception {
        mockMvc.perform(delete("/api/categories/" + this.notClassic.getId()))
                .andExpect(status().isOk());
    }

}
