package com.springboot.webflux.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public interface CommonFriendDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    class Request{

        @JsonProperty(value = "friends")
        private List<String> friends;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    class Response{

        private Boolean success;
        private List<String> friends;
        private Integer count;
    }

}
