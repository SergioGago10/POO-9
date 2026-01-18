package upm.users;

public class Client extends User {

    private String cashId;
    private TypeClient typeClient;
    private String dni;

    public Client(String name, String dni, String email, String cashId, TypeClient typeClient) {
        super(name, email, dni);
        this.dni = dni;
        this.cashId = cashId;
        this.typeClient = typeClient;
    }

    public Client() {
        super();
    }

    @Override
    public String getId() {
        if (dni != null && !dni.isBlank()) {
            return dni;
        }
        return id;
    }

    public void setDni(String dni) {
        this.dni = dni;
        this.id = dni;
    }

    public String getCashId() {
        return cashId;
    }

    public void setCashId(String cashId) {
        this.cashId = cashId;
    }

    public TypeClient getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(TypeClient typeClient) {
        this.typeClient = typeClient;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return String.format("%s{identifier='%s', name=%s, email: %s, cash: %s}",
                typeClient, getId(), name, email, cashId);
    }
}
