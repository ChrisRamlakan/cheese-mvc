package org.launchcode.controllers.rest;

import org.launchcode.models.Cheese;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/cheeses")
public class CheeseRestController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDao categoryDao;

    @GetMapping
    public Iterable<Cheese> getAllCheeses() {
        return cheeseDao.findAllByOrderByName();
    }

    @GetMapping("/{id}")
    public Cheese getCheeseById(@PathVariable int id) {
        return cheeseDao.findOne(id);
    }

    @GetMapping("/category/{id}")
    public Iterable<Cheese> getAllCheesesByCategory(@PathVariable int id) {
        return cheeseDao.findByCategory(categoryDao.findOne(id));
    }
}
