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
        System.out.println("Commands:\n" +
                "  client add \"<nombre>\" <DNI> <email> <cashId>\n" +
                "  client remove <DNI>\n" +
                "  client list\n" +
                "  cash add [<id>] \"<nombre>\"<email>\n" +
                "  cash remove <id>\n" +
                "  cash list\n" +
                "  cash tickets <id>\n" +
                "  ticket new [<id>] <cashId> <userId>\n" +
                "  ticket add <ticketId><cashId> <prodId> <amount> [--p<txt> --p<txt>] \n" +
                "  ticket remove <ticketId><cashId> <prodId> \n" +
                "  ticket print <ticketId> <cashId> \n" +
                "  ticket list\n" +
                "  prod add <id> \"<name>\" <category> <price>\n" +
                "  prod update <id> NAME|CATEGORY|PRICE <value>\n" +
                "  prod addFood [<id>] \"<name>\" <price> <expiration:yyyy-MM-dd> <max_people>\n" +
                "  prod addMeeting [<id>] \"<name>\" <price> <expiration:yyyy-MM-dd> <max_people>\n" +
                "  prod list\n" +
                "  prod remove <id>\n" +
                "  help\n" +
                "  echo “<text>” \n" +
                "  exit\n" +
                "\n" +
                "Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS\n" +
                "Discounts if there are ≥2 units in the category: MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%, ELECTRONICS 3%.\n");
    }
}

