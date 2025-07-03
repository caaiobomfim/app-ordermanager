package caaiobomfim.app_ordermanager.adapter.in.rest;

import caaiobomfim.app_ordermanager.adapter.in.rest.dto.OrderRequest;
import caaiobomfim.app_ordermanager.adapter.in.rest.dto.OrderResponse;
import caaiobomfim.app_ordermanager.adapter.in.rest.mapper.OrderMapper;
import caaiobomfim.app_ordermanager.application.service.GetOrderStatusUseCase;
import caaiobomfim.app_ordermanager.application.service.ProcessOrderUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class OrderController {

    private final ProcessOrderUseCase processOrderUseCase;
    private final GetOrderStatusUseCase getOrderStatusUseCase;

    public OrderController(ProcessOrderUseCase processOrderUseCase, GetOrderStatusUseCase getOrderStatusUseCase) {
        this.processOrderUseCase = processOrderUseCase;
        this.getOrderStatusUseCase = getOrderStatusUseCase;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {

        var order = OrderMapper.INSTANCE.mapFrom(orderRequest);

        var processedOrder = processOrderUseCase.publish(order);

        var orderResponse = OrderMapper.INSTANCE.mapFrom(processedOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String id) {
        return getOrderStatusUseCase.getById(id)
                .map(order -> ResponseEntity.ok(OrderMapper.INSTANCE.mapFrom(order)))
                .orElse(ResponseEntity.notFound().build());
    }

}