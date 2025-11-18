package upm.Users;

public class Cash extends User {
    private String id; // UW + 9 dígitos

    // Constructor con ID generado aleatoriamente
    public Cash(String name, String email) {
        super(name, email, null); // cashId es null porque un cajero no tiene cashId
        this.id = CashManager.generateRandomId();
    }

    // Constructor con ID dado
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