package org.launchcode.models.dto;

/**
 * Created by LaunchCode.
 * This is only used as an object that Spring will bind the json request to.
 */
public class CheeseDTO {

    private int id;
    private String name;
    private String description;
    private int categoryID;

    public CheeseDTO() { }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
}
