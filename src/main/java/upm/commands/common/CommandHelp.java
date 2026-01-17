package upm.commands.common;


import upm.CLI;
import upm.commands.core.Command;

public class CommandHelp extends Command {
    public CommandHelp() {
        super("help");
    }



    @Override
    public boolean apply(String[] args) {
        if (args == null || args.length != 1) {
            CLI.printErrorNextLine("Error -> Format must be: help");
            return true;
        }

        printHelp();
        return true;
    }

    private void printHelp() {
        CLI.print("""
                Commands:
                  client add "<nombre>" (<DNI>|<NIF>) <email> <cashId>
                  client remove <DNI>
                  client list
                  cash add [<id>] "<nombre>"<email>
                  cash remove <id>
                  cash list
                  cash tickets <id>
                  ticket new [<id>] <cashId> <userId> -[c|p|s] (default -p option)
                  ticket add <ticketId><cashId> <prodId> <amount> [--p<txt> --p<txt>]\s
                  ticket remove <ticketId><cashId> <prodId>\s
                  ticket print <ticketId> <cashId>\s
                  ticket list
                  prod add ([<id>] "<name>" <category> <price> [<maxPers>] || ("<name>" <category> )
                  prod update <id> NAME|CATEGORY|PRICE <value>
                  prod addFood [<id>] "<name>" <price> <expiration:yyyy-MM-dd> <max_people>
                  prod addMeeting [<id>] "<name>" <price> <expiration:yyyy-MM-dd> <max_people>
                  prod list
                  prod remove <id>
                  help
                  echo “<text>”\s
                  exit
                
                Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS
                Discounts if there are ≥2 units in the category: MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%, ELECTRONICS 3%.
                """);
    }
}

