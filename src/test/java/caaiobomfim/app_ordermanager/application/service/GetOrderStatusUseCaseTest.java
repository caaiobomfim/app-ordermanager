package caaiobomfim.app_ordermanager.application.service;

import caaiobomfim.app_ordermanager.domain.model.Order;
import caaiobomfim.app_ordermanager.domain.model.OrderStatus;
import caaiobomfim.app_ordermanager.repository.InMemoryOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetOrderStatusUseCaseTest {

    private InMemoryOrderRepository repository;
    private GetOrderStatusUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOrderRepository();
        useCase = new GetOrderStatusUseCase(repository);
    }

    @Test
    void shouldReturnOrderWhenExists() {
        Order order = new Order();
        order.setId("ID1");
        order.setClientId("CLIENTID1");
        order.setItems(List.of("item1", "item2"));
        order.setStatus(OrderStatus.PENDENTE);

        repository.save(order);

        Optional<Order> result = useCase.getById("ID1");

        assertTrue(result.isPresent());
        assertEquals("CLIENTID1", result.get().getClientId());
        assertEquals(OrderStatus.PENDENTE, result.get().getStatus());
    }

    @Test
    void shouldReturnEmptyWhenOrderDoesNotExist() {
        Optional<Order> result = useCase.getById("1");

        assertTrue(result.isEmpty());
    }
}
