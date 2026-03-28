package sodresoftwares.government.api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import sodresoftwares.government.api.client.IbgeClient;
import sodresoftwares.government.api.exception.ApiException;
import sodresoftwares.government.api.model.user.StateDTO;
import sodresoftwares.government.api.repositories.UserRepository;

@SpringBootTest(properties = { 
		"spring.cache.type=simple",
		"spring.autoconfigure.exclude=" 
				+ "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
				+ "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,"
				+ "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration" })
@ActiveProfiles("test")
public class IbgeServiceCacheTest {
	
	@Autowired
	private IbgeService ibgeService;

	@MockitoBean
	private IbgeClient ibgeClient;

	@MockitoBean
	private UserRepository userRepository;

	@Test
	@DisplayName("should return cached and call client only once")
	void getStatesCacheSucess() {
		List<StateDTO> states = List.of(new StateDTO(1, "Acre", "AC"));

		when(ibgeClient.getStates()).thenReturn(states);

		List<StateDTO> result01  =  ibgeService.getStates();
		
		List<StateDTO> result02 = ibgeService.getStates();

		assertEquals(result01, result02);
		verify(ibgeClient, times(1)).getStates();
	}

	@Test
	@DisplayName("should not cache results when IBGE client throws an unexpected error")
	void getStatesCacheError1() {
		when(ibgeClient.getStates()).thenThrow(new RuntimeException("Something went wrong"));

		assertThrows(ApiException.class, () -> ibgeService.getStates());

		assertThrows(ApiException.class, () -> ibgeService.getStates());

		verify(ibgeClient, times(2)).getStates();
	}

	@Test
	@DisplayName("should not cache results when IBGE client returns empty list")
	void getStatesCacheError2() {
		when(ibgeClient.getStates()).thenReturn(List.of());

		assertThrows(ApiException.class, () -> ibgeService.getStates());

		assertThrows(ApiException.class, () -> ibgeService.getStates());

		verify(ibgeClient, times(2)).getStates();
	}
}
