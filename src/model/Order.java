package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter; //til brug af redigering af dato & tidspunkt.
import java.util.List;

public class Order {
    private int orderId;
    private Customer customer;
    private List<Pizza> pizzas;
    private OrderStatus status;
    private LocalDateTime pickupTime;
    private LocalDateTime orderTime;
    private String comment;

    // Opretter 2 tidsformater, én under aktive ordre (mere overskueligt) og hertil den anden i statistik, mere detaljeret.
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public Order(int orderId, Customer customer, LocalDateTime pickupTime) {
        this.orderId = orderId;
        this.customer = customer;
        this.pizzas = new ArrayList<>();
        this.status = OrderStatus.MODTAGET;
        this.orderTime = LocalDateTime.now();
        this.pickupTime = pickupTime;
        this.comment = "";
    }

    public void addPizza(Pizza pizza) {
        pizzas.add(pizza);
    }

    public double getTotalPrice() {
        double total = 0;
        for (Pizza pizza : pizzas) {
            total += pizza.getPrice();
        }
        return total * (1 - customer.getDiscount());
    }

    public int getOrderId()              {
        return orderId; }
    public Customer getCustomer()        {
        return customer; }
    public List<Pizza> getPizzas()       {
        return pizzas; }
    public OrderStatus getStatus()       {
        return status; }
    public LocalDateTime getPickupTime() {
        return pickupTime; }
    public String getComment(){
        return comment; }
    public void setComment(String comment){
        this.comment = comment;
    }

    // FORMATTER METODER der bruges i de 2 forskellige statusser.
    public String getFormattedPickupTime() {
        return pickupTime.format(TIME_FORMATTER);
    }

    public String getFormattedPickupTimeWithDate() {
        return pickupTime.format(DATE_TIME_FORMATTER);
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String commentLine = (comment != null && !comment.isEmpty())
                ? " | Kommentar: " + comment
                : "";

        StringBuilder pizzaLine = new StringBuilder(" | Pizzaer: ");
        for (int i = 0; i < pizzas.size(); i++) {
            pizzaLine.append(pizzas.get(i).getName());
            if (i < pizzas.size() - 1) pizzaLine.append(", ");
        }

        return "Ordre #" + orderId +
                " | " + customer.getName() +
                " | Afhentning: " + getFormattedPickupTime() +
                " | " + status + pizzaLine + commentLine +
                " | Pris: " + String.format("%.2f", getTotalPrice()) + " kr.";
    }
}