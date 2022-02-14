package com.springboot.webflux.controller;

import com.springboot.webflux.dto.*;
import com.springboot.webflux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public Mono<LoadAllUserDto.Response> loadAllUser() {
        return userService.loadAllUser();
    }

    @PostMapping
    public Mono<SaveOrUpdateUserDto.Response> insertByUser(@RequestBody SaveOrUpdateUserDto.Request request) {
        return userService.insertByUser(request);
    }

    @PostMapping("/user/friends-list")
    public Mono<GetFriendsListDto.Response> getFriendsList(@RequestBody GetFriendsListDto.Request request) {
        return userService.getFriendsListByEmail(request);
    }

    @PostMapping("/user/common-friends")
    public Mono<CommonFriendDto.Response> getCommonFriends(@RequestBody CommonFriendDto.Request request){
        return userService.getCommonFriend(request);
    }

}



