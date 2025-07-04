package caaiobomfim.app_ordermanager.infrastructure.messaging;

import caaiobomfim.app_ordermanager.domain.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderPublisherTest {

    private SqsAsyncClient sqsClient;
    private OrderPublisher publisher;

    private static final String QUEUE_URL = "http://localhost:4566/000000000000/order-queue";

    @BeforeEach
    void setup() throws Exception {
        sqsClient = mock(SqsAsyncClient.class);
        publisher = new OrderPublisher(sqsClient);

        var field = OrderPublisher.class.getDeclaredField("QUEUE_URL");
        field.setAccessible(true);
        field.set(publisher, "http://queue-url");
    }

    @Test
    void devePublicarMensagemNoSqs() throws Exception {
        Order order = new Order();
        order.setId("ID1");
        order.setClientId("CLIENT1");
        order.setItems(List.of("item1"));
        SendMessageResponse mockResponse = SendMessageResponse.builder()
                .messageId("123")
                .build();
        when(sqsClient.sendMessage(any(SendMessageRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(mockResponse));

        publisher.publish(order);

        ArgumentCaptor<SendMessageRequest> captor = ArgumentCaptor.forClass(SendMessageRequest.class);
        verify(sqsClient, times(1)).sendMessage(captor.capture());

        SendMessageRequest sentRequest = captor.getValue();
        assertEquals("http://queue-url", sentRequest.queueUrl());

        ObjectMapper mapper = new ObjectMapper();
        String expectedMessage = mapper.writeValueAsString(order);
        assertEquals(expectedMessage, sentRequest.messageBody());
    }
}