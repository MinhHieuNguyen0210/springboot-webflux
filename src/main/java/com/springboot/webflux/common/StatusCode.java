package com.springboot.webflux.common;

import lombok.Getter;

@Getter
public enum StatusCode {

    SUCCESS(200, true);

    private final Integer status;

    private final Boolean success;

    StatusCode(Integer status, Boolean success) {
        this.status = status;
        this.success = success;
    }
}
