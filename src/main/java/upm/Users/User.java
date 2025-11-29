package upm.Users;

public abstract class User {
    protected String name;
    protected String email;
    protected String cashId;

    public User(String name, String email, String cashId){
        this.name = name;
        this.email = email;
        this.cashId = cashId;
    }
    public String getName(){return name;}
    public String getEmail(){return email;}
    public String getCashId(){
        return cashId;
    }


}