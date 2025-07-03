package caaiobomfim.app_ordermanager.infrastructure.messaging;

import caaiobomfim.app_ordermanager.application.service.ProcessOrderUseCase;
import caaiobomfim.app_ordermanager.domain.model.Order;
import caaiobomfim.app_ordermanager.domain.model.OrderStatus;
import caaiobomfim.app_ordermanager.repository.InMemoryOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Component
public class OrderConsumer {

    private final SqsAsyncClient sqsClient;
    private final ProcessOrderUseCase processOrderUseCase;
    private final ObjectMapper objectMapper;

    @Value("${aws.sqs.queue.url}")
    private String queue_url;

    @Value("${order.processing-delay}")
    private long processing_delay;

    public OrderConsumer(SqsAsyncClient sqsClient, InMemoryOrderRepository repository, ProcessOrderUseCase processOrderUseCase, ObjectMapper objectMapper) {
        this.sqsClient = sqsClient;
        this.processOrderUseCase = processOrderUseCase;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelayString = "${consumer.fixed-delay}", initialDelayString = "${consumer.initial-delay}")
    private void receiveMessages() {
        System.out.println("Buscando... ");

        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(queue_url)
                .maxNumberOfMessages(5)
                .waitTimeSeconds(2)
                .build();

        sqsClient.receiveMessage(request)
                .thenAccept(response -> {
                    if (response.hasMessages()) {
                        response.messages().forEach(this::processMessage);
                    }
                });
    }

    private void processMessage(Message message) {
        try {
            System.out.println("Mensagem recebida: " + message.body());

            Order order = objectMapper.readValue(message.body(), Order.class);

            Thread.sleep(processing_delay);

            processOrderUseCase.update(order);

            System.out.println("Pedido processado: " + order.getId());

            deleteMessage(message);
        } catch (Exception e) {
            System.err.println("Erro ao processar mensagem: " + e.getMessage());
        }
    }

    private void deleteMessage(Message message) {
        DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                .queueUrl(queue_url)
                .receiptHandle(message.receiptHandle())
                .build();

        sqsClient.deleteMessage(deleteRequest);
    }
}
