package upm.users;

 import upm.CLI;
 import upm.Utilities;
 import upm.tickets.management.TicketManager;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserManager {
    private static UserManager instance;
    private final ArrayList<Client> clientsList;
    private final ArrayList<Cash> cashList;

    private UserManager() {
        this.clientsList = new ArrayList<>();
        this.cashList = new ArrayList<>();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    @JsonIgnore
    public ArrayList<Client> getClients() {
        return new ArrayList<>(clientsList); // devolvemos copia para no exponer la interna
    }

    @JsonIgnore
    public ArrayList<Cash> getCash() {
        return new ArrayList<>(cashList); // devolvemos copia para no exponer la interna
    }


    public boolean addClient(Client client) {
        if (client == null) return false;
        for (Client c : clientsList) {
            if (c.getId().equals(client.getId())) {
                CLI.printErrorNextLine("Error -> The client with id: " + client.getId() +  " is already in the clients list.");
                return false;
            }
        }
        return clientsList.add(client);
    }

    public boolean addCash(Cash cash) {
        if (cash == null) {
            CLI.printNextLine("Error -> Cashier couldn't be created.");
            return false;
        }
        for (Cash c : cashList) {
            if (c.getId().equals(cash.getId())) {
                CLI.printNextLine("Error -> The cashier already exists");
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
        for (User user : cashList)
            if (user.getId().equals(id)) {
                return user;
            }
        return null;
    }

    public boolean removeUserByDni(String dni) {
        if (dni == null) return false;

        if (dni.startsWith("UW")) {
            Iterator<Cash> it = cashList.iterator();
            while (it.hasNext()) {
                Cash current = it.next();
                if (current.getId().equals(dni)) {
                    //borramos los tickets del cashier una vez lo hemos borrado.
                    TicketManager tm = TicketManager.getInstance();
                    tm.ticketCashRemover(current);
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
        int number;
        do {
            number = Utilities.randomNumGen(7);
        } while (idExists("UW" + number));
        return "UW" + number;
    }

    public void setClientsList(List<Client> clientsList){
        this.clientsList.clear();
        if (clientsList != null)
            this.clientsList.addAll(clientsList);
    }

    public void setCashList(List<Cash> cashList){
        this.cashList.clear();
        if (cashList != null)
            this.cashList.addAll(cashList);
    }
}
