package com.thisisabir.sqliteimage.model;

public class FoodModel {

    private int id;
    private String foodname;
    private String foodprice;
    private byte[] image;

    public FoodModel(int id, String foodname, String foodprice, byte[] image) {
        this.id = id;
        this.foodname = foodname;
        this.foodprice = foodprice;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodprice() {
        return foodprice;
    }

    public void setFoodprice(String foodprice) {
        this.foodprice = foodprice;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
