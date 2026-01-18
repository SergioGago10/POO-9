package upm.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import upm.CLI;
import upm.products.Product;
import upm.products.ProductManager;
import upm.products.ProductService;
import upm.tickets.core.Ticket;
import upm.tickets.management.TicketManager;
import upm.users.Cash;
import upm.users.Client;
import upm.users.UserManager;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public final class PersistenceService {

    private static final File FILE = new File("system.json");
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public PersistenceService() {}

    public static void main(String[] args){
        pruebasLoad();
        //pruebasSave();
    }

    public static void pruebasSave(){
        UserManager userManager = UserManager.getInstance();
        ProductManager productManager = ProductManager.getInstance();
        TicketManager tm= TicketManager.getInstance();
        Cash cash=new Cash("00000W","Saul", "saul@gmail.com");
        userManager.addCash(cash);
        Client client= new Client("Saul","00000000N","saul@gmail.com","00000W", upm.users.TypeClient.CLIENT);
        Client client2= new Client("jesus","000000011","jesus@gmail.com","00000W", upm.users.TypeClient.COMPANY);
        userManager.addClient(client);
        userManager.addClient(client2);

        Product product = new upm.products.BasicProduct("1", "Troteras y danceras", upm.products.Category.BOOK, 12);
        Product product2 = new upm.products.BasicProduct("2", "Chaqueta", upm.products.Category.CLOTHES, 20);
        productManager.addProduct(product);
        productManager.addProduct(product2);
        ProductService service = new ProductService("1", upm.products.ServiceCategory.TRANSPORT, LocalDateTime.now());
        ProductService service2 = new ProductService("2", upm.products.ServiceCategory.INSURANCE, LocalDateTime.now());
        productManager.addService(service);
        productManager.addService(service2);
        save();
    }

    public static void pruebasLoad(){
        UserManager userManager = UserManager.getInstance();
        ProductManager productManager = ProductManager.getInstance();
        TicketManager tm=TicketManager.getInstance();
        load();
        for(Client client : userManager.getClients()){
            CLI.printNextLine(client.toString());
            CLI.printNextLine(client.getTickets().toString());
        }
        for(Cash cash : userManager.getCash()){
            CLI.printNextLine(cash.toString());
            CLI.printNextLine(cash.getTickets().toString());
        }
        for(Product product : productManager.getCatalogProducts()){
            CLI.printNextLine(product.toString());
        }
    }

    public static void save() {
        try {
            SystemState state = buildStateFromManagers();
            mapper.writerWithDefaultPrettyPrinter().writeValue(FILE, state);
        } catch (IOException e) {
            throw new RuntimeException("Error saving system state", e);
        }
    }

    public static void load() {
        if (!FILE.exists()) return;

        try {
            SystemState state = mapper.readValue(FILE, SystemState.class);
            loadStateIntoManagers(state);
        } catch (IOException e) {
            CLI.printErrorNextLine("Error -> JSON not found or with unreadable format");
        }
    }

    public static void reset(boolean deleteFile) {
        if (deleteFile && FILE.exists()) {
            try { FILE.delete(); } catch (Exception ignored) {}
        }
        ProductManager pm = ProductManager.getInstance();
        pm.setCatalogProducts(new ArrayList<>());
        pm.setCatalogServices(new ArrayList<>());

        UserManager um = UserManager.getInstance();
        um.setClientsList(new ArrayList<>());
        um.setCashList(new ArrayList<>());

        TicketManager tm = TicketManager.getInstance();
        tm.setTicketsList(new ArrayList<>());

        save();
    }

    private static SystemState buildStateFromManagers() {
        SystemState state = new SystemState();
        state.setProducts(ProductManager.getInstance().getCatalogProducts());
        state.setServices(ProductManager.getInstance().getCatalogServices());
        state.setClients(UserManager.getInstance().getClients());
        state.setCash(UserManager.getInstance().getCash());
        state.setTickets(TicketManager.getInstance().getTicketsList());
        return state;
    }

    private static void loadStateIntoManagers(SystemState state) {
        ProductManager pm = ProductManager.getInstance();
        pm.setCatalogProducts(state.getProducts());
        pm.setCatalogServices(state.getServices());

        UserManager um = UserManager.getInstance();
        um.setClientsList(state.getClients());
        um.setCashList(state.getCash());

        TicketManager tm = TicketManager.getInstance();

        List<Ticket<?>> allTickets = new ArrayList<>();
        if (state.getTickets() != null) allTickets.addAll(state.getTickets());

        if (state.getCash() != null) {
            for (Cash c : state.getCash()) {
                if (c != null && c.getTickets() != null) allTickets.addAll((List) c.getTickets());
            }
        }
        if (state.getClients() != null) {
            for (Client cl : state.getClients()) {
                if (cl != null && cl.getTickets() != null) allTickets.addAll((List) cl.getTickets());
            }
        }

        Map<String, Ticket<?>> canonicalById = new LinkedHashMap<>();
        for (Ticket<?> t : allTickets) {
            String id = safeTicketId(t);
            if (id == null || id.isBlank()) continue;
            canonicalById.putIfAbsent(id, t);
        }

        tm.setTicketsList(new ArrayList<>(canonicalById.values()));

        if (state.getCash() != null) {
            for (Cash c : state.getCash()) {
                canonicalizeUserTickets(c, canonicalById);
            }
        }
        if (state.getClients() != null) {
            for (Client cl : state.getClients()) {
                canonicalizeUserTickets(cl, canonicalById);
            }
        }
    }

    private static void canonicalizeUserTickets(upm.users.User u, Map<String, Ticket<?>> canonicalById) {
        if (u == null) return;
        List list = u.getTickets();
        if (list == null || list.isEmpty()) return;

        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            if (!(obj instanceof Ticket<?> t)) continue;
            String id = safeTicketId(t);
            if (id == null) continue;
            Ticket<?> canonical = canonicalById.get(id);
            if (canonical != null && canonical != t) {
                try { list.set(i, canonical); } catch (Exception ignored) {}
            }
        }
    }

    private static String safeTicketId(Ticket<?> t) {
        if (t == null) return null;
        if (t.getTicketMetadata() == null) return null;
        return t.getTicketMetadata().getTicketID();
    }
}
