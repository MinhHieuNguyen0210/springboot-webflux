package com.springboot.webflux.service;

import com.springboot.webflux.dto.FriendDto;
import com.springboot.webflux.dto.ReceiveUpdateDto;
import com.springboot.webflux.entity.UserRelationship;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserRelationShipService {

    Mono<UserRelationship> createUserRelationShip(UserRelationship userRelationship);

//    Mono<UserRelationship> update(Long id, UserRelationship userRelationship);

    Mono<UserRelationship> findByUserFirstIdAndUserSecondId(Integer userFirstId, Integer userSecondId);

    Flux<UserRelationship> findAll();

    Mono<FriendDto.Response> addFriends(FriendDto.Request request);

    Mono<FriendDto.Response> subscribeToUpdate(FriendDto.Request request);

    Mono<FriendDto.Response> blockFriend(FriendDto.Request request);

    Mono<UserRelationship> connectByRelationType(List<String> userEmails, Integer relationType);

    Mono<ReceiveUpdateDto.Response> getFriendsListCanReceiveUpdate(ReceiveUpdateDto.Request request);
}
