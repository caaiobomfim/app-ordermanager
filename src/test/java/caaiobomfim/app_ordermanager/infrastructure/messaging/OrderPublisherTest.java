package caaiobomfim.app_ordermanager.infrastructure.messaging;

import caaiobomfim.app_ordermanager.domain.model.Order;
import caaiobomfim.app_ordermanager.domain.model.OrderStatus;
import caaiobomfim.app_ordermanager.repository.InMemoryOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderPublisherTest {

    private SqsAsyncClient sqsClient;
    private InMemoryOrderRepository repository;
    private OrderPublisher publisher;

    private static final String QUEUE_URL = "http://localhost:4566/000000000000/order-queue";

    @BeforeEach
    void setUp() {
        sqsClient = mock(SqsAsyncClient.class);
        repository = new InMemoryOrderRepository();
        publisher = new OrderPublisher(sqsClient, repository);

        try {
            var field = OrderPublisher.class.getDeclaredField("QUEUE_URL");
            field.setAccessible(true);
            field.set(publisher, QUEUE_URL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldSaveOrderAndSendToSqsQueue() throws Exception {
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setClientId("CLIENTID");
        order.setItems(List.of("item1", "item2"));
        order.setStatus(OrderStatus.PENDENTE);

        var future = CompletableFuture.completedFuture(SendMessageResponse.builder().messageId("123").build());
        when(sqsClient.sendMessage(any(SendMessageRequest.class))).thenReturn(future);

        publisher.publish(order);

        var saved = repository.findById(order.getId());
        assertTrue(saved.isPresent());
        assertEquals(order.getClientId(), saved.get().getClientId());

        verify(sqsClient, times(1)).sendMessage(any(SendMessageRequest.class));
    }
}