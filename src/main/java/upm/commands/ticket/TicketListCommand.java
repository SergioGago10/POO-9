package upm.commands.ticket;

import upm.CLI;
import upm.commands.core.Command;
import upm.tickets.management.TicketManager;

public class TicketListCommand extends Command {

    public TicketListCommand(){
        super("list");
    }

    @Override
    public boolean apply(String[] args) {
        try{
            if(args.length!=2){
                CLI.printErrorNextLine("Error -> format must be: ticket list");
            }else{
                TicketManager ticketManager=TicketManager.getInstance();
                ticketManager.getFormatter().printListTickets(ticketManager);
                CLI.printNextLine("ticket list: ok");
            }
        } catch (Exception e) {
            CLI.printErrorNextLine("Error -> tickets could not be printed: " + e.getMessage());
        }
        return true;
    }
}
