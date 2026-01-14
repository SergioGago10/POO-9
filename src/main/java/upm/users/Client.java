package upm.users;

public class Client extends upm.users.User {
    private String cashId;
    private TypeClient typeClient;
    private String dni;

    public Client(String name, String dni, String email, String cashId, TypeClient typeClient) {
        super(name, email, dni); // id del User == dni
        this.dni = dni;
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

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    @Override
    public String getId() {return dni;}
}
