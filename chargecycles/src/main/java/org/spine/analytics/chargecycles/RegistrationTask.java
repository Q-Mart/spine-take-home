package org.spine.analytics.chargecycles;

import static org.spine.analytics.chargecycles.Stubs.*;

import java.util.Set;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spine.utils.dto.RegistrationDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Class responsible for registering the service and also sending heartbeats asynchronously This is
 * more pseudocode for stubbing than anything.We'd want to use something already available as this
 * is a solved problem. If this was acutal prod code, it wouldn't live here. It would need to be in
 * a utlitiy module, as it's essentially the same behavior for each service.
 */
@Component
public class RegistrationTask {
  @Value("${spring.application.name}")
  private String serviceType;

  // TODO: Move this to spring config
  private final String serviceRegistryUri = "serviceregistry.com";

  @Value("{server.address}")
  private String serverAddress;

  private static final Logger Logger = LoggerFactory.getLogger(RegistrationTask.class);

  private String serviceId;

  @PostConstruct
  void init() {
    var details =
        new RegistrationDetails(
            serviceType,
            serverAddress,
            Set.of("/charge-cycles/v1/", "charge-cycles/v1/warranty-info"),
            "Retrieve/caclulate data related to charge cycles completed for an asset");

    Stubs.StatusAndServiceId result = postRegistrationHttpRequest(serviceRegistryUri, details);

    if (result.status() == 201) {
      serviceId = result.serviceId();
      Logger.info(
          "Registered to registry at {} with details {}. Registered service ID is {}",
          serviceRegistryUri,
          details,
          serviceId);
    } else {
      // TODO: Propagate error that is sent by service registry
      Logger.error(
          "Could not register to registry {} with details {}", serviceRegistryUri, details);

      // Rely on k8s to restart the container in order to retry registration
      System.exit(1);
    }
  }

  /**
   * Periodic task for sending heartbeats
   * TODO: find a way to configure the value for {@code fixedDelay} without hard-coding
   */
  @Scheduled(fixedDelay = 60000)
  void sendHeartbeat() {
    Logger.info("Sending heartbeat to service registry at {}", serviceRegistryUri);

    int status = putRegistrationHeartBeatHttpRequest(serviceRegistryUri, serviceId);

    if (status == 200) {
      Logger.info("Heartbeat sent successfully");
    } else {
      // TODO: Propagate error that is sent by service registry
      Logger.error("Heartbeat transmission failed");

      // Rely on k8s to restart the container in order to retry registration
      System.exit(1);
    }
  }
}
