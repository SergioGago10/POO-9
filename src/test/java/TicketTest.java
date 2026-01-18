

import org.junit.jupiter.api.Test;
import upm.products.Item;
import upm.tickets.core.Ticket;
import upm.tickets.core.TicketContent;
import upm.tickets.core.TicketRenderer;
import upm.tickets.core.TicketState;
import upm.tickets.itemsaddition.ItemAdditionVisitor;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    // Ticket “dummy” para instanciar Ticket
    static class DummyTicket extends Ticket<upm.products.Item> {
        DummyTicket(String id) { super(id); }

        @Override public TicketContent getSortedContent() { return null; }
        @Override public void accept(TicketRenderer renderer) { }

        // Para añadir sin tocar itemsList (evita protected access)
        boolean addForTest(upm.products.Item item) { return internalAdd(item); }
    }

    // Item de pruebas: OJO -> EXTENDS (porque Item es clase abstracta)
    static class TestItem implements Item {
        private final String id;

        TestItem(String id) {
            super(); // si tu Item NO tiene constructor vacío, aquí te fallará y habrá que poner el super(...) real
            this.id = id;
        }

        @Override
        public String getId() { return id; }

        @Override
        public boolean addTo(Ticket<?> ticket) {
            return false;
        }
        @Override
        public boolean accept(ItemAdditionVisitor visitor, String[] args) {
            // no-op
            return false;
        }

        @Override
        public void setCategoryFromCLI(String value) {
            Item.super.setCategoryFromCLI(value);
        }
    }

    @Test
    //Comprueba que un ticket nuevo se crea vacío y con el identificador correcto.
    void constructor_startsEmpty_andKeepsTicketId() {
        DummyTicket t = new DummyTicket("T-001");
        assertEquals(TicketState.EMPTY, t.getEstado());
        assertEquals("T-001", t.getTicketMetadata().getTicketID());
    }

    @Test
    //Verifica que la lista de productos del ticket no se puede modificar desde fuera.
    void getItemsList_isReadOnly() {
        DummyTicket t = new DummyTicket("T-001");
        assertThrows(UnsupportedOperationException.class,
                () -> t.getItemsList().add(new TestItem("X")));
    }

    @Test
    //Comprueba que un producto se elimina correctamente y que el ticket vuelve a vacío si no quedan productos.
    void removeProduct_removesById_andIfEmpty_setsStateEmpty() {
        DummyTicket t = new DummyTicket("T-001");
        t.addForTest(new TestItem("A"));
        t.setEstado(TicketState.OPEN);

        t.removeProductFromTicket("A");

        assertTrue(t.getItemsList().isEmpty());
        assertEquals(TicketState.EMPTY, t.getEstado());
    }

    @Test
    //Verifica que al cerrar el ticket se cambia su estado a cerrado y se actualiza su identificador.
    void closeTicket_setsStateClose_andChangesTicketId() {
        DummyTicket t = new DummyTicket("T-001");
        String before = t.getTicketMetadata().getTicketID();

        t.closeTicket();

        assertEquals(TicketState.CLOSE, t.getEstado());
        assertNotEquals(before, t.getTicketMetadata().getTicketID());
    }

    @Test
    //Comprueba que no se pueden eliminar productos una vez que el ticket está cerrado
    void removeProduct_doesNothing_whenClosed() {
        DummyTicket t = new DummyTicket("T-001");
        t.addForTest(new TestItem("A"));
        t.closeTicket();

        t.removeProductFromTicket("A");

        assertEquals(1, t.getItemsList().size());
    }
}





