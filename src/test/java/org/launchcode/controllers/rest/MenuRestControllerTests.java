package org.launchcode.controllers.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.launchcode.controllers.IntegrationTestConfig;
import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
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
public class MenuRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDao categoryDao;

    private MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON, Charset.forName("utf8"));
    private Menu cheapMenu;
    private Cheese velveta;

    @Before
    /**
     *  Put KNOWN data in the test database.
     *  Also keep a list of the objects that should be in the database and use those to test results.
     *
     *  This will run before EACH TEST, so that each test has clean, known data to use. This prevents
     *  data pollution from test to test.
     */
    public void before() {
        menuDao.save(new Menu("Fancy"));
        Category classic = categoryDao.save(new Category("classic"));
        Category notClassic = categoryDao.save(new Category("Not Classic"));
        Cheese mild = new Cheese("Mild Cheddar", "orange", classic);
        cheeseDao.save(mild);
        Cheese cheddar = new Cheese("Cheddar", "orange", classic);
        cheeseDao.save(cheddar);
        this.velveta = new Cheese("Velveta", "orange", notClassic);
        cheeseDao.save(this.velveta);
        this.cheapMenu = new Menu("Cheap");
        this.cheapMenu.addItem(cheddar);
        this.cheapMenu.addItem(mild);
        menuDao.save(this.cheapMenu);
    }

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(jsonContentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(cheapMenu.getName())))
                .andExpect(jsonPath("$[0].id", is(cheapMenu.getId())));
                // TODO: test child cheese present?
    }

    @Test
    public void getById() throws Exception {
        mockMvc.perform(get("/api/menus/" + this.cheapMenu.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(jsonContentType))
                .andExpect(jsonPath("$.name", is(this.cheapMenu.getName())))
                .andExpect(jsonPath("$.id", is(this.cheapMenu.getId())));
                // TODO: test child cheese present?
    }

    @Test
    public void getAllCheesesByMenu() throws Exception {
        mockMvc.perform(get("/api/menus/"+ this.cheapMenu.getId() + "/cheeses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(this.cheapMenu.getCheeses().size())))
                .andExpect(jsonPath("$[0].name", not(empty())))
                .andExpect(jsonPath("$[0].description", not(empty())))
                .andExpect(jsonPath("$[1].name", not(empty())))
                .andExpect(jsonPath("$[1].description", not(empty())));
    }

    @Test
    public void postNewMenu() throws Exception {
        String json = "{\"name\":\"Ancient Cheeses\"}";
        mockMvc.perform(post("/api/menus/").content(json).contentType(jsonContentType))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", not(empty())))
                .andExpect(jsonPath("$.name", is("Ancient Cheeses")));
    }

    @Test
    public void addCheeseToMenu() throws Exception {
        String json = "{\"cheeseId\":\"" + this.velveta.getId() + "\"}";
        System.out.println(json);
        mockMvc.perform(post("/api/menus/" + this.cheapMenu.getId() + "/cheeses").content(json).contentType(jsonContentType))
                .andExpect(status().isCreated());
        // cheapMenu should now have three cheeses
        mockMvc.perform(get("/api/menus/"+ this.cheapMenu.getId() + "/cheeses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", not(empty())));
    }

    @Test
    public void removeCheeseFromMenu() throws Exception {
        Cheese toRemove = this.cheapMenu.getCheeses().get(0);
        mockMvc.perform(delete("/api/menus/" + this.cheapMenu.getId() + "/cheeses/" + toRemove.getId()))
                .andExpect(status().isCreated());
        // cheapMenu should only have one cheese left
        mockMvc.perform(get("/api/menus/"+ this.cheapMenu.getId() + "/cheeses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", not(equalTo(toRemove.getName()))));
    }

}
