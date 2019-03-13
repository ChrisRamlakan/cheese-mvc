package org.launchcode.controllers.rest;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.dto.CheeseDTO;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



// TODO: be specific about which origins to allow
@CrossOrigin
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cheese postNewCheese(@RequestBody CheeseDTO cheeseDTO) {
        Cheese cheese = new Cheese(cheeseDTO);
        Category category = categoryDao.findOne(cheeseDTO.getCategoryID());
        cheese.setCategory(category);
        return cheeseDao.save(cheese);
    }

    @DeleteMapping("/{id}")
    public void deleteCheeseById(@PathVariable int id) {
        cheeseDao.delete(id);
    }

}
