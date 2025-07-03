package caaiobomfim.app_ordermanager.adapter.in.rest.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class RestControllerExceptionHandlerTest {

    private RestControllerExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new RestControllerExceptionHandler();
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() {
        FieldError fieldError = new FieldError("orderRequest", "clientId", "O campo clientId não pode ser vazio");
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "orderRequest");
        bindingResult.addError(fieldError);

        MethodParameter parameter = mock(MethodParameter.class);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<Map<String, Object>> response = handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).containsKey("errors"));
        assertEquals("O campo clientId não pode ser vazio", ((Map<?, ?>) response.getBody().get("errors")).get("clientId"));
        assertEquals("Erro de validação nos campos.", response.getBody().get("message"));
    }

    @Test
    void shouldReturnInternalServerErrorWhenGenericExceptionOccurs() {
        Exception exception = new RuntimeException("Simulated failure");

        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Simulated failure", Objects.requireNonNull(response.getBody()).get("message"));
        assertEquals("Erro interno do servidor", response.getBody().get("error"));
    }
}
