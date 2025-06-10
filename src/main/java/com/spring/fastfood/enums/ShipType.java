package com.spring.fastfood.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ShipType {
    @JsonProperty("standard")
    STANDARD,
    @JsonProperty("express")
    EXPRESS,
    @JsonProperty("economy")
    ECONOMY
}
