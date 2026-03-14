package sodresoftwares.government.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import sodresoftwares.government.api.client.IbgeClient;
import sodresoftwares.government.api.model.user.StateDTO;

@Service
public class IbgeService {

    private final IbgeClient ibgeClient;
    
    public IbgeService(IbgeClient ibgeClient) {
    	this.ibgeClient = ibgeClient;
    }

    public List<StateDTO> getStates() {
        return ibgeClient.getStates();
    }
}
