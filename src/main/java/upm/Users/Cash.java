package upm.Users;

import static upm.Users.CashManager.generateRandomIdentifier;


public class Cash extends User {
    private String cashId;

    public Cash(String identifier, String name, String email) {
        super(name, email,identifier);
    }


    public Cash(String name, String email) {
        this(generateRandomIdentifier(),name,email);
    }


    @Override
    public String toString() {
        return " Cash{identifier='" + id + "', name='" + name + "', email='" + email + "'}";
    }
}