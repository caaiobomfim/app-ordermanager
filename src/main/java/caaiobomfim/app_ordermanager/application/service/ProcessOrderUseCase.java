package caaiobomfim.app_ordermanager.application.service;

import caaiobomfim.app_ordermanager.domain.model.Order;
import caaiobomfim.app_ordermanager.domain.model.OrderStatus;
import caaiobomfim.app_ordermanager.infrastructure.messaging.OrderPublisher;
import caaiobomfim.app_ordermanager.repository.InMemoryOrderRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProcessOrderUseCase {

    private final InMemoryOrderRepository repository;
    private final OrderPublisher orderPublisher;

    public ProcessOrderUseCase(InMemoryOrderRepository repository, OrderPublisher orderPublisher) {
        this.repository = repository;
        this.orderPublisher = orderPublisher;
    }

    public Order publish(Order order) {
        order.setId(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.PENDENTE);

        repository.save(order);
        orderPublisher.publish(order);

        return order;
    }

    public void update(Order order) {
        order.setStatus(OrderStatus.PROCESSADO);
        repository.update(order);
    }
}
