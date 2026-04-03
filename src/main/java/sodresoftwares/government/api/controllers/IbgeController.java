package sodresoftwares.government.api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import sodresoftwares.government.api.model.user.MunicipalityDTO;
import sodresoftwares.government.api.model.user.RegionDTO;
import sodresoftwares.government.api.model.user.StateDTO;
import sodresoftwares.government.api.services.IbgeService;

@RestController
public class IbgeController {

	private final IbgeService ibgeService;

	public IbgeController(IbgeService ibgeService) {
		this.ibgeService = ibgeService;
	}
	
	@GetMapping("/states")
	public ResponseEntity<List<StateDTO>> getStates() {
			List<StateDTO> states = ibgeService.getStates();
			return ResponseEntity.ok(states);
	}

	@GetMapping("/regioes")
	public ResponseEntity<List<RegionDTO>> getRegions() {
		List<RegionDTO> regions = ibgeService.getRegions();
		return ResponseEntity.ok(regions);
	}

	@GetMapping("/states/{uf}/municipalities")
	public ResponseEntity<List<MunicipalityDTO>> getMunicipalitiesByStates(@PathVariable String uf) {
		List<MunicipalityDTO> municipalities = ibgeService.getMunicipalitiesByStates(uf);
		return ResponseEntity.ok(municipalities);
	}
}
