package sodresoftwares.government.api.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sodresoftwares.government.api.client.IbgeClient;
import sodresoftwares.government.api.model.user.StateDTO;

@ExtendWith(MockitoExtension.class)
public class IbgeServiceTest {

	@Mock
	private IbgeClient ibgeClient;

	@InjectMocks
	private IbgeService ibgeService;

	@Test
	void shouldReturnListOfStates() {
        List<StateDTO> mockStates = List.of(
                new StateDTO(1, "Acre", "AC"),
                new StateDTO(2, "Alagoas", "AL")
            );

		when(ibgeClient.getStates()).thenReturn(mockStates);

		List<StateDTO> result = ibgeService.getStates();

		assertThat(result).hasSize(2)
        .extracting(StateDTO::nome)
        .contains("Acre", "Alagoas");
		
		verify(ibgeClient, times(2)).getStates(); 
	}
}
