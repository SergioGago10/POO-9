package upm;

import java.util.*;

public class ClientsManager {
    public static List<Client> clientsList = new ArrayList();

    public static boolean addClient (Client client){
        if (client==null) return false;
        ListIterator<Client> it = clientsList.listIterator();
        while (it.hasNext()){
            String currentDni = it.next().getDni();
            if (currentDni.equals(client.getDni())){
                return false;
            }
        }
        return clientsList.add(client);
    }

    public static boolean removeClient (Client client){
        Iterator<Client> it = clientsList.iterator();
        boolean found = false;
        while (it.hasNext() && !found){
            String currentDni = it.next().getDni();
            if (currentDni.equals(client.getDni())){
                it.remove();
                Utilities.arrayShifterToLeft(client);
                found=true;
            }
        }
        return found;
    }
    public static StringBuilder showClients(){
        StringBuilder sb = new StringBuilder();
        if (clientsList.isEmpty()){
            sb.append("No hay clientes registrados en el sistema.");
            return sb;
        }
        Collections.sort(clientsList,Comparator.comparing(Client::getName,String.CASE_INSENSITIVE_ORDER));
        sb.append("Lista de clientes:\n" );
        for (Client client : clientsList){
            sb.append(client.toString());
        }
        return sb;
    }
}
