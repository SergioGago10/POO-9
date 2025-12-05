package upm.Users;


public class Cash extends User {

    public Cash(String identifier, String name, String email) {
        super(name, email,identifier);
    }

    @Override
    public String toString() {
        return " Cash{identifier='" + id + "', name='" + name + "', email='" + email + "'}";
    }
}