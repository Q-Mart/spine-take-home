package org.spine.utils.dto;

import java.time.Instant;

public record Asset(
    String id, String name, String Owner, Type type, Capabilities capabilities, Warranty warranty) {
  public enum Type {
    BESS,
    PV,
    WIND;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

  /**
   * A quick google identifies the largest operating BESS sites as having capacity in the realm of
   * 4000MWh and power delivery in the range of ~100s of MegaWatts. So a float seems like something
   * that could adequately hold these values
   *
   * @param energyCapacity
   * @param powerCapacity
   */
  public record Capabilities(float energyCapacity, float powerCapacity) {}

  public record Warranty(
      Instant startDate, Instant endDate, float annualCycles, float maxSoc, float minSoc) {}
}
