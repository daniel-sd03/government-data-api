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
import sodresoftwares.government.api.model.user.MunicipalityDTO;
import sodresoftwares.government.api.model.user.RegionDTO;
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

	@Test
	void getMunicipalitiesByStatesSuccess() {
		String uf = "SP";

		List<MunicipalityDTO> mockMunicipalities = List.of(
				new MunicipalityDTO(1, "São Paulo"),
				new MunicipalityDTO(2, "Campinas")
		);

		when(apiHandler.execute(anyString(), any())).thenReturn(mockMunicipalities);

		List<MunicipalityDTO> result = ibgeService.getMunicipalitiesByStates(uf);

		assertThat(result).isEqualTo(mockMunicipalities);
		verify(apiHandler).execute(eq("IBGE Municipalities - SP"), any());
	}

	@Test
	void getRegionsSuccess() {
        List<RegionDTO> mockRegions = List.of(
                new RegionDTO(1, "Norte", "N"),
                new RegionDTO(2, "Nordeste", "NE")
            );

		when(apiHandler.execute(anyString(), any())).thenReturn(mockRegions);

		List<RegionDTO> result = ibgeService.getRegions();

		assertThat(result).isEqualTo(mockRegions);
		verify(apiHandler).execute(eq("IBGE Regions"), any());
	}
}
