package upm;

public abstract class User {
    public String name;
    public String dni;
    public String email;
    public int cashId;

    public User(String name, String dni, String email, int cashId){
        this.name = name;
        this.dni = dni;
        this.email = email;
        this.cashId = cashId;
    }
    public String getName(){return name;}
    public String getDni(){return dni;}
    public String getEmail(){return email;}
    public int getCashId(){return cashId;}



    public void addClient(int dni){}
    public void removeClient(int dni){}
}
