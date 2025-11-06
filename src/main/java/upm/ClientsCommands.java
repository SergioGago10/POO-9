package upm;
import java.util.*;

public class ClientsCommands {
    public static List<Client> clientsList = new ArrayList();

    public static boolean addClient (Client client){
        boolean added = false;
        if (client==null) return added;
        ListIterator<Client> it = clientsList.listIterator();
        while (it.hasNext()){
            String currentDni = it.next().getDni();
            if (currentDni.equals(client.getDni())){
                return added;
            }
        }
        return clientsList.add(client);
    }

    public static boolean removeClient (Client client){
        Iterator<Client> it = clientsList.iterator();
        boolean removed = false;
        while (it.hasNext() && !removed){
            String currentDni = it.next().getDni();
            if (currentDni.equals(client.getDni())){
                it.remove();
                Utilities.arrayShifterToLeft(clientsList);
                removed =true;
            }
        }
        return removed;
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
