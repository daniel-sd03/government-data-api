package sodresoftwares.government.api.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import sodresoftwares.government.api.client.IbgeClient;
import sodresoftwares.government.api.infra.handler.ApiHandler;
import sodresoftwares.government.api.model.user.MunicipalityDTO;
import sodresoftwares.government.api.model.user.RegionDTO;
import sodresoftwares.government.api.model.user.StateDTO;

@Service
public class IbgeService {
    private final IbgeClient ibgeClient;
    private final ApiHandler apiHandler;

    public IbgeService(IbgeClient ibgeClient, ApiHandler apiHandler) {
        this.ibgeClient = ibgeClient;
        this.apiHandler = apiHandler;
    }

    @Cacheable(value = "states", key = "'all'")
    public List<StateDTO> getStates() {
        return apiHandler.execute("IBGE States", ibgeClient::getStates);
    }

    @Cacheable(value = "regions", key = "'all'")
    public List<RegionDTO> getRegions() {
        return apiHandler.execute("IBGE Regions", ibgeClient::getRegions);
    }

    @Cacheable(value = "municipalities", key = "#uf.toUpperCase()")
    public List<MunicipalityDTO> getMunicipalitiesByStates(String uf) {
        return apiHandler.execute("IBGE Municipalities - " + uf.toUpperCase(),
                () -> ibgeClient.getMunicipalitiesByStates(uf));
    }
}
