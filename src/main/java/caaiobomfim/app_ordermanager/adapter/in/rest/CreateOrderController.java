package caaiobomfim.app_ordermanager.adapter.in.rest;

import caaiobomfim.app_ordermanager.adapter.in.rest.dto.OrderRequest;
import caaiobomfim.app_ordermanager.adapter.in.rest.dto.OrderResponse;
import caaiobomfim.app_ordermanager.adapter.in.rest.mapper.OrderMapper;
import caaiobomfim.app_ordermanager.domain.model.Order;
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

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {

        Order order = OrderMapper.INSTANCE.mapFrom(orderRequest);

        order.setId(UUID.randomUUID().toString());

        OrderResponse response = OrderMapper.INSTANCE.mapFrom(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}