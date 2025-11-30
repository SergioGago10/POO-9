package upm.Commands;

import upm.tickets.TicketManager;


public abstract class TicketCommand extends Command {

    protected final TicketManager ticketManager;

    public TicketCommand(String text, TicketManager ticketManager) {
        super(text);
        this.ticketManager = ticketManager;
    }

    @Override
    public abstract boolean apply(String[] args);
}

