package sodresoftwares.government.api.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import sodresoftwares.government.api.model.user.StateDTO;

@Component
public class IbgeClient {

    private final WebClient webClient;

    public IbgeClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://servicodados.ibge.gov.br")
                .build();
    }

    public List<StateDTO> getStates() {
        return webClient
                .get()
                .uri("/api/v1/localidades/estados")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<StateDTO>>() {})
                .block();
    }
}
