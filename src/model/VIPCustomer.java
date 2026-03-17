package model;

// VIPCustomer arver fra Customer
public class VIPCustomer extends Customer {

    // Constructor sender navnet videre til Customer
    public VIPCustomer(String name) {
        super(name); // kalder constructor i Customer
    }

    // VIP kunder får 10% rabat
    @Override
    public double getDiscount() {
        return 0.10;
    }

    // toString fungerer automatisk via Customer
}