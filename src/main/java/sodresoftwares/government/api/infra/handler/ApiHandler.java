package sodresoftwares.government.api.infra.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import sodresoftwares.government.api.exception.ApiException;

import java.util.List;
import java.util.function.Supplier;

@Component
public class ApiHandler {
    private static final Logger logger = LoggerFactory.getLogger(ApiHandler.class);

    public <T> T execute(String context, Supplier<T> action) {
        try {
            logger.debug("Starting execution for: {}", context);
            T result = action.get();

            if (result == null || (result instanceof List && ((List<?>) result).isEmpty())) {
                logger.warn("No data found for: {}", context);
                throw new ApiException(404, "No data found for " + context);
            }
            return result;

        } catch (WebClientResponseException e) {
            logger.error("{} API error: status={}, body={}", context, e.getStatusCode().value(), e.getResponseBodyAsString());
            throw new ApiException(e.getStatusCode().value(), context + " API error: " + e.getMessage());
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to connect to {} API", context, e);
            throw new ApiException(501, "Failed to communicate with API: " + e.getMessage());
        }
    }
}