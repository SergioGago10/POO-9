package upm.Commands;

import upm.Users.Client;
import upm.Users.ClientsManager;

public class ClientAddCommand extends Command {
    public ClientAddCommand(){
        super("add");
    }
    @Override
    public boolean apply(String[] args) {
        boolean applied;

        if (args.length < 3) {
            applied = false;
        } else {
            String dni = args[2];
            Client client = ClientsManager.getClientByDni(dni);

            if (client != null) {
                System.out.println(client.toString());
                ClientsManager.removeClientByDni(dni);
                System.out.println("client remove: ok");
                applied = true;
            } else {
                System.err.println("The client with DNI: " + dni + " couldn't be removed. Client not found.");
                applied = false;
            }
        }

        return applied;
    }


}
