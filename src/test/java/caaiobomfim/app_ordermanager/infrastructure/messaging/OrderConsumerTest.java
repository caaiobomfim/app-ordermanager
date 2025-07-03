package caaiobomfim.app_ordermanager.infrastructure.messaging;

import caaiobomfim.app_ordermanager.application.service.ProcessOrderUseCase;
import caaiobomfim.app_ordermanager.domain.model.Order;
import caaiobomfim.app_ordermanager.domain.model.OrderStatus;
import caaiobomfim.app_ordermanager.repository.InMemoryOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderConsumerTest {

    @Mock
    private SqsAsyncClient sqsClient;

    @Mock
    private ProcessOrderUseCase processOrderUseCase;

    @InjectMocks
    private OrderConsumer orderConsumer;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();

        orderConsumer = new OrderConsumer(sqsClient, new InMemoryOrderRepository(), processOrderUseCase, objectMapper);

        ReflectionTestUtils.setField(orderConsumer, "queue_url", "http://localhost:4566/000000000000/order-queue");
        ReflectionTestUtils.setField(orderConsumer, "processing_delay", 1L);
    }

    @Test
    void shouldProcessMessageAndUpdateOrder() throws Exception {
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setClientId("CLIENT");
        order.setItems(List.of("item1", "item2"));
        order.setStatus(OrderStatus.PENDENTE);

        String messageBody = objectMapper.writeValueAsString(order);

        Message sqsMessage = Message.builder()
                .body(messageBody)
                .receiptHandle("receipt-handle")
                .build();

        orderConsumer.processMessage(sqsMessage);

        verify(processOrderUseCase, times(1)).update(any(Order.class));
        verify(sqsClient, times(1)).deleteMessage(any(DeleteMessageRequest.class));
    }

}
