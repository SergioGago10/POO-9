package upm.Users;

import upm.tickets.Ticket;

import java.util.*;

public final class ClientsManager {

    public static final ArrayList<Client> clientsList = new ArrayList<>();
    private ClientsManager() { }

    public static ArrayList<Client> getClientsList() {
        return new ArrayList<>(clientsList); // devolvemos copia para no exponer la interna
    }

    public static boolean addClient(Client client) {
        if (client == null) return false;
        for (Client c : clientsList) {
            if (c.getDni().equals(client.getDni())) {
                return false;
            }
        }
        return clientsList.add(client);
    }

    public static Client getClientByDni(String dni) {
        if (dni == null) return null;
        for (Client client : clientsList) {
            if (client.getDni().equals(dni)) {
                return client;
            }
        }
        return null;
    }

    public static boolean removeClientByDni(String dni) {
        if (dni == null) return false;

        Iterator<Client> it = clientsList.iterator();
        while (it.hasNext()) {
            Client current = it.next();
            if (current.getDni().equals(dni)) {
                it.remove();
                return true;
            }
        }
        return false;
    }
}

