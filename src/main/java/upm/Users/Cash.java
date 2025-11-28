package upm.Users;

import static upm.Users.CashManager.generateRandomId;

public class Cash extends User {
    private String id;

    public Cash(String id, String name, String email) {
        super(name, email, id);
        this.id = id;
    }


    public Cash(String name, String email) {
        super(name, email, generateRandomId());
        this.id = getCashId();
    }


    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("- %s | Email: %s | CashierId: %s", name, email, cashId);
    }
}