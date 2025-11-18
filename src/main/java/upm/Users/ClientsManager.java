package upm.Users;
import upm.Commands.Command;
import upm.Users.Client;
import upm.Utilities;

import java.util.*;

public class ClientsManager {
    public static ArrayList<Client> clientsList = new ArrayList();

    public ClientsManager() {
        clientsList = new ArrayList<>();
    }

    public ArrayList<Client> getClients() {
        return clientsList;
    }

    public static boolean addClient(Client client) {
        boolean added = false;
        if (client == null) return added;
        ListIterator<Client> it = clientsList.listIterator();
        while (it.hasNext()) {
            String currentDni = it.next().getDni();
            if (currentDni.equals(client.getDni())) {
                return added;
            }
        }
        return ClientsManager.clientsList.add(client);
    }

    public static boolean removeClient(Client client) {
        Iterator<Client> it = clientsList.iterator();
        boolean removed = false;
        while (it.hasNext() && !removed) {
            String currentDni = it.next().getDni();
            if (currentDni.equals(client.getDni())) {
                it.remove();
                removed = true;
            }
        }
        return removed;
    }

    public static StringBuilder showClients(ArrayList<Client> clientsList) {
        StringBuilder sb = new StringBuilder();
        if (clientsList.isEmpty()) {
            sb.append("No hay clientes registrados en el sistema.");
            return sb;
        }
        Collections.sort(clientsList, Comparator.comparing(Client::getName, String.CASE_INSENSITIVE_ORDER));
        sb.append("Lista de clientes:\n");
        for (Client client : clientsList) {
            sb.append(client.toString());
        }
        return sb;
    }

}
