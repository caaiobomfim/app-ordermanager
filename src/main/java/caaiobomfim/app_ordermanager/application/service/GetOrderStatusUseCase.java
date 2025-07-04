package caaiobomfim.app_ordermanager.application.service;

import caaiobomfim.app_ordermanager.domain.model.Order;
import caaiobomfim.app_ordermanager.repository.InMemoryOrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetOrderStatusUseCase {

    private final InMemoryOrderRepository repository;

    public GetOrderStatusUseCase(InMemoryOrderRepository repository) {
        this.repository = repository;
    }

    public Optional<Order> getById(String id) {
        return repository.findById(id);
    }
}
