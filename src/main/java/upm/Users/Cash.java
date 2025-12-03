package upm.Users;

import static upm.Users.CashManager.generateRandomIdentifier;


public class Cash extends User {
    private String identifier;

    public Cash(String identifier, String name, String email) {
        super(name, email);
        this.identifier = identifier;
    }


    public Cash(String name, String email) {
        this(generateRandomIdentifier(),name,email);
    }


    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return " Cash{identifier='" + identifier + "', name='" + name + "', email='" + email + "'}";
    }
}