package caaiobomfim.app_ordermanager.adapter.in.rest.dto;

import caaiobomfim.app_ordermanager.domain.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("items")
    private List<String> items;

    @JsonProperty("status")
    private OrderStatus status;

}