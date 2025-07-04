package caaiobomfim.app_ordermanager.domain.repository;

import caaiobomfim.app_ordermanager.domain.model.Order;

import java.util.Optional;

public interface OrderRepository {

    void save(Order order);
    Optional<Order> findById(String id);
    void update(Order order);
}
