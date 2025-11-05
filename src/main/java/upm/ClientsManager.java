package upm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ClientsManager extends Client{
    public static List<Client> clientsList = new ArrayList();

    public ClientsManager(String name, String dni, String email, int cashId) {
        super(name, dni, email, cashId);
    }

    public static boolean addClient (Client client){
        ListIterator<Client> it = clientsList.listIterator();
        while (it.hasNext()){
            Client current = it.next();
            if (current.equals(client)){
                return false;
            }
        }
        it.add(client);
        return true;
    }

    public static boolean removeClient (Client client){
        Iterator<Client> it = clientsList.iterator();
        boolean found = false;
        while (it.hasNext() && !found){
            Client current = it.next();
            if (current.equals(client)){
                found=true;
            }
        }
        return found;
    }
    public static String showClients(){

    }
}
