package sodresoftwares.government.api.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import sodresoftwares.government.api.client.IbgeClient;
import sodresoftwares.government.api.exception.ApiException;
import sodresoftwares.government.api.infra.handler.ApiHandler;
import sodresoftwares.government.api.model.user.StateDTO;

@ExtendWith(MockitoExtension.class)
public class IbgeServiceTest {

	@Mock
	private IbgeClient ibgeClient;

	@Mock
	private ApiHandler apiHandler;

	@InjectMocks
	private IbgeService ibgeService;

	@Test
	void getStatesSuccess() {
        List<StateDTO> mockStates = List.of(
                new StateDTO(1, "Acre", "AC"),
                new StateDTO(2, "Alagoas", "AL")
            );

		when(apiHandler.execute(anyString(), any())).thenReturn(mockStates);

		List<StateDTO> result = ibgeService.getStates();

		assertThat(result).isEqualTo(mockStates);
		verify(apiHandler).execute(eq("IBGE States"), any());
	}
}
