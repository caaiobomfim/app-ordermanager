package caaiobomfim.app_ordermanager.domain.model;

import lombok.Getter;

import java.util.List;

@Getter
public class Order {

    private final String id;
    private final String clientId;
    private final List<String> items;
    private final String status;

    public Order(String id, String clientId, List<String> items, String status) {
        this.id = id;
        this.clientId = clientId;
        this.items = items;
        this.status = status;
    }

}