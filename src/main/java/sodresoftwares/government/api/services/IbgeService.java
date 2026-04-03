package sodresoftwares.government.api.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import sodresoftwares.government.api.client.IbgeClient;
import sodresoftwares.government.api.infra.handler.ApiHandler;
import sodresoftwares.government.api.model.user.StateDTO;

@Service
public class IbgeService {
    private final IbgeClient ibgeClient;
    private final ApiHandler apiHandler; //

    public IbgeService(IbgeClient ibgeClient, ApiHandler apiHandler) {
        this.ibgeClient = ibgeClient;
        this.apiHandler = apiHandler;
    }

    @Cacheable(value = "states", key = "'all'")
    public List<StateDTO> getStates() {
        return apiHandler.execute("IBGE States", ibgeClient::getStates);
    }
}
