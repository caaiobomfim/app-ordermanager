package caaiobomfim.app_ordermanager.repository;

import caaiobomfim.app_ordermanager.domain.model.Order;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryOrderRepository {

    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    public void save(Order order) {
        orders.put(order.getId(), order);
    }

    public Optional<Order> findById(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    public void update(Order order) {
        orders.put(order.getId(), order);
    }

    public Map<String, Order> getAll() {
        return orders;
    }
}
