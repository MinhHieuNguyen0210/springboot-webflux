package com.springboot.webflux.controller;

import com.springboot.webflux.dto.CommonFriendDto;
import com.springboot.webflux.dto.GetFriendsListDto;
import com.springboot.webflux.dto.LoadAllUserDto;
import com.springboot.webflux.dto.SaveOrUpdateUserDto;
import com.springboot.webflux.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public Mono<LoadAllUserDto.Response> loadAllUser() {
        return userService.loadAllUser();
    }

    @PostMapping("user/add")
    @ApiOperation(value = "API create new user")
    public Mono<SaveOrUpdateUserDto.Response> insert(@RequestBody SaveOrUpdateUserDto.Request request) {
        return userService.insert(request);
    }

    @PostMapping("/user/friends-list")
    @ApiOperation(value = "API get friends list")
    public Mono<GetFriendsListDto.Response> getFriendsList(@RequestBody GetFriendsListDto.Request request) {
        return userService.getFriendsListByEmail(request);
    }

    @PostMapping("/user/common-friends")
    @ApiOperation(value = "API get common friends")
    public Mono<CommonFriendDto.Response> getCommonFriends(@RequestBody CommonFriendDto.Request request) {
        return userService.getCommonFriend(request);
    }
}



