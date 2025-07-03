package caaiobomfim.app_ordermanager.adapter.in.rest.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {

    private String clientId;
    private List<String> items;
}
