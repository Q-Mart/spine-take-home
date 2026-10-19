package org.spine.utils.dto;

/**
 * At the moment, it seems a bit ridiculous to have a record containing one element. Maybe this can
 * be extended later to contain more info required for this domain?
 *
 * <p>More generally, I think we need to decide on a standard way for data to be returned from an
 * endpoint
 *
 * @param numberOfCycles
 */
public record CycleInfo(float numberOfCycles) {}
