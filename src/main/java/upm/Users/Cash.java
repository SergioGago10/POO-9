package upm.Users;

import static upm.Users.CashManager.generateRandomIdentifier;


public class Cash extends User {
    private String identifier;

    public Cash(String identifier, String name, String email) {
        super(name, email, identifier);
        this.identifier = identifier;
    }


    public Cash(String name, String email) {
        super(name, email, generateRandomIdentifier());
        this.identifier = getCashId();
    }


    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "Cash{identifier='" + identifier + "', name='" + name + "', email='" + email + "'}";
    }
}