package sodresoftwares.government.api.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class IbgeClient {

    private final WebClient webClient;

    public IbgeClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://servicodados.ibge.gov.br")
                .build();
    }

    public Object getStates() {
        return webClient
                .get()
                .uri("/api/v1/localidades/estados")
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}
