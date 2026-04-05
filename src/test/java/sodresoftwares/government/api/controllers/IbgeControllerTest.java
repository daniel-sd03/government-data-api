package sodresoftwares.government.api.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import sodresoftwares.government.api.exception.ApiException;
import sodresoftwares.government.api.infra.security.SecurityFilter;
import sodresoftwares.government.api.model.user.MunicipalityDTO;
import sodresoftwares.government.api.model.user.RegionDTO;
import sodresoftwares.government.api.model.user.StateDTO;
import sodresoftwares.government.api.services.IbgeService;

@WebMvcTest(
	    controllers = IbgeController.class,
	    excludeFilters = @ComponentScan.Filter(
	        type = FilterType.ASSIGNABLE_TYPE,
	        classes = SecurityFilter.class
	    )
	)
@AutoConfigureMockMvc(addFilters = false)
public class IbgeControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IbgeService ibgeService;
   
    @Test
    void getStatesSucess() throws Exception {

        List<StateDTO> states = List.of(
            new StateDTO(1, "Acre", "AC"),
            new StateDTO(2, "Alagoas", "AL")

        );

        when(ibgeService.getStates()).thenReturn(states);

        mockMvc.perform(get("/states"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Acre"));
    }
    
    @Test
    void getStatesError() throws Exception {

        when(ibgeService.getStates()).thenThrow(new ApiException(404, "No states found"));

        mockMvc.perform(get("/states"))
        	.andExpect(status().isNotFound())
        	.andExpect(jsonPath("$.status").value(404))
        	.andExpect(jsonPath("$.message").value("No states found"));
    }

    @Test
    void getRegionsSuccess() throws Exception {

        List<RegionDTO> regions = List.of(
            new RegionDTO(1, "Norte", "N"),
            new RegionDTO(2, "Nordeste", "NE")
        );

        when(ibgeService.getRegions()).thenReturn(regions);

        mockMvc.perform(get("/regioes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Norte"));
    }

    @Test
    void getRegionsError() throws Exception {

        when(ibgeService.getRegions()).thenThrow(new ApiException(404, "No regions found"));

        mockMvc.perform(get("/regioes"))
        	.andExpect(status().isNotFound())
        	.andExpect(jsonPath("$.status").value(404))
        	.andExpect(jsonPath("$.message").value("No regions found"));
    }

    @Test
    void getMunicipalitiesByStatesSuccess() throws Exception {
        String uf = "SP";

        List<MunicipalityDTO> municipalities = List.of(
                new MunicipalityDTO(1, "São Paulo"),
                new MunicipalityDTO(2, "Campinas")
        );

        when(ibgeService.getMunicipalitiesByStates(uf)).thenReturn(municipalities);

        mockMvc.perform(get("/states/{uf}/municipalities", uf))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nome").value("São Paulo"));
    }

    @Test
    void getMunicipalitiesByStatesError() throws Exception {
        String uf = "SP";

        when(ibgeService.getMunicipalitiesByStates(uf))
                .thenThrow(new ApiException(404, "No municipalities found"));

        mockMvc.perform(get("/states/{uf}/municipalities", uf))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("No municipalities found"));
    }

    @Test
    void getAllMunicipalitiesSuccess() throws Exception {
        List<MunicipalityDTO> municipalities = List.of(
                new MunicipalityDTO(1, "São Paulo"),
                new MunicipalityDTO(2, "Belo Horizonte")
        );

        when(ibgeService.getAllMunicipalities()).thenReturn(municipalities);

        mockMvc.perform(get("/municipalities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nome").value("São Paulo"));
    }

    @Test
    void getAllMunicipalitiesError() throws Exception {
        when(ibgeService.getAllMunicipalities())
                .thenThrow(new ApiException(404, "No municipalities found"));

        mockMvc.perform(get("/municipalities"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("No municipalities found"));
    }

    @Test
    void searchMunicipalitiesSuccess() throws Exception {
        String searchName = "São";

        List<MunicipalityDTO> municipalities = List.of(
                new MunicipalityDTO(1, "São Paulo"),
                new MunicipalityDTO(3, "São Bernardo do Campo")
        );

        when(ibgeService.searchMunicipalitiesByName(searchName)).thenReturn(municipalities);

        mockMvc.perform(get("/municipalities/search").param("name", searchName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nome").value("São Paulo"))
                .andExpect(jsonPath("$[1].nome").value("São Bernardo do Campo"));
    }

    @Test
    void searchMunicipalitiesError() throws Exception {
        String searchName = "Atlantida";

        when(ibgeService.searchMunicipalitiesByName(searchName))
                .thenThrow(new ApiException(404, "municipalities not found"));

        mockMvc.perform(get("/municipalities/search").param("name", searchName))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("municipalities not found"));
    }
}
