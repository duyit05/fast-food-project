package com.spring.fastfood.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum VoucherType {
    @JsonProperty("expired")
    EXPIRED,
    @JsonProperty("valid")
    VALID
}
