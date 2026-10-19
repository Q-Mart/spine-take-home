package org.spine.analytics.chargecycles;

import com.google.common.collect.ImmutableList;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.spine.utils.dto.Asset;
import org.spine.utils.dto.MeteringDataPoint;

/**
 * Stub out HTTP requests as I do not have enough time to wire things together properly TODO: given
 * more time, do this stubbing in a more elegant manner (e.g. write out actually HTTP requests so we
 * can mock them precisely when doing integration testing)
 */
public class Stubs {
  public record StatusAndServiceId(int status, String serviceId) {}

  public record StatusAndAsset(int status, Asset asset) {}

  static StatusAndServiceId postRegistrationHttpRequest(String uri, Object params) {
    return new StatusAndServiceId(201, UUID.randomUUID().toString());
  }

  static int putRegistrationHeartBeatHttpRequest(String uri, String serviceId) {
    return 200;
  }

  static int getPermissionAccessHttpRequest(String assetId, String cookie) {
    return 200;
  }

  static StatusAndAsset getAssetHttpRequest(String assetId) {
    return new StatusAndAsset(
        200,
        new Asset(
            UUID.randomUUID().toString(),
            "Alice's asset",
            UUID.randomUUID().toString(),
            Asset.Type.BESS,
            new Asset.Capabilities(10000, 1000),
            new Asset.Warranty(
                Instant.now(),
                Instant.now(),
                1000,
                100,
                0) // Stub out the warranty dates for now, as I won't have time to implement the
            // warranty endpoint
            ));
  }

  // TODO: Stricter checking on labels field
  static List<MeteringDataPoint> getMeteringForAssetHttpRequest(
      String assetId, Instant from, Instant to, List<String> labels) {
    long secondsBetweenToAndFrom = Duration.between(from, to).toSeconds();

    ImmutableList.Builder<MeteringDataPoint> resultBuilder = ImmutableList.builder();
    for (long i = 0; i < secondsBetweenToAndFrom; i++) {
      resultBuilder.add(generateMeteringDatapoint(from, i));
    }

    return resultBuilder.build();
  }

  private static MeteringDataPoint generateMeteringDatapoint(Instant startTimestamp, long i) {
    return new MeteringDataPoint(
        startTimestamp.plus(Duration.ofSeconds(i)), 50f, 1000, 5f, 24f, 1.5f * i, 1.5f * i);
  }
}
