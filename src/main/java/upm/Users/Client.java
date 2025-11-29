package upm.Users;

public class Client extends User {
    private String dni;
    public Client(String name, String dni, String email, String cashId){
        super(name,email,cashId);
        this.dni=dni;
    }
    public String getDni(){
        return dni;
    }
    public String toString() {
        return String.format(" Client{identifier='%s', name=%s, email: %s, cash: %s", dni, name, email, cashId);
    }
}
