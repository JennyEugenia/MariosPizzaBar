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
     //format of menu (%d=int, %s=String)
    public String toString() {
        String ingredientStr = String.join(", ", ingredients);

        String left = String.format("%d. %s: %s", pizzaNumber, name, ingredientStr);

        int totalLength = 80; // length of line
        int dots = totalLength - left.length();

        String dotStr = ".".repeat(Math.max(0, dots));

        return left + dotStr + price + " kr";
    }
}
