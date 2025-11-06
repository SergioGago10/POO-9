package upm;

import java.lang.classfile.ClassElement;

public class Client extends User{
    public String dni;
    public Client(String name, String email, int cashId){
        super(name,email,cashId);
        this.dni=dni;
    }
    public String getDni(){
        return dni;
    }
    public String toString() {
        return String.format("- %s | DNI: %s | Email: %s | CashierId: %s", name, dni, email, cashId);
    }
}
