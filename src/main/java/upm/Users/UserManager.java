package upm.Users;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class UserManager {
    private static UserManager instance;
    private static ArrayList<Client> clientsList;
    private static ArrayList<Cash> cashList;

    private UserManager(ArrayList<Client> clientsList, ArrayList<Cash> cashList) {
        if (clientsList == null)
            UserManager.clientsList = new ArrayList<>();
        if (cashList == null)
            UserManager.cashList = new ArrayList<>();
    }

    public static UserManager getInstance(){
        if (instance==null){
            instance=new UserManager(clientsList,cashList);
        }
        return instance;
    }

    public static ArrayList<Client> getClients() {
        return new ArrayList<>(clientsList); // devolvemos copia para no exponer la interna
    }

    public static ArrayList<Cash> getCash() {
        return new ArrayList<>(cashList); // devolvemos copia para no exponer la interna
    }

    public boolean addClient(Client client) {
        if (client == null) return false;
        for (Client c : clientsList) {
            if (c.getId().equals(client.getId())) {
                return false;
            }
        }
        return clientsList.add(client);
    }

    public boolean addCash(Cash cash) {
        if (cash == null) {
            System.out.println("Es nulo");
            return false;};
        for (Cash c : cashList) {
            if (c.getId().equals(cash.getId())) {
                System.out.println("existe ya");
                return false;
            }
        }
        return cashList.add(cash);
    }

    public User getUserByID(String id) {
        if (id == null) return null;
        for (User user : clientsList) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        for(User user : cashList)
            if (user.getId().equals(id)) {
                return user;
            }
        return null;
    }

    public static boolean removeUserByDni(String dni) {
        if (dni == null) return false;

        if (dni.startsWith("UW")) {
            Iterator<Cash> it = cashList.iterator();
            while (it.hasNext()) {
                Cash current = it.next();
                if (current.getId().equals(dni)) {
                    it.remove();
                    return true;
                }
            }
            return false;
        } else {
            Iterator<Client> it = clientsList.iterator();
            while (it.hasNext()) {
                Client current = it.next();
                if (current.getId().equals(dni)) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }


    }

    public boolean idExists(String dni) {
        return getUserByID(dni) != null;
    }

    public String generateRandomIdentifier() {
        Random random = new Random();
        int number;
        do {
            number = 1_000_000 + random.nextInt(9_000_000);
        } while (idExists("UW" + number));
        return "UW" + number;
    }
}
