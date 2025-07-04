package caaiobomfim.app_ordermanager.repository;

import caaiobomfim.app_ordermanager.domain.model.Order;
import caaiobomfim.app_ordermanager.domain.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryOrderRepositoryTest {

    private InMemoryOrderRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOrderRepository();
    }

    @Test
    void shouldSaveAndFindOrderById() {
        Order order = new Order();
        order.setId("1");
        order.setClientId("ID1");
        order.setItems(List.of("item1", "item2"));
        order.setStatus(OrderStatus.PENDENTE);

        repository.save(order);
        Optional<Order> result = repository.findById("1");

        assertTrue(result.isPresent());
        assertEquals("ID1", result.get().getClientId());
    }

    @Test
    void shouldReturnEmptyWhenOrderNotFound() {
        Optional<Order> result = repository.findById("not-exist");
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldUpdateExistingOrder() {
        Order order = new Order();
        order.setId("1");
        order.setClientId("CLIENT1");
        order.setItems(List.of("item"));
        order.setStatus(OrderStatus.PENDENTE);

        repository.save(order);

        order.setClientId("CLIENT1");
        order.setItems(List.of("item"));
        order.setStatus(OrderStatus.PROCESSADO);

        repository.update(order);

        Optional<Order> updated = repository.findById("1");
        assertTrue(updated.isPresent());
        assertEquals("CLIENT1", updated.get().getClientId());
        assertEquals(OrderStatus.PROCESSADO, updated.get().getStatus());
    }
}
