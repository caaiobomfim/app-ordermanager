package caaiobomfim.app_ordermanager.adapter.in.rest;

import caaiobomfim.app_ordermanager.adapter.in.rest.dto.OrderRequest;
import caaiobomfim.app_ordermanager.adapter.in.rest.dto.OrderResponse;
import caaiobomfim.app_ordermanager.adapter.in.rest.mapper.OrderMapper;
import caaiobomfim.app_ordermanager.domain.model.Order;
import caaiobomfim.app_ordermanager.domain.model.OrderStatus;
import caaiobomfim.app_ordermanager.infrastructure.messaging.OrderPublisher;
import caaiobomfim.app_ordermanager.repository.InMemoryOrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
public class OrderController {

    private final InMemoryOrderRepository repository;
    private final OrderPublisher orderPublisher;
    private final ObjectMapper objectMapper;

    public OrderController(InMemoryOrderRepository repository, OrderPublisher orderPublisher, ObjectMapper objectMapper) {
        this.repository = repository;
        this.orderPublisher = orderPublisher;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) throws JsonProcessingException {

        Order order = OrderMapper.INSTANCE.mapFrom(orderRequest);

        order.setId(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.PENDENTE);

        orderPublisher.publish(order);

        OrderResponse orderResponse = OrderMapper.INSTANCE.mapFrom(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String id) {
        return repository.findById(id)
                .map(order -> ResponseEntity.ok(OrderMapper.INSTANCE.mapFrom(order)))
                .orElse(ResponseEntity.notFound().build());
    }

}