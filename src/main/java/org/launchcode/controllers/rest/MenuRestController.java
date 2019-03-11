package org.launchcode.controllers.rest;

import org.json.JSONObject;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/")
public class MenuRestController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private MenuDao menuDao;

    @GetMapping("/menus")
    public Iterable<Menu> getAll() {
        return menuDao.findAllByOrderByName();
    }

    @GetMapping("/menus/{id}")
    public Menu getById(@PathVariable int id) {
        return menuDao.findOne(id);
    }

    @PostMapping("/menus")
    @ResponseStatus(HttpStatus.CREATED)
    public Menu addNewMenu(@RequestBody Menu menu) {
        return menuDao.save(menu);
    }

    @GetMapping("/menus/{id}/cheeses")
    public Iterable<Cheese> getCheeseForMenu(@PathVariable int id) {
        return menuDao.findOne(id).getCheeses();
    }

    @PostMapping("/menus/{menuId}/cheeses")
    @ResponseStatus(HttpStatus.CREATED)
    public void postNewCheese(@PathVariable int menuId, @RequestBody String json) {
        // json should only contain {cheeseId: 83}, but I could not figure out how to get spring to bind it
        // automatically, so using a library to get the value of the cheeseId attribute
        JSONObject object = new JSONObject(json);
        String cheeseIdString = (String) object.get("cheeseId");
        int cheeseId = Integer.parseInt(cheeseIdString);

        Cheese cheese = cheeseDao.findOne(cheeseId);
        Menu menu = menuDao.findOne(menuId);
        menu.addItem(cheese);
        menuDao.save(menu);
    }

    @DeleteMapping("/menus/{menuId}/cheeses/{cheeseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void postNewCheese(@PathVariable int menuId, @PathVariable int cheeseId) {
        Menu menu = menuDao.findOne(menuId);
        Cheese cheese = cheeseDao.findOne(cheeseId);
        menu.getCheeses().remove(cheese);
        menuDao.save(menu);
    }
}
