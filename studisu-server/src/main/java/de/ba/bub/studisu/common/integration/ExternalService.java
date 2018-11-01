package de.ba.bub.studisu.common.integration;

import de.ba.bub.studisu.common.exception.CommonServiceException;
import de.ba.bub.studisu.common.model.ExternalServiceStatus;

/**
 * Marker interface for monitorable or pingable external services
 * Created by loutzenhj on 30.05.2017.
 */
public interface ExternalService {

    /**
     * Service status for monitoring/healthcheck
     * @return service status
     * @throws CommonServiceException
     */
    ExternalServiceStatus getServiceStatus() throws CommonServiceException;
}
