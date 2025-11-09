package upm.Users;

import java.util.Random;

public class Cashier extends User {

    public Cashier(String name, String email, String cashId) {
        super(name, email, cashId);
    }

    public static String generarId() {
        Random random = new Random();
        int num = 1000000 + random.nextInt(9000000);
        return "UW" + num;
    }
}