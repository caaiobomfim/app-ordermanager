package caaiobomfim.app_ordermanager.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Order {

    private String id;
    private String clientId;
    private List<String> items;
    private String status;

    public Order(String id, String clientId, List<String> items, String status) {
        this.id = id;
        this.clientId = clientId;
        this.items = items;
        this.status = status;
    }

}