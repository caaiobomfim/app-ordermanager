package caaiobomfim.app_ordermanager.adapter.in.rest;

import caaiobomfim.app_ordermanager.adapter.in.rest.dto.OrderRequest;
import caaiobomfim.app_ordermanager.adapter.in.rest.dto.OrderResponse;
import caaiobomfim.app_ordermanager.adapter.in.rest.mapper.OrderMapper;
import caaiobomfim.app_ordermanager.application.service.GetOrderStatusUseCase;
import caaiobomfim.app_ordermanager.application.service.ProcessOrderUseCase;
import caaiobomfim.app_ordermanager.domain.model.Order;
import caaiobomfim.app_ordermanager.domain.model.OrderStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private ProcessOrderUseCase processOrderUseCase;
    private GetOrderStatusUseCase getOrderStatusUseCase;
    private OrderController controller;

    @BeforeEach
    void setUp() {
        processOrderUseCase = mock(ProcessOrderUseCase.class);
        getOrderStatusUseCase = mock(GetOrderStatusUseCase.class);
        controller = new OrderController(processOrderUseCase, getOrderStatusUseCase);
    }

    @Test
    void shouldCreateOrderSuccessfully() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setClientId("6865c1cd-b224-8001-929c-27cc31a8d43f");
        request.setItems(List.of("item1"));

        Order order = OrderMapper.INSTANCE.mapFrom(request);
        order.setId("fb2ac077-eea7-4bda-802a-cb29d7599f2d");
        order.setStatus(OrderStatus.PENDENTE);

        when(processOrderUseCase.publish(any(Order.class))).thenReturn(order);

        ResponseEntity<OrderResponse> response = controller.createOrder(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("fb2ac077-eea7-4bda-802a-cb29d7599f2d", Objects.requireNonNull(response.getBody()).getId());
        assertEquals(OrderStatus.PENDENTE, response.getBody().getStatus());
    }

    @Test
    void shouldReturnOrderWhenFound() {
        Order order = new Order();
        order.setId("fb2ac077-eea7-4bda-802a-cb29d7599f2d");
        order.setClientId("6865c1cd-b224-8001-929c-27cc31a8d43f");
        order.setItems(List.of("item1"));
        order.setStatus(OrderStatus.PROCESSADO);

        when(getOrderStatusUseCase.getById("fb2ac077-eea7-4bda-802a-cb29d7599f2d")).thenReturn(Optional.of(order));

        ResponseEntity<OrderResponse> response = controller.getOrder("fb2ac077-eea7-4bda-802a-cb29d7599f2d");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("fb2ac077-eea7-4bda-802a-cb29d7599f2d", Objects.requireNonNull(response.getBody()).getId());
        assertEquals(OrderStatus.PROCESSADO, response.getBody().getStatus());
    }

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() {
        when(getOrderStatusUseCase.getById("1")).thenReturn(Optional.empty());

        ResponseEntity<OrderResponse> response = controller.getOrder("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}