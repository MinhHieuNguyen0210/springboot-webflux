package com.springboot.webflux.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public interface GetFriendsListDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    class Request {

        @JsonProperty(value = "email")
        private String email;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    class Response {

        private Boolean success;
        private List<String> friends;
        private Integer count;

    }


}
