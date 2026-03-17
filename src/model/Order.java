package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private Customer customer;        // NormalCustomer, VIPCustomer eller EmployeeCustomer
    private List<Pizza> pizzas;
    private OrderStatus status;
    //private LocalDateTime pickupTime; // Hvornår skal den afhentes?
    //private LocalDateTime orderTime;  // Hvornår blev den bestilt?

    public Order(int orderId, Customer customer, LocalDateTime pickupTime) {
        this.orderId = orderId;
        this.customer = customer;
        this.pizzas = new ArrayList<>();
        this.status = OrderStatus.MODTAGET;
        //this.orderTime = LocalDateTime.now();
        //this.pickupTime = pickupTime;
    }

    public void addPizza(Pizza pizza) {
        pizzas.add(pizza);
    }

    // Udregner samlet pris – rabatten håndteres udfra hvert Customertype eller Kundetype.
    public double getTotalPrice() {
        double total = 0;
        for (Pizza pizza : pizzas) {
            total += pizza.getPrice();
        }
        return customer.applyDiscount(total); // Polymorfi!
    }

    // Getters
    public int getOrderId()             {
        return orderId; }

    public Customer getCustomer()       {
        return customer; }

    public List<Pizza> getPizzas()      {
        return pizzas; }

    public OrderStatus getStatus()      {
        return status; }

    /*public LocalDateTime getPickupTime(){
        return pickupTime; }

    public LocalDateTime getOrderTime() {
        return orderTime; }*/

    // Setter – bruges når Alfonso markerer en pizza som klar
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    /*@Override
    public String toString() {
        return "Ordre #" + orderId +
                " | " + customer.getName() +
                " | Afhentning: " + pickupTime +
                " | " + status +
                " | Pris: " + getTotalPrice() + " kr.";*/
    }
}
