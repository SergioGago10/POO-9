package upm;

public abstract class User {
    public String name;
    public String email;
    public int cashId;

    public User(String name, String email, int cashId){
        this.name = name;
        this.email = email;
        this.cashId = cashId;
    }
    public String getName(){return name;}
    public String getEmail(){return email;}
    public int getCashId(){return cashId;}


}
