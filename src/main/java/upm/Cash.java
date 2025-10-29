package upm;
import java.util.*;
public class Cash {

    private String id;
    private String name;

    private String email;



    public Cash(String id, String name, String email){
        if (id == null) {
            throw new IllegalArgumentException("invalid id");
        }

        if (name == null || name.isBlank() ){
            throw new IllegalArgumentException("Invalid name.");
        }

        if (email == null ) {
            throw new IllegalArgumentException("invalid email");
        }
        this.id = id;
        this.name = name.replace("\"", ""); //Quitamos comillas para que en la comparacion por nombre alfabetico no de error
        this.email = email;
    }

    public String getid(){
        return id;
    }
    public String getName() {

        return name;
    }
    public String getemail() {

        return email;
    }
    public static String generarId() {
        Random random = new Random();
        int num = 1000000 + random.nextInt(9000000);
        return "UW" + num;
    }



    }



