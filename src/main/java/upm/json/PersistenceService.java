package upm.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import upm.CLI;
import upm.products.*;
import upm.tickets.management.TicketManager;
import upm.users.*;

import java.io.IOException;
import java.io.File;
import java.time.LocalDateTime;

public final class PersistenceService {

    private static final File FILE = new File("system.json");
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public PersistenceService() {}

    public static void main(String[] args){
        pruebasLoad();


        //Prueba añadir y guardar
        //pruebasSave();
    }


    public static void pruebasSave(){
        UserManager userManager = UserManager.getInstance();
        ProductManager productManager = ProductManager.getInstance();
        TicketManager tm= TicketManager.getInstance();
        Cash cash=new Cash("00000W","Saul", "saul@gmail.com");
        userManager.addCash(cash);
        Client client= new Client("Saul","00000000N","saul@gmail.com","00000W", TypeClient.CLIENT);
        Client client2= new Client("jesus","000000011","jesus@gmail.com","00000W", TypeClient.COMPANY);
        userManager.addClient(client);
        userManager.addClient(client2);

        Product product = new BasicProduct("1", "Troteras y danceras", Category.BOOK, 12);
        Product product2 = new BasicProduct("2", "Chaqueta", Category.CLOTHES, 20);
        productManager.addProduct(product);
        productManager.addProduct(product2);
        ProductService service = new ProductService("1", ServiceCategory.TRANSPORT, LocalDateTime.now());
        ProductService service2 = new ProductService("2", ServiceCategory.INSURANCE, LocalDateTime.now());
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

    // Guardar todo
    public static void save() {
        try {
            SystemState state = buildStateFromManagers();
            mapper.writerWithDefaultPrettyPrinter().writeValue(FILE, state);
        } catch (IOException e) {
            throw new RuntimeException("Error saving system state", e);
        }
    }

    // Cargar todo
    public static void load() {
        if (!FILE.exists()) return;

        try {
            SystemState state = mapper.readValue(FILE, SystemState.class);

            // Restaurar managers
            loadStateIntoManagers(state);

        } catch (IOException e) {
            CLI.printErrorNextLine("Error -> JSON not found or with unreadable format");
        }
    }

    // ---------------- helpers ----------------

    private static SystemState buildStateFromManagers() {
        SystemState state = new SystemState();
        state.setProducts(ProductManager.getInstance().getCatalogProducts());
        state.setServices(ProductManager.getInstance().getCatalogServices());
        state.setClients(UserManager.getInstance().getClients());
        state.setCash(UserManager.getInstance().getCash());
        return state;
    }

    private static void loadStateIntoManagers(SystemState state) {
        ProductManager pm = ProductManager.getInstance();
        pm.setCatalogProducts(state.getProducts());
        pm.setCatalogServices(state.getServices());

        UserManager um = UserManager.getInstance();
        um.setClientsList(state.getClients());
        um.setCashList(state.getCash());

        // Reconstruir TicketManager
        TicketManager tm = TicketManager.getInstance();
    }
}


