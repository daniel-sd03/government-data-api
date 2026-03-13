package sodresoftwares.government.api.services;

import org.springframework.stereotype.Service;

import sodresoftwares.government.api.client.IbgeClient;

@Service
public class IbgeService {

    private final IbgeClient ibgeClient;
    
    public IbgeService(IbgeClient ibgeClient) {
    	this.ibgeClient = ibgeClient;
    }

    public Object getStates() {
        return ibgeClient.getStates();
    }
}
