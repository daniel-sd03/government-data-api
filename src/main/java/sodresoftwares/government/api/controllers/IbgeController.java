package sodresoftwares.government.api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import sodresoftwares.government.api.exception.ApiException;
import sodresoftwares.government.api.model.user.StateDTO;
import sodresoftwares.government.api.services.IbgeService;

@RestController
public class IbgeController {

	private final IbgeService ibgeService;

	public IbgeController(IbgeService ibgeService) {
		this.ibgeService = ibgeService;
	}

	@GetMapping("/states")
	public ResponseEntity<?> getStates() {
		try {
			List<StateDTO> states = ibgeService.getStates();
			return ResponseEntity.ok(states);

		} catch (ApiException e) {
			return ResponseEntity.status(e.getStatusCode()).body(List.of());
		} catch (Exception e) {
			return ResponseEntity.status(500).body(List.of());
		}
	}
}
