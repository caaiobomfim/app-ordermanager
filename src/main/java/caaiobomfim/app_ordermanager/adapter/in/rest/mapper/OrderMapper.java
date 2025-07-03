package caaiobomfim.app_ordermanager.adapter.in.rest.mapper;

import caaiobomfim.app_ordermanager.adapter.in.rest.dto.OrderRequest;
import caaiobomfim.app_ordermanager.adapter.in.rest.dto.OrderResponse;
import caaiobomfim.app_ordermanager.domain.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "id", target = "id", ignore = true)
    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "items", target = "items")
    Order mapFrom(OrderRequest request);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "status", target = "status")
    OrderResponse mapFrom(Order order);
}
