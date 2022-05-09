package com.springboot.webflux.service.impl;

import com.springboot.webflux.dto.CommonFriendDto;
import com.springboot.webflux.dto.GetFriendsListDto;
import com.springboot.webflux.dto.LoadAllUserDto;
import com.springboot.webflux.dto.SaveOrUpdateUserDto;
import com.springboot.webflux.entity.User;
import com.springboot.webflux.repository.UserRepository;
import com.springboot.webflux.service.UserService;
import com.springboot.webflux.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public Mono<LoadAllUserDto.Response> loadAllUser() {
        return Flux.fromIterable(userRepository.findAll()).collectList()
                .flatMap((data -> Mono.just(LoadAllUserDto.Response.builder()
                        .success(true).users(data).build())));
    }

    @Override
    public Mono<SaveOrUpdateUserDto.Response> insert(SaveOrUpdateUserDto.Request dto) {
        Mono<User> user = Mono.just(userRepository
                .save(User.builder().id(dto.getId()).email(dto.getEmail()).build()));
        return user.flatMap(data -> Mono.just(SaveOrUpdateUserDto.Response
                .builder()
                .success(true)
                .user(data)
                .build()));
    }

    @Override
    public Mono<SaveOrUpdateUserDto.Response> update(Integer id, SaveOrUpdateUserDto.Request dto) {
        User entity = User.builder().id(dto.getId()).email(dto.getEmail()).build();
        return findById(id).filter(i -> null != i).flatMap(i -> {
            Mono<User> user = Mono.just(userRepository.save(entity));
            return user.flatMap(data -> Mono.just(SaveOrUpdateUserDto.Response.builder().success(true).user(data).build()));
        }).doOnError(e -> Mono.error(new RuntimeException("update exception")));
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        userRepository.deleteById(id);
        return Mono.empty();
    }

    @Override
    public Mono<Void> deleteAll() {
        userRepository.deleteAll();
        return Mono.empty();
    }

    @Override
    public Mono<User> findById(Integer id) {
        return Mono.justOrEmpty(userRepository.findById(id))
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return Mono.just(userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("not found")));
    }

    @Override
    public Mono<GetFriendsListDto.Response> getFriendsListByEmail(GetFriendsListDto.Request request) {
        Mono<List<String>> emailFriendsList = findByEmail(request.getEmail())
                .map(User::getId)
                .map(id -> userRepository.getFriendsListById(id));

        return emailFriendsList.flatMap(data -> Mono.just(GetFriendsListDto.Response.builder().friends(data).success(true).count(data.size()).build()));
    }

    @Override
    public Mono<CommonFriendDto.Response> getCommonFriend(CommonFriendDto.Request request) {
        // friends list of first user
        Mono<List<String>> mono1 = getFriendsListByEmail(GetFriendsListDto.Request.builder().email(request.getFriends().get(0)).build())
                .map(GetFriendsListDto.Response::getFriends);
        // friends list of second user
        Mono<List<String>> mono2 = getFriendsListByEmail(GetFriendsListDto.Request.builder().email(request.getFriends().get(1)).build())
                .map(GetFriendsListDto.Response::getFriends);
        List<String> commonFriends = new ArrayList<>();

        return Mono.zip(mono1, mono2).flatMap((data) -> {
            Set<String> map = new HashSet<>();
            data.getT1().stream().forEach(item -> map.add(item));
            data.getT2().stream().filter(item -> map.contains(item)).forEach(a -> commonFriends.add(a));
            return Mono.just(commonFriends);
        }).flatMap(cfs -> Flux.fromIterable(cfs).collectList()).flatMap(data
                -> Mono.just(CommonFriendDto.Response.builder().friends(data).success(true).count(data.size()).build()));
    }
}
