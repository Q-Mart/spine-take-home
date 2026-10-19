package org.spine.analytics.chargecycles;

import static org.spine.analytics.chargecycles.Stubs.*;

import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spine.utils.dto.Asset;
import org.spine.utils.dto.CycleInfo;
import org.spine.utils.dto.MeteringDataPoint;
import org.spine.utils.exception.ApiEndpointCallException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

@EnableScheduling
@SpringBootApplication
public class Application {

  private static final Logger Logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @GetMapping("/charge-cycles/v1/{assetId}")
  public CycleInfo calculateCycles(
      @PathVariable String assetId,
      @RequestParam Instant from,
      @RequestParam Instant to,
      @CookieValue("JSESSIONID") String cookie) {

    int status = getPermissionAccessHttpRequest(assetId, cookie);

    if (status != 200) {
      // TODO: properly propagate the error here from the permissions service
      Logger.warn(
          "Unable to get permissions for asset {}. {} status code returned", assetId, status);
      throw new ApiEndpointCallException(status, "Permission call failed");
    }

    Asset asset = getAsset(assetId);

    // TODO: sort out what to do when this returns a 400 or 404 error
    List<MeteringDataPoint> dataPoints =
        getMeteringForAssetHttpRequest(assetId, from, to, List.of());

    // Get the delta values - we don't need the totals here
    // Now I'm writing the code for this calculation, I realise how inefficient it is to get ALL the
    // metering points
    // for a time window when we only need the first and last. TODO: rethink the metering API
    float energyImported =
        dataPoints.getLast().totalEnergyImported() - dataPoints.getFirst().totalEnergyImported();
    float energyExported =
        dataPoints.getLast().totalEnergyExported() - dataPoints.getFirst().totalEnergyExported();

    // Do the cycle calculation
    float cyclesCompleted =
        (energyExported + energyImported) / (2 * asset.capabilities().energyCapacity());

    return new CycleInfo(cyclesCompleted);
  }

  @ExceptionHandler(ApiEndpointCallException.class)
  ResponseEntity<String> handlePermissionAccessFailed(ApiEndpointCallException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(e.getStatusCode()));
  }

  private Asset getAsset(String assetId) {
    Stubs.StatusAndAsset statusAndAsset = getAssetHttpRequest(assetId);

    // TODO: Proper error handling
    if (statusAndAsset.status() == 200) {
      return statusAndAsset.asset();
    }

    throw new ApiEndpointCallException(statusAndAsset.status(), "Asset GET call failed");
  }
}
