package org.spine.analytics.chargecycles;

import java.util.UUID;

/**
 * Stub out HTTP requests as I do not have enough time to wire things together properly TODO: given
 * more time, do this stubbing in a more elegant manner (e.g. write out actually HTTP requests so we
 * can mock them precisely when doing integration testing)
 */
public class Stubs {
  public record StatusAndServiceId(int status, String serviceId) {}

  static StatusAndServiceId postRegistrationHttpRequest(String uri, Object params) {
    return new StatusAndServiceId(201, UUID.randomUUID().toString());
  }

  static int putRegistrationHeartBeatHttpRequest(String uri, String serviceId) {
    return 200;
  }
}
