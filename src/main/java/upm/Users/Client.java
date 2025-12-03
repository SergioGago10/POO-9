package upm.Users;

public class Client extends User {
    private String dni;
    private String cashId;
    public Client(String name, String dni, String email, String cashId){
        super(name,email,dni);
        this.cashId=cashId;
        this.dni=dni;
    }
    public String toString() {
        return String.format(" Client{identifier='%s', name=%s, email: %s, cash: %s}", dni, name, email, cashId);
    }
}
