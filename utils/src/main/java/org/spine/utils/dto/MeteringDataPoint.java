package org.spine.utils.dto;

import java.time.Instant;

public record MeteringDataPoint(
    Instant timestamp,
    float soc,
    int power,
    float frequency,
    float temperature,
    float totalEnergyImported,
    float totalEnergyExported) {}
