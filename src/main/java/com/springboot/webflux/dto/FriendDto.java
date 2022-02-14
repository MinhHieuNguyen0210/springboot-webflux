package com.springboot.webflux.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.webflux.entity.UserRelationship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public interface FriendDto {

    String ERROR = "";

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    class Request {

        @JsonProperty(value = "friends")
        private List<String> friends;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    class Response {

        private Boolean success;

        List<UserRelationship> relationships;

    }


}
