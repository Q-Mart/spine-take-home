package org.spine.utils.dto;

import java.util.Collection;

public record RegistrationDetails(
    String serviceType, String location, Collection<String> endpoints, String description) {}
