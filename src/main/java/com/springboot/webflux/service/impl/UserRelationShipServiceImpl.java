package com.springboot.webflux.service.impl;

import com.springboot.webflux.common.AppConstant;
import com.springboot.webflux.dto.FriendDto;
import com.springboot.webflux.dto.ReceiveUpdateDto;
import com.springboot.webflux.entity.User;
import com.springboot.webflux.entity.UserRelationship;
import com.springboot.webflux.repository.UserRelationShipRepository;
import com.springboot.webflux.repository.UserRepository;
import com.springboot.webflux.service.UserRelationShipService;
import com.springboot.webflux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserRelationShipServiceImpl implements UserRelationShipService {

    @Autowired
    private UserRelationShipRepository userRelationShipRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<UserRelationship> createUserRelationShip(UserRelationship request) {
        return Mono.just(userRelationShipRepository.save(request));
    }

    @Override
    public Mono<UserRelationship> findByUserFirstIdAndUserSecondId(Integer userFirstId, Integer userSecondId) {
        return Mono.justOrEmpty(userRelationShipRepository.findByUserFirstIdAndUserSecondId(userFirstId, userSecondId)).
                switchIfEmpty(Mono.error(RuntimeException::new));
    }

    @Override
    public Flux<UserRelationship> findAll() {
        return Flux.fromIterable(userRelationShipRepository.findAll());
    }


    @Override
    public Mono<FriendDto.Response> addFriends(FriendDto.Request request) {
        Mono<UserRelationship> relationFirstUser = connectByRelationType(request.getFriends(), AppConstant.RelationType.FRIEND);
        Collections.swap(request.getFriends(), 0, 1);
        Mono<UserRelationship> relationSecondUser = connectByRelationType(request.getFriends(), AppConstant.RelationType.FRIEND);

        List<UserRelationship> userRelationshipList = new ArrayList<>();
        relationFirstUser.subscribe(data -> userRelationshipList.add(data));
        relationSecondUser.subscribe(data -> userRelationshipList.add(data));

        Mono<List<UserRelationship>> userMono = Flux.fromIterable(userRelationshipList).collectList();

        return userMono.flatMap(data -> Mono.just(FriendDto.Response.builder().success(true).relationships(data).build()));
    }

    @Override
    public Mono<FriendDto.Response> subscribeToUpdate(FriendDto.Request request) {
        Mono<UserRelationship> relationFirstUser = connectByRelationType(request.getFriends(), AppConstant.RelationType.SUBSCRIBE);
        List<UserRelationship> userRelationshipList = new ArrayList<>();
        relationFirstUser.subscribe(data -> userRelationshipList.add(data));
        Mono<List<UserRelationship>> userMono = Flux.fromIterable(userRelationshipList).collectList();
        return userMono.flatMap(data -> Mono.just(FriendDto.Response.builder().success(true).relationships(data).build()));
    }

    @Override
    public Mono<FriendDto.Response> blockFriend(FriendDto.Request request) {
        UserRelationship relationship = new UserRelationship();
        userService.findByEmail(request.getFriends().get(0))
                .map(User::getId)
                .subscribe(id -> relationship.setUserFirstId(id));

        userService.findByEmail(request.getFriends().get(1))
                .map(User::getId)
                .subscribe(id -> relationship.setUserSecondId(id));

        return findByUserFirstIdAndUserSecondId(relationship.getUserFirstId(), relationship.getUserSecondId()) // get type relation of 2
                .flatMap(ur -> {
                    if (AppConstant.RelationType.FRIEND == ur.getType()) {
                        List<UserRelationship> userRelationshipList = new ArrayList<>();
                        connectByRelationType(request.getFriends(), AppConstant.RelationType.BLOCK).subscribe(data -> userRelationshipList.add(data));
                        return Flux.fromIterable(userRelationshipList).collectList()
                                .flatMap(data -> Mono.just(FriendDto.Response.builder().success(true).relationships(data).build()));
                    }
                    return Mono.just(FriendDto.Response.builder().success(true).relationships(null).build());
                });
    }


    @Override
    public Mono<UserRelationship> connectByRelationType(List<String> userEmails, Integer relationType) {
        UserRelationship relationship = new UserRelationship();
        userService.findByEmail(userEmails.get(0))
                .map(User::getId)
                .subscribe(id -> relationship.setUserFirstId(id));

        userService.findByEmail(userEmails.get(1))
                .map(User::getId)
                .subscribe(id -> relationship.setUserSecondId(id));

        switch (relationType) {
            case AppConstant.RelationType.FRIEND:
                relationship.setType(AppConstant.RelationType.FRIEND);
                break;
            case AppConstant.RelationType.BLOCK:
                relationship.setType(AppConstant.RelationType.BLOCK);
                break;
            case AppConstant.RelationType.SUBSCRIBE:
                relationship.setType(AppConstant.RelationType.SUBSCRIBE);
                break;
        }

        return createUserRelationShip(relationship);
    }

    @Override
    public Mono<ReceiveUpdateDto.Response> getFriendsListCanReceiveUpdate(ReceiveUpdateDto.Request request) {

        List<String> userEmailResult = new ArrayList<>();

        Mono<List<User>> users = Flux.fromIterable(userRepository.findAll()).collectList();

        Mono<List<Integer>> listIdUserSecond = userService.findByEmail(request.getSender())
                .map(User::getId)
                .map(id -> userRelationShipRepository.getIdUserSecondByUserFirst(id));

        Mono<Integer> userFirst = userService.findByEmail(request.getSender()).map(User::getId);

        return Mono.zip(userFirst, listIdUserSecond, users)
                .flatMap(data -> {
                    for (Integer i : data.getT2()) {
                        findByUserFirstIdAndUserSecondId(data.getT1(), i)
                                .map(UserRelationship::getType)
                                .subscribe(type -> {
                                    if (type == AppConstant.RelationType.FRIEND || type == AppConstant.RelationType.SUBSCRIBE) {
                                        userService.findById(i)
                                                .map(User::getEmail)
                                                .subscribe(email -> userEmailResult.add(email));
                                    }
                                });
                    }
                    for (User user : data.getT3()) {
                        if (request.getText().contains(user.getEmail())) {
                            userEmailResult.add(user.getEmail());
                        }
                    }
                    return Mono.just(userEmailResult);
                })
                .flatMap(data -> Flux.fromIterable(data).collectList()
                        .flatMap(list -> Mono.just(ReceiveUpdateDto.Response.builder().recipients(list).success(true).build())));
    }
}
