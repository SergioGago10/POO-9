package upm.Users;

import java.util.ArrayList;
import java.util.Iterator;

public class UserManager {
    public static ArrayList<User> userList;

    private UserManager() {
        userList=new ArrayList<>();
    }

    public static ArrayList<User> getUserList() {
        return new ArrayList<>(userList); // devolvemos copia para no exponer la interna
    }

    public static boolean addUser(User user) {
        if (user == null) return false;
        for (User u : userList) {
            if (u.getId().equals(user.getId())) {
                return false;
            }
        }
        return userList.add(user);
    }

    public static User getClientByID(String id) {
        if (id == null) return null;
        for (User user : userList) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public static boolean removeClientByDni(String dni) {
        if (dni == null) return false;

        Iterator<User> it = userList.iterator();
        while (it.hasNext()) {
            User current = it.next();
            if (current.getId().equals(dni)) {
                it.remove();
                return true;
            }
        }
        return false;
    }
    public static boolean idExists(String dni) {
        return getClientByID(dni) != null;
    }
}
