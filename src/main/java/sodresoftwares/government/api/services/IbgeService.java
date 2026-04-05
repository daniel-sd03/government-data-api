package sodresoftwares.government.api.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import sodresoftwares.government.api.client.IbgeClient;
import sodresoftwares.government.api.exception.ApiException;
import sodresoftwares.government.api.infra.handler.ApiHandler;
import sodresoftwares.government.api.model.user.MunicipalityDTO;
import sodresoftwares.government.api.model.user.RegionDTO;
import sodresoftwares.government.api.model.user.StateDTO;

@Service
public class IbgeService {
    private final IbgeClient ibgeClient;
    private final ApiHandler apiHandler;
    // Lazy self-injection to bypass Spring's proxy and allow @Cacheable to work on internal calls.
    private final IbgeService self;

    public IbgeService(IbgeClient ibgeClient, ApiHandler apiHandler, @Lazy IbgeService self) {
        this.ibgeClient = ibgeClient;
        this.apiHandler = apiHandler;
        this.self = self;
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

    @Cacheable(value = "municipalities", key = "'all'")
    public List<MunicipalityDTO> getAllMunicipalities() {
        return apiHandler.execute("IBGE Municipalities All - ",
                ibgeClient::getAllMunicipalities);
    }

    public List<MunicipalityDTO> searchMunicipalitiesByName(String name) {

        if (name == null || name.isBlank()) {
            throw new ApiException(400, "Name parameter is required");
        }

        List<MunicipalityDTO> AllMunicipalities = self.getAllMunicipalities();

        List<MunicipalityDTO> filteredMunicipalities = AllMunicipalities.stream()
                .filter(m -> m.nome() != null &&
                        m.nome().toLowerCase().contains(name.toLowerCase()))
                .toList();

        if(filteredMunicipalities.isEmpty()){
            throw new ApiException(404, "municipalities not found");
        }

        return filteredMunicipalities;
    }
}
