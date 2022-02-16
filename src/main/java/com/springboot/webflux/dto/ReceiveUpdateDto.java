package com.springboot.webflux.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public interface ReceiveUpdateDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    class Request {

        private String sender;
        private String text;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    class Response {

        List<String> recipients;
        private Boolean success;

    }

}
