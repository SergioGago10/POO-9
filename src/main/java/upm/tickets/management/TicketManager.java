package upm.tickets.management;

import com.fasterxml.jackson.annotation.JsonIgnore;
import upm.Utilities;
import upm.tickets.core.Ticket;
import upm.tickets.core.TicketFactory;
import upm.users.Cash;
import upm.users.Client;
import upm.users.User;
import upm.users.UserManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TicketManager {
    private final List<Ticket<?>> ticketsList;
    private static TicketManager instance;

    private TicketManager() {
        ticketsList = new ArrayList<>();
    }

    @JsonIgnore
    public List<Ticket<?>> getTicketsList() {
        return new ArrayList<>(ticketsList);
    }

    public static TicketManager getInstance() {
        if (instance == null) instance = new TicketManager();
        return instance;
    }

    public void setTicketsList(List<Ticket<?>> tickets) {
        ticketsList.clear();
        if (tickets == null) return;

        Map<String, Ticket<?>> byId = new LinkedHashMap<>();
        for (Ticket<?> t : tickets) {
            String id = safeTicketId(t);
            if (id == null || id.isBlank()) continue;
            byId.putIfAbsent(id, t);
        }
        ticketsList.addAll(byId.values());
    }

    public boolean exists(String ticketId) {
        if (ticketId == null) return false;
        return ticketsList.stream().anyMatch(t -> ticketId.equals(safeTicketId(t)));
    }

    public Ticket<?> getTicketById(String ticketId) {
        if (ticketId == null) return null;

        Ticket<?> exact = ticketsList.stream()
                .filter(t -> ticketId.equals(safeTicketId(t)))
                .findFirst()
                .orElse(null);

        if (exact != null) return exact;

        List<Ticket<?>> matches = ticketsList.stream()
                .filter(t -> {
                    String id = safeTicketId(t);
                    return id != null && (id.equals(ticketId) || id.endsWith(ticketId) || id.contains(ticketId));
                })
                .collect(Collectors.toList());

        if (matches.isEmpty()) return null;
        if (matches.size() == 1) return matches.get(0);

        matches.sort(Comparator.comparingInt(t -> safeTicketId(t).length()));
        return matches.get(0);
    }

    public Ticket<?> newTicket(String option) {
        String random = String.valueOf(Utilities.randomNumGen(5));
        String ticketId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm")) + random;
        while (exists(ticketId)) {
            random = String.valueOf(Utilities.randomNumGen(5));
            ticketId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm")) + random;
        }
        return newTicket(ticketId, option);
    }

    public Ticket<?> newTicket(String ticketId, String option) {
        if (ticketId == null || ticketId.isBlank()) {
            throw new IllegalArgumentException("ticketId cannot be null/blank");
        }
        if (exists(ticketId)) {
            throw new IllegalArgumentException("Ticket with id " + ticketId + " already exists");
        }
        Ticket<?> ticket = TicketFactory.create(ticketId, option);
        ticketsList.add(ticket);
        return ticket;
    }

    public void ticketCashRemover(Cash cashier) {
        if (cashier == null) return;

        UserManager clientSearch = UserManager.getInstance();
        List<Ticket<?>> cashTickets = (List) cashier.getTickets();

        for (Ticket<?> currentTicket : new ArrayList<>(cashTickets)) {
            String id = safeTicketId(currentTicket);
            if (id != null) {
                ticketsList.removeIf(t -> id.equals(safeTicketId(t)));
            } else {
                ticketsList.remove(currentTicket);
            }

            Iterator<Client> clientIt = clientSearch.getClients().iterator();
            boolean found = false;
            while (!found && clientIt.hasNext()) {
                User user = clientIt.next();
                found = user.removeTicket(currentTicket);
            }
        }
    }

    private String safeTicketId(Ticket<?> t) {
        if (t == null) return null;
        if (t.getTicketMetadata() == null) return null;
        return t.getTicketMetadata().getTicketID();
    }
}
