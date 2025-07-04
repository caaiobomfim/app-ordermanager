package caaiobomfim.app_ordermanager.infrastructure.messaging;

import caaiobomfim.app_ordermanager.domain.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
public class OrderPublisher {

    private final SqsAsyncClient sqsClient;

    @Value("${aws.sqs.queue.url}")
    private String QUEUE_URL;

    public OrderPublisher(SqsAsyncClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void publish(Order order) {
        try {
            String message = new ObjectMapper().writeValueAsString(order);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(QUEUE_URL)
                    .messageBody(message)
                    .build();

            sqsClient.sendMessage(request)
                    .thenAccept(response -> {
                        System.out.println("Mensagem publicada no SQS com ID: " + response.messageId());
                    });
        } catch (Exception e) {
            System.err.println("Erro ao publicar pedido: " + e.getMessage());
        }

    }
}
