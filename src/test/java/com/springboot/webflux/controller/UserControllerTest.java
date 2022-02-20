package com.springboot.webflux.controller;

import com.springboot.webflux.dto.CommonFriendDto;
import com.springboot.webflux.dto.GetFriendsListDto;
import com.springboot.webflux.entity.User;
import com.springboot.webflux.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private UserRepository userRepository;

    @Test
    void getAllUser() {
        List<User> userList = Arrays.asList(new User(1, "a@gmail.com"), new User(2, "b@gmail.com"));

        Mockito.when(userRepository.findAll()).thenReturn(userList);

        webTestClient.get()
                .uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.users[0].id").isEqualTo(1)
                .jsonPath("$.users[0].email").isEqualTo("a@gmail.com")
                .jsonPath("$.users[1].id").isEqualTo(2)
                .jsonPath("$.users[1].email").isEqualTo("b@gmail.com");
    }

    @Test
    void insert() {
        User bodyData = User.builder().id(10).email("test@gmail.com").build();
        Mockito.when(userRepository.save(any())).thenReturn(User.builder().id(10).email("test@gmail.com").build());

        webTestClient.post()
                .uri("/api/v1/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(bodyData), User.class)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.user.id").isEqualTo(10)
                .jsonPath("$.user.email").isEqualTo("test@gmail.com");
    }

    @Test
    void getFriendList() {
        GetFriendsListDto.Request body = GetFriendsListDto.Request.builder().email("test@gmail.com").build();
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User(1, "test@gmail.com")));
        Mockito.when(userRepository.getFriendsListById(any())).thenReturn(Arrays.asList("foo@gmail.com", "bar@gmail.com"));

        webTestClient.post()
                .uri("/api/v1/user/friends-list")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(body), GetFriendsListDto.Request.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.count").isEqualTo(2);
    }

    @Test
    void getCommonFriend() {
        CommonFriendDto.Request body = CommonFriendDto.Request.builder().friends(Arrays.asList("userFirst@gmail.com", "userSecond@gmail.com")).build();

        Mockito.when(userRepository.findByEmail(body.getFriends().get(0))).thenReturn(Optional.of(new User(1, "userFirst@gmail.com")));
        Mockito.when(userRepository.getFriendsListById(1)).thenReturn(Arrays.asList("foo@gmail.com", "bar@gmail.com"));

        Mockito.when(userRepository.findByEmail(body.getFriends().get(1))).thenReturn(Optional.of(new User(2, "userSecond@gmail.com")));
        Mockito.when(userRepository.getFriendsListById(2)).thenReturn(Arrays.asList("foo@gmail.com", "bar@gmail.com", "zoo@gmail.com"));

        webTestClient.post()
                .uri("/api/v1/user/common-friends")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(body), CommonFriendDto.Request.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.count").isEqualTo(2);
    }

}
