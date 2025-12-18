package upm.Users;

public class Client extends User {
    private String dni;
    private String cashId;
    private TypeClient type;
    public Client(String name, String dni, String email, String cashId, TypeClient type){
        super(name,email,dni);
        this.cashId=cashId;
        this.dni=dni;
        this.type=type;
    }
    public String toString() {
        return String.format("%s{identifier='%s', name=%s, email: %s, cash: %s}", type,dni, name, email, cashId);
    }
}
