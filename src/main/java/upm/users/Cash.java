package upm.users;

public class Cash extends User {

    public Cash(String identifier, String name, String email) {
        super(name, email, identifier); // id del User
    }

    public Cash() {
        super();
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {return name;}

    @Override
    public String toString() {
        return "Cash{" +
                "identifier='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
