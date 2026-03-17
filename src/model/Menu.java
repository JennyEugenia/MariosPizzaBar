package model;

import java.util.ArrayList;

public class Menu {
    private ArrayList<Pizza> pizzas;

    public void menu(){
        pizzas = new ArrayList<>();
        loadMenu();
    }

    private void loadMenu(){
        pizzas.add(new Pizza(1, "Vesuvio",
                new String[]{"tomatsauce", "ost", "skinke", "oregano"}, 57.0));

        pizzas.add(new Pizza(2, "Amerikaner",
                new String[]{"tomatsauce", "ost", "oksefars", "oregano"}, 53.0));

        pizzas.add(new Pizza(3, "Cacciatore",
                new String[]{"tomatsauce", "ost", "pepperoni", "oregano"}, 57.0));

        pizzas.add(new Pizza(4, "Carbona",
                new String[]{"tomatsauce", "ost", "kødsauce", "spaghetti", "cocktailpølser", "oregano"}, 63.0));

        pizzas.add(new Pizza(5, "Dennis",
                new String[]{"tomatsauce", "ost", "skinke", "pepperoni", "cocktailpølser", "oregano"}, 65.0));

        pizzas.add(new Pizza(6, "Bertil",
                new String[]{"tomatsauce", "ost", "bacon", "oregano"}, 57.0));

        pizzas.add(new Pizza(7, "Silvia",
                new String[]{"tomatsauce", "ost", "pepperoni", "rød peber", "løg", "oliven", "oregano"}, 61.0));

        pizzas.add(new Pizza(8, "Victoria",
                new String[]{"tomatsauce", "ost", "skinke", "ananas", "champignon", "løg", "oregano"}, 61.0));

        pizzas.add(new Pizza(9, "Toronfo",
                new String[]{"tomatsauce", "ost", "skinke", "bacon", "kebab", "chili", "oregano"}, 61.0));

        pizzas.add(new Pizza(10, "Capricciosa",
                new String[]{"tomatsauce", "ost", "skinke", "champignon", "oregano"}, 61.0));

        pizzas.add(new Pizza(11, "Hawaii",
                new String[]{"tomatsauce", "ost", "skinke", "ananas", "oregano"}, 61.0));

        pizzas.add(new Pizza(12, "Le Blissola",
                new String[]{"tomatsauce", "ost", "skinke", "rejer", "oregano"}, 61.0));

        pizzas.add(new Pizza(13, "Venezia",
                new String[]{"tomatsauce", "ost", "skinke", "bacon", "oregano"}, 61.0));

        pizzas.add(new Pizza(14, "Mafia",
                new String[]{"tomatsauce", "ost", "pepperoni", "bacon", "løg", "oregano"}, 61.0));

    }

    public Pizza findPizza(int number){
        for ( Pizza pizza : pizzas){
            if ( pizza.getNumber() == number){
                return pizza;
            }
        }
        return null;
    }

    public void displayMenu(){
        for (Pizza pizza : pizzas){
            System.out.println(pizzas);
        }
    }

    public ArrayList<Pizza>getPizza(){
        return pizzas;
    }

}
