package de.ba.bub.studisu.common.model;

import java.io.Serializable;

/**
 * Simple model object for external service status
 * for monitoring
 * Created by loutzenhj on 30.05.2017.
 */
public class ExternalServiceStatus implements Serializable {

	private static final long serialVersionUID = -5650574210592895797L;

	/**
     * Status enums
     * very binary at the moment, can be extended later
     */
    public static enum Status{
        VERFUEGBAR,
        NICHT_VERFUEGBAR
    }

    private String serviceName;
    private Status serviceStatus;

    /**
     * C-tor with params
     * @param serviceName
     * @param serviceStatus
     */
    public ExternalServiceStatus(String serviceName, Status serviceStatus) {
        this.serviceName = serviceName;
        this.serviceStatus = serviceStatus;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Status getServiceStatus() {
        return serviceStatus;
    }
}
