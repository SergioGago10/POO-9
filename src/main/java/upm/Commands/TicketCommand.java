package upm.Commands;

import upm.tickets.TicketManager;

/**
 * Clase abstracta base para todos los comandos de tickets.
 * Proporciona la referencia al TicketManager y métodos comunes.
 */
public abstract class TicketCommand extends Command {

    protected final TicketManager ticketManager;

    /**
     * Constructor que recibe el nombre del subcomando y el TicketManager
     *
     * @param text          nombre del comando ("list", "add", "new", etc.)
     * @param ticketManager instancia compartida de TicketManager
     */
    public TicketCommand(String text, TicketManager ticketManager) {
        super(text);
        this.ticketManager = ticketManager;
    }

    /**
     * Cada subcomando implementa su propia lógica.
     *
     * @param args argumentos del comando
     * @return true si el comando se ejecutó, false si no se ejecutó
     */
    @Override
    public abstract boolean apply(String[] args);
}

