package upm.Users;

public class Client extends User {
    private String cashId;
    private TypeClient typeClient;

    public Client(String name, String dni, String email, String cashId, TypeClient typeClient) {
        super(name, email, dni);
        this.cashId = cashId;
        this.typeClient = typeClient;
    }

    public Client(){}

    public String toString() {
        return String.format("%s{identifier='%s', name=%s, email: %s, cash: %s}", typeClient, id, name, email, cashId);
    }

    public TypeClient getTypeClient() {
        return typeClient;
    }

    public void setType(TypeClient type) {
        this.typeClient = type;
    }

    public String getCashId() {
        return cashId;
    }

    public void setCashId(String cashId) {
        this.cashId = cashId;
    }
}
