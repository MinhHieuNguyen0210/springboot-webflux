package com.springboot.webflux.dto;

import com.springboot.webflux.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public interface LoadAllUserDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    class Response {

        private Boolean success;
        private List<User> users;

    }

}
