package caaiobomfim.app_ordermanager.infrastructure.messaging;

import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class OrderPublisher {

    private final SqsAsyncClient sqsClient;

    private static final String QUEUE_URL = "https://sqs.sa-east-1.amazonaws.com/123456789012/order-queue";

    public OrderPublisher(SqsAsyncClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void publish(String message) {
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .messageBody(message)
                .build();

        sqsClient.sendMessage(request)
                .thenAccept(response -> {
                    System.out.println("Mensagem publicada no SQS com ID: " + response.messageId());
                });
    }
}
