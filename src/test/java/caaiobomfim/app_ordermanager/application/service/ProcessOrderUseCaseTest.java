package caaiobomfim.app_ordermanager.application.service;

import caaiobomfim.app_ordermanager.domain.model.Order;
import caaiobomfim.app_ordermanager.domain.model.OrderStatus;
import caaiobomfim.app_ordermanager.infrastructure.messaging.OrderPublisher;
import caaiobomfim.app_ordermanager.repository.InMemoryOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ProcessOrderUseCaseTest {

    private InMemoryOrderRepository repository;
    private OrderPublisher orderPublisher;
    private ProcessOrderUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOrderRepository();
        orderPublisher = mock(OrderPublisher.class);
        useCase = new ProcessOrderUseCase(repository, orderPublisher);
    }

    @Test
    void shouldPublishOrderAndSaveWithPendingStatus() {
        Order order = new Order();
        order.setClientId("CLIENTID1");
        order.setItems(List.of("item1", "item2"));

        Order result = useCase.publish(order);

        assertNotNull(result.getId());
        assertEquals(OrderStatus.PENDENTE, result.getStatus());
        assertEquals(result, repository.findById(result.getId()).orElse(null));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderPublisher, times(1)).publish(orderCaptor.capture());

        Order publishedOrder = orderCaptor.getValue();
        assertEquals(result.getId(), publishedOrder.getId());
        assertEquals(OrderStatus.PENDENTE, publishedOrder.getStatus());
    }

    @Test
    void shouldUpdateOrderStatusToProcessed() {
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setClientId("CLIENTID1");
        order.setItems(List.of("itemX"));
        order.setStatus(OrderStatus.PENDENTE);

        repository.save(order);

        useCase.update(order);

        Order updated = repository.findById(order.getId()).orElseThrow();
        assertEquals(OrderStatus.PROCESSADO, updated.getStatus());
    }

}