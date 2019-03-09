package com.automation.task.tests.api.service.markers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MarkFeatures {

    NAME("nm"),
    CITY("city"),
    LAT("lat"),
    LNG("lng"),
    COUNTRY("cty");

    @Getter
    private String markerFeature;
}
