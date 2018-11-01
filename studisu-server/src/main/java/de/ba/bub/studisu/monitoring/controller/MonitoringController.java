package de.ba.bub.studisu.monitoring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ba.bub.studisu.common.integration.bildungsangebotservice.BildungsangebotService;
import de.ba.bub.studisu.common.integration.dkz.DKZService;
import de.ba.bub.studisu.common.model.ExternalServiceStatus;

/**
 * Controller, die auf monitoring/health check "pings" hört,
 * und eine entsprechende http code zurückliefert, je nach service verfügbarkeit
 * Created by loutzenhj on 30.05.2017.
 */
@RestController
@RequestMapping("/pc/v1/mt")
public class MonitoringController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringController.class);

    /**
     * DKZService.
     */
    private final DKZService dkzService;
    /**
     * BildungsangebotService
     */
    private final BildungsangebotService bildungsangebotServiceClient;

    /**
     * C-tor with service parameters
     * @param dkzService
     * @param baClient
     */
    @Autowired
    public MonitoringController(
            @Qualifier("dkz") DKZService dkzService,
            BildungsangebotService baClient
            ){
        this.dkzService = dkzService;
        this.bildungsangebotServiceClient = baClient;
    }

    /**
     * Monitoring status endpoint
     * @return http 200 if bildungsangebotServiceClient and dkzService are available,
     * otherwise http 500
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getMonitoringStatus(){

        ExternalServiceStatus exstat = bildungsangebotServiceClient.getServiceStatus();
        if(exstat==null || exstat.getServiceStatus().equals(ExternalServiceStatus.Status.NICHT_VERFUEGBAR)){
            LOGGER.error("BildungsangebotService NICHT verfügbar!");
            return ResponseEntity.status(500).body(null);
        }
        exstat = dkzService.getServiceStatus();
        if(exstat==null || exstat.getServiceStatus().equals(ExternalServiceStatus.Status.NICHT_VERFUEGBAR)){
            LOGGER.error("DKZService NICHT verfügbar!");
            return ResponseEntity.status(500).body(null);
        }
        return ResponseEntity.status(200).body(null);
    }

    /**
     * Last chance exception handler
     * @param e
     * @return http 500 as unexpected error occured
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> behandleFehler(final Exception e){
        LOGGER.error("Exception during Monitoring! "+e.getMessage());
        return ResponseEntity.status(500).body(null);
    }
}
