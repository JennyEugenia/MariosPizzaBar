package model;

// EmployeeCustomer arver fra Customer
public class EmployeeCustomer extends Customer {

    // Constructor sender navnet videre til Customer
    public EmployeeCustomer(String name) {
        super(name); // kalder constructor i Customer
    }

    // Medarbejdere får 20% rabat
    @Override
    public double getDiscount() {
        return 0.20;
    }

    // toString fungerer automatisk via Customer
}