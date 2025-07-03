package caaiobomfim.app_ordermanager.adapter.in.rest;

import caaiobomfim.app_ordermanager.adapter.in.rest.dto.OrderRequest;
import caaiobomfim.app_ordermanager.adapter.in.rest.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class CreateOrderController {

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        List<String> items = new ArrayList<>();
        OrderResponse response = new OrderResponse();
        return ResponseEntity.status(201).body(response);
    }

}