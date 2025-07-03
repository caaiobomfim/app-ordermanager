package caaiobomfim.app_ordermanager.adapter.in.rest.mapper;

import caaiobomfim.app_ordermanager.adapter.in.rest.dto.OrderRequest;
import caaiobomfim.app_ordermanager.adapter.in.rest.dto.OrderResponse;
import caaiobomfim.app_ordermanager.domain.model.Order;
import caaiobomfim.app_ordermanager.domain.model.OrderStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OrderMapperTest {

    @Test
    void shouldMapFromOrderRequestToOrder() {
        OrderRequest request = new OrderRequest();
        request.setClientId("1");
        request.setItems(List.of("item1", "item2"));

        Order order = OrderMapper.INSTANCE.mapFrom(request);

        assertNull(order.getId(), "O ID deve ser null pois foi ignorado no mapping");
        assertEquals("1", order.getClientId());
        assertEquals(List.of("item1", "item2"), order.getItems());
    }

    @Test
    void shouldMapFromOrderToOrderResponse() {
        Order order = new Order();
        order.setId("1");
        order.setClientId("2");
        order.setItems(List.of("item1", "item2"));
        order.setStatus(OrderStatus.PENDENTE);

        OrderResponse response = OrderMapper.INSTANCE.mapFrom(order);

        assertEquals("1", response.getId());
        assertEquals("2", response.getClientId());
        assertEquals(List.of("item1", "item2"), response.getItems());
        assertEquals(OrderStatus.PENDENTE, response.getStatus());
    }
}
