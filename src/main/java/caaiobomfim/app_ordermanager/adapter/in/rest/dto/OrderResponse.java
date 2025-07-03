package caaiobomfim.app_ordermanager.adapter.in.rest.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderResponse {

    private String id;
    private List<String> items;
    private String status;

    public OrderResponse(String id, List<String> items, String status) {
        this.id = id;
        this.items = items;
        this.status = status;
    }

}
