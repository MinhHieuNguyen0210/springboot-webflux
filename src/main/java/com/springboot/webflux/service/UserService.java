package com.springboot.webflux.service;

import com.springboot.webflux.dto.CommonFriendDto;
import com.springboot.webflux.dto.GetFriendsListDto;
import com.springboot.webflux.dto.LoadAllUserDto;
import com.springboot.webflux.dto.SaveOrUpdateUserDto;
import com.springboot.webflux.entity.User;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<LoadAllUserDto.Response> loadAllUser();

    Mono<SaveOrUpdateUserDto.Response> insert(SaveOrUpdateUserDto.Request userDto);

    Mono<SaveOrUpdateUserDto.Response> update(Integer id, SaveOrUpdateUserDto.Request userDto);

    Mono<Void> deleteById(Integer id);

    Mono<User> findById(Integer id);

    Mono<User> findByEmail(String email);

    Mono<GetFriendsListDto.Response> getFriendsListByEmail(GetFriendsListDto.Request request);

    Mono<CommonFriendDto.Response> getCommonFriend(CommonFriendDto.Request request);

    Mono<Void> deleteAll();

}
