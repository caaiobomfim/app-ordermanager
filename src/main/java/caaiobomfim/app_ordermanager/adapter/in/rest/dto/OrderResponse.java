package caaiobomfim.app_ordermanager.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("items")
    private List<String> items;

    @JsonProperty("status")
    private String status;

}