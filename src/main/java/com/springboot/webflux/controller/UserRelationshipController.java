package com.springboot.webflux.controller;

import com.springboot.webflux.common.AppConstant;
import com.springboot.webflux.dto.FriendDto;
import com.springboot.webflux.dto.ReceiveUpdateDto;
import com.springboot.webflux.entity.UserRelationship;
import com.springboot.webflux.service.UserRelationShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserRelationshipController {

    @Autowired
    private UserRelationShipService userRelationShipService;

    @PostMapping("/user-relationship/connect")
    public Mono<FriendDto.Response> addFriends(@RequestBody FriendDto.Request request) {
        return userRelationShipService.addFriends(request);
    }

    @PostMapping("/user-relationship/subscribe")
    public Mono<FriendDto.Response> subscribeFriend(@RequestBody FriendDto.Request request) {
        return userRelationShipService.subscribeToUpdate(request);
    }

    @GetMapping("user-relationship/{first}/{second}")
    public Mono<UserRelationship> findBy2Id(@PathVariable("first") Integer userFirstId, @PathVariable("second") Integer userSecondId){
        return userRelationShipService.findByUserFirstIdAndUserSecondId(userFirstId, userSecondId);
    }

    @PostMapping("/user-relationship/block")
    public Mono<FriendDto.Response> blockFriend(@RequestBody FriendDto.Request request){
        return userRelationShipService.blockFriend(request);
    }

    @PostMapping("/user-relationship/receive-update")
    public Mono<ReceiveUpdateDto.Response> getFriendsListCanReceiveUpdate(@RequestBody ReceiveUpdateDto.Request request){
           return userRelationShipService.getFriendsListCanReceiveUpdate(request);
    }
}
