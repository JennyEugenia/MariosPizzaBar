package Model;

// NormalCustomer arver fra Customer
public class NormalCustomer extends Customer {

    // Constructor sender navnet videre til Customer
    public NormalCustomer(String name) {
        super(name); // kalder constructor i Customer
    }

    // Override af getDiscount
    // Normale kunder får ingen rabat
    @Override
    public double getDiscount() {
        return 0.0;
    }

    // toString fungerer automatisk via Customer
}