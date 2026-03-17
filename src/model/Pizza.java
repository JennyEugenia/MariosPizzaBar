package model;

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
    public int getNumber(){
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
        return name + price + " kr";
    }
}
