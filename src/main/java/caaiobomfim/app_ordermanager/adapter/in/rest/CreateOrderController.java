package caaiobomfim.app_ordermanager.adapter.in.rest;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
public class CreateOrderController {

    @PostMapping
    public ResponseEntity<Object> createOrder(@Valid @RequestBody Object request) {
        Object response = new Object();
        return ResponseEntity.status(201).body(response);
    }

}