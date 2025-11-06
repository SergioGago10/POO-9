package upm;

import java.lang.classfile.ClassElement;

public class Client extends User{

    public Client(String name, String dni, String email, int cashId){
        super(name,dni,email,cashId);
    }
    public String toString() {
        return String.format("- %s | DNI: %s | Email: %s | CashierId: %s", name, dni, email, cashId);
    }
}
