package sodresoftwares.government.api.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import sodresoftwares.government.api.client.IbgeClient;
import sodresoftwares.government.api.exception.ApiException;
import sodresoftwares.government.api.model.user.StateDTO;

@ExtendWith(MockitoExtension.class)
public class IbgeServiceTest {

	@Mock
	private IbgeClient ibgeClient;

	@InjectMocks
	private IbgeService ibgeService;

	@Test
	void getStatesSucess() {
        List<StateDTO> mockStates = List.of(
                new StateDTO(1, "Acre", "AC"),
                new StateDTO(2, "Alagoas", "AL")
            );

		when(ibgeClient.getStates()).thenReturn(mockStates);

		List<StateDTO> result = ibgeService.getStates();

		assertThat(result).hasSize(2)
        .extracting(StateDTO::nome)
        .contains("Acre", "Alagoas");
		
		verify(ibgeClient, times(1)).getStates(); 
	}
	
	@Test
	void getStatesError1() {
		
		when(ibgeClient.getStates()).thenReturn(List.of());
		
		 ApiException ex = assertThrows(ApiException.class, () -> ibgeService.getStates());

		 assertEquals(404, ex.getStatusCode());
		 assertTrue(ex.getMessage().contains("No states found"));
	}
	
	@Test
	void getStatesError2() {
		
	    WebClientResponseException webEx = WebClientResponseException.create(
	        500, "Internal Server Error", null, null, null
	    );

	    when(ibgeClient.getStates()).thenThrow(webEx);

	    ApiException ex = assertThrows(ApiException.class, () -> ibgeService.getStates());

	    assertEquals(500, ex.getStatusCode());
	    assertTrue(ex.getMessage().contains("IBGE API error:"));
	}
	
	@Test
	void getStatesError3() {
	    when(ibgeClient.getStates()).thenThrow(new RuntimeException("Something went wrong"));

	    ApiException ex = assertThrows(ApiException.class, () -> ibgeService.getStates());

	    assertEquals(501, ex.getStatusCode());
	    assertEquals("Failed communicate with IBGE API: Something went wrong", ex.getMessage());
	}
}
