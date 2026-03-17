package Model;

// Kreation af abstract super class ("Customer")
public abstract class Customer {

    // private = kun tilgængelig i denne klasse
    private final String name; // final = kan ikke ændres efter oprettelse

    // Constructor til at lave en kunde (unikke objekter)
    public Customer(String name) {
        this.name = name; // gemmer navnet
    }

    // Getter til at hente kundens navn
    public String getName() {
        return name;
    }

    // Abstract metode til beregning af rabat,
    // som alle subclasses skal have deres egen version af
    public abstract double getDiscount();

    // toString til at printe objektet
    @Override
    public String toString() {
        // Bemærk: vi bruger getName() frem for direkte adgang til name
        return getName() + " (discount: " + (getDiscount() * 100) + "%)";
    }
}