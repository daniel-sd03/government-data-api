package sodresoftwares.government.api.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import sodresoftwares.government.api.model.user.MunicipalityDTO;
import sodresoftwares.government.api.model.user.RegionDTO;
import sodresoftwares.government.api.model.user.StateDTO;

@Component
public class IbgeClient {

    private final WebClient webClient;

    public IbgeClient(WebClient.Builder builder) {
        final int size = 16 * 1024 * 1024; // 16MB
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
        this.webClient = builder
                .exchangeStrategies(strategies)
                .baseUrl("https://servicodados.ibge.gov.br/api/v1/localidades")
                .build();
    }

    public List<StateDTO> getStates() {
        return webClient
                .get()
                .uri("/estados")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<StateDTO>>() {})
                .block();
    }

    public List<RegionDTO> getRegions() {
        return webClient
                .get()
                .uri("/regioes")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<RegionDTO>>() {})
                .block();
    }

    public List<MunicipalityDTO> getMunicipalitiesByStates(String uf) {
        return webClient
                .get()
                .uri("/estados/{uf}/municipios", uf.toUpperCase())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MunicipalityDTO>>() {})
                .block();
    }
}
