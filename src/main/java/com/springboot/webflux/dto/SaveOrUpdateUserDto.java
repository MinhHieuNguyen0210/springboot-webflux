package com.springboot.webflux.dto;

import com.springboot.webflux.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface SaveOrUpdateUserDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    class Request {

        private Integer id;
        private String email;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    class Response {

        private Boolean success;
        private User user;

    }

}
