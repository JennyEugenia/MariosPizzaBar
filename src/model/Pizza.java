package model;

import java.util.Arrays;

public class Pizza {
    private int pizzaNumber;
    private String name;
    private String[] ingredients;
    private double price;


    public Pizza(int pizzaNumber, String name, String[] ingredients, double price){
        this.pizzaNumber = pizzaNumber;
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
    }

    //Getters
    public int getPizzaNumber(){
        return pizzaNumber;

    }
    public String getName(){
        return name;
    }

    public String[] getIngredients(){
        return ingredients;
    }

    public double getPrice(){
        return price;
    }

    public String toString(){
        return pizzaNumber +". " + name + ": " + Arrays.toString(ingredients) +", " + price + " kr";
    }
}
