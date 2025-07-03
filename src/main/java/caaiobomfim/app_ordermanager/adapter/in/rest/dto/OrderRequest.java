package caaiobomfim.app_ordermanager.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {

    @JsonIgnore
    private String id;

    @NotBlank(message = "O campo clientId não pode ser vazio")
    @JsonProperty("clientId")
    private String clientId;

    @NotEmpty(message = "A lista de itens não pode ser vazia")
    @Size(min = 1, message = "Deve haver pelo menos um item")
    @JsonProperty("items")
    private List<@NotBlank(message = "Item não pode ser vazio") String> items;
}
