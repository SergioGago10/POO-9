package upm.Commands;



public class CommandHelp extends Command {
    public CommandHelp() {
        super("help");
    }



    @Override
    public boolean apply(String[] args) {
        if (args == null || args.length == 0) {
            return false;
        }

        //  "help" sin más argumentos
        if (args.length != 1 || !args[0].equalsIgnoreCase(text)) {
            return false;
        }

        printHelp();
        return true;
    }

    private void printHelp() {
        System.out.println("Comandos disponibles:\n");

        System.out.println("CLIENTES / CAJEROS");
        System.out.println("  client add \"<nombre>\" <DNI> <email> <cashId>");
        System.out.println("      -> Crea un nuevo cliente asociado al cajero <cashId>.");
        System.out.println("  client remove <DNI>");
        System.out.println("      -> Elimina el cliente con ese DNI.");
        System.out.println("  client list");
        System.out.println("      -> Lista todos los clientes (ordenados por nombre).");
        System.out.println("  cash add [<id>] \"<nombre>\" <email>");
        System.out.println("      -> Crea un nuevo cajero (si no hay <id>, se genera).");
        System.out.println("  cash remove <id>");
        System.out.println("      -> Elimina el cajero con ese id.");
        System.out.println("  cash list");
        System.out.println("      -> Lista todos los cajeros (ordenados por nombre).");
        System.out.println("  cash tickets <id>");
        System.out.println("      -> Muestra los tickets gestionados por ese cajero.\n");

        System.out.println("TICKETS");
        System.out.println("  ticket new [<id>] <cashId> <userId>");
        System.out.println("      -> Crea un ticket para el usuario <userId> atendido por <cashId>.");
        System.out.println("  ticket add <ticketId> <cashId> <prodId> <amount> [--p<txt> ...]");
        System.out.println("      -> Añade un producto al ticket (con posibles textos de personalización).");
        System.out.println("  ticket remove <ticketId> <cashId> <prodId>");
        System.out.println("      -> Elimina un producto del ticket.");
        System.out.println("  ticket print <ticketId> <cashId>");
        System.out.println("      -> Imprime y cierra el ticket (cerrado solo se reimprime).");
        System.out.println("  ticket list");
        System.out.println("      -> Lista todos los tickets.\n");

        System.out.println("PRODUCTOS");
        System.out.println("  prod add [<id>] \"<name>\" <category> <price> [<maxPers>]");
        System.out.println("      -> Crea un producto normal o personalizable (si tiene <maxPers>).");
        System.out.println("  prod update <id> NAME|CATEGORY|PRICE <value>");
        System.out.println("      -> Modifica nombre, categoría o precio de un producto.");
        System.out.println("  prod addFood [<id>] \"<name>\" <price> <expiration: yyyy-MM-dd> <max_people>");
        System.out.println("      -> Crea un producto de tipo comida (con caducidad y plazas).");
        System.out.println("  prod addMeeting [<id>] \"<name>\" <price> <expiration: yyyy-MM-dd> <max_people>");
        System.out.println("      -> Crea un producto de tipo reunión (con caducidad y plazas).");
        System.out.println("  prod list");
        System.out.println("      -> Lista todos los productos.");
        System.out.println("  prod remove <id>");
        System.out.println("      -> Elimina el producto con ese id.\n");

        System.out.println("GENERALES");
        System.out.println("  help");
        System.out.println("      -> Muestra esta ayuda.");
        System.out.println("  echo \"<text>\"");
        System.out.println("      -> Imprime <text> tal cual.");
        System.out.println("  exit");
        System.out.println("      -> Cierra la aplicación.\n");
    }
}

