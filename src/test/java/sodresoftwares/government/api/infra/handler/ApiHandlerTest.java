package sodresoftwares.government.api.infra.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import sodresoftwares.government.api.exception.ApiException;

public class ApiHandlerTest {
    private final ApiHandler apiHandler = new ApiHandler();

    @Test
    @DisplayName("Should return data successfully when the supplier executes without errors")
    void apiHandlerTestSuccess() {
        String expected = "API Success Data";

        String result = apiHandler.execute("Success Test", () -> expected);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Should throw 404 ApiException when the returned list is empty")
    void apiHandlerTestError() {
        ApiException ex = assertThrows(ApiException.class, () ->
                apiHandler.execute("IBGE States", List::of)
        );

        assertEquals(404, ex.getStatusCode());
        assertTrue(ex.getMessage().contains("No data found for IBGE States"));
    }

    @Test
    @DisplayName("Should throw ApiException with original status code when WebClientResponseException occurs")
    void apiHandlerTestError1() {
        WebClientResponseException webEx = WebClientResponseException.create(
                403, "Forbidden", null, null, null
        );

        ApiException ex = assertThrows(ApiException.class, () ->
                apiHandler.execute("Gov API", () -> { throw webEx; })
        );

        assertEquals(403, ex.getStatusCode());
        assertTrue(ex.getMessage().contains("Gov API API error"));
    }

    @Test
    @DisplayName("Should throw 501 ApiException when a generic RuntimeException occurs")
    void apiHandlerTestError2() {
        RuntimeException runtimeEx = new RuntimeException("Connection refused");

        ApiException ex = assertThrows(ApiException.class, () ->
                apiHandler.execute("Service X", () -> { throw runtimeEx; })
        );

        assertEquals(501, ex.getStatusCode());
        assertTrue(ex.getMessage().contains("Failed to communicate with API: Connection refused"));
    }
}
