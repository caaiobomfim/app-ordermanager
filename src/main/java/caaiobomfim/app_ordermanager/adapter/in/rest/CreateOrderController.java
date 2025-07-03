package caaiobomfim.app_ordermanager.adapter.in.rest;

import caaiobomfim.app_ordermanager.adapter.in.rest.dto.OrderRequest;
import caaiobomfim.app_ordermanager.adapter.in.rest.dto.OrderResponse;
import caaiobomfim.app_ordermanager.adapter.in.rest.mapper.OrderMapper;
import caaiobomfim.app_ordermanager.domain.model.Order;
import caaiobomfim.app_ordermanager.domain.model.OrderStatus;
import caaiobomfim.app_ordermanager.infrastructure.messaging.OrderPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
public class CreateOrderController {

    private final OrderPublisher orderPublisher;
    private final ObjectMapper objectMapper;

    public CreateOrderController(OrderPublisher orderPublisher, ObjectMapper objectMapper) {
        this.orderPublisher = orderPublisher;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) throws JsonProcessingException {

        Order order = OrderMapper.INSTANCE.mapFrom(orderRequest);

        order.setId(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.PENDENTE);

        String message = objectMapper.writeValueAsString(order);
        orderPublisher.publish(message);

        order.setStatus(OrderStatus.PROCESSADO);
        OrderResponse orderResponse = OrderMapper.INSTANCE.mapFrom(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

}