package upm.Users;

public class Cash extends User {
    private String id;
    public Cash(String name, String email) {
        super(name, email, null);
        this.id = CashManager.generateRandomId();
    }

    public Cash(String id, String name, String email) {
        super(name, email, null);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("Cash{id='%s', name='%s', email='%s'}", id, name, email);
    }
}