package sodresoftwares.government.api.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import sodresoftwares.government.api.client.IbgeClient;
import sodresoftwares.government.api.exception.ApiException;
import sodresoftwares.government.api.model.user.StateDTO;

@Service
public class IbgeService {

	private static final Logger logger = LoggerFactory.getLogger(IbgeService.class);

    private final IbgeClient ibgeClient;

    public IbgeService(IbgeClient ibgeClient) {
        this.ibgeClient = ibgeClient;
    }

    @Cacheable(value = "states", key = "'all'")
    public List<StateDTO> getStates() {
        try {
        	System.out.println("passou aqui");
        	logger.debug("Calling IBGE API to fetch states");
            List<StateDTO> states = ibgeClient.getStates();

            if (states == null || states.isEmpty()) {
                logger.warn("No states found in API do IBGE response ");
                throw new ApiException(404, "No states found");
            }
            logger.debug("States returned by IBGE API: {}", states.size());
            
            return states;

        } catch (WebClientResponseException e) {
            logger.error("IBGE API error: status={}, body={}", e.getStatusCode().value(), e.getResponseBodyAsString());
            throw new ApiException(e.getStatusCode().value(), "IBGE API error: " + e.getMessage());
        } catch (ApiException e) {
        	throw e;
        } catch (Exception e) {
            logger.error("Failed to connect to IBGE API", e);
            throw new ApiException(501, "Failed communicate with IBGE API: " + e.getMessage());
        }
    }
}
