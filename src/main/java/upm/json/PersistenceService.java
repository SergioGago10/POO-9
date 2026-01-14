package upm.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import upm.products.*;
import upm.tickets.management.TicketManager;
import upm.users.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public final class PersistenceService {

    private static final File FILE = new File("system.json");

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private PersistenceService() {}

    // =========================
    // MAIN DE PRUEBAS
    // =========================
    public static void main(String[] args) {

        // 1) Crear datos y guardar
        pruebasSave();

        // 2) Borrar memoria (simula reinicio)
        clearManagersForTest();

        // 3) Cargar desde system.json
        pruebasLoad();
    }

    // =========================
    // PRUEBA GUARDAR
    // =========================
    public static void pruebasSave() {
        UserManager userManager = UserManager.getInstance();
        ProductManager productManager = ProductManager.getInstance();
        TicketManager ticketManager = TicketManager.getInstance();

        Cash cash = new Cash("00000W", "Saul", "saul@gmail.com");
        userManager.addCash(cash);

        Client client = new Client("Saul", "00000000N", "saul@gmail.com", "00000W", TypeClient.CLIENT);
        Client client2 = new Client("Jesus", "00000011H", "jesus@gmail.com", "00000W", TypeClient.COMPANY);
        userManager.addClient(client);
        userManager.addClient(client2);

        Product product1 = new BasicProduct("1", "Troteras y danceras", Category.BOOK, 12);
        Product product2 = new BasicProduct("2", "Chaqueta", Category.CLOTHES, 20);
        productManager.addProduct(product1);
        productManager.addProduct(product2);

        ProductService service1 = new ProductService("1", ServiceCategory.TRANSPORT, LocalDateTime.now());
        ProductService service2 = new ProductService("2", ServiceCategory.INSURANCE, LocalDateTime.now());
        productManager.addService(service1);
        productManager.addService(service2);

        save();
    }

    // =========================
    // PRUEBA CARGAR
    // =========================
    public static void pruebasLoad() {
        UserManager userManager = UserManager.getInstance();
        ProductManager productManager = ProductManager.getInstance();
        TicketManager ticketManager = TicketManager.getInstance();

        load();

        System.out.println("=== DATOS CARGADOS ===");
        System.out.println("Clients: " + userManager.getClients().size());
        System.out.println("Cash: " + userManager.getCash().size());
        System.out.println("Products: " + productManager.getCatalogProducts().size());
        System.out.println("Services: " + productManager.getCatalogServices().size());
        System.out.println("Tickets: " + ticketManager.getTicketsList().size());

        for (Product p : productManager.getCatalogProducts()) {
            System.out.println(p);
        }
    }

    // =========================
    // SAVE / LOAD
    // =========================
    public static void save() {
        try {
            SystemState state = buildStateFromManagers();
            System.out.println("Saving to: " + FILE.getAbsolutePath());
            mapper.writerWithDefaultPrettyPrinter().writeValue(FILE, state);
        } catch (IOException e) {
            throw new RuntimeException("Error saving system state", e);
        }
    }

    public static void load() {
        if (!FILE.exists()) {
            System.out.println("No system.json found, skipping load.");
            return;
        }

        try {
            SystemState state = mapper.readValue(FILE, SystemState.class);
            loadStateIntoManagers(state);
        } catch (IOException e) {
            throw new RuntimeException("Error loading system state", e);
        }
    }

    // =========================
    // HELPERS
    // =========================
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
        tm.setTicketsList(state.getTickets());
    }

    private static void clearManagersForTest() {
        ProductManager.getInstance().getCatalogProducts().clear();
        ProductManager.getInstance().getCatalogServices().clear();
        UserManager.getInstance().getClients().clear();
        UserManager.getInstance().getCash().clear();
        TicketManager.getInstance().setTicketsList(new java.util.ArrayList<>());
    }
}
