package com.springboot.webflux.controller;

import com.springboot.webflux.common.AppConstant;
import com.springboot.webflux.dto.FriendDto;
import com.springboot.webflux.dto.ReceiveUpdateDto;
import com.springboot.webflux.entity.User;
import com.springboot.webflux.entity.UserRelationship;
import com.springboot.webflux.repository.UserRelationShipRepository;
import com.springboot.webflux.repository.UserRepository;
import com.springboot.webflux.service.UserService;
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

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRelationshipControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    UserRelationShipRepository userRelationShipRepository;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Test
    void addFriends() {
        List<String> userEmails = Arrays.asList("a@gmail.com", "b@gmail.com");
        FriendDto.Request request = FriendDto.Request.builder().friends(userEmails).build();

        Mockito.when(userService.findByEmail(userEmails.get(0))).thenReturn(Mono.just(new User(10, "a@gmail.com")));
        Mockito.when(userService.findByEmail(userEmails.get(1))).thenReturn(Mono.just(new User(11, "b@gmail.com")));

        Mockito.when(userRelationShipRepository.save(UserRelationship.builder().userFirstId(10).userSecondId(11).type(1).build()))
                .thenReturn(new UserRelationship(10, 11, AppConstant.RelationType.FRIEND));

        Mockito.when(userRelationShipRepository.save(UserRelationship.builder().userFirstId(11).userSecondId(10).type(1).build()))
                .thenReturn(new UserRelationship(11, 10, AppConstant.RelationType.FRIEND));

        webTestClient.post()
                .uri("/api/v1/user-relationship/connect")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), FriendDto.Request.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.success").isEqualTo(true);
    }

    @Test
    void subscribeFriend() {
        List<String> userEmails = Arrays.asList("a@gmail.com", "b@gmail.com");
        FriendDto.Request request = FriendDto.Request.builder().friends(userEmails).build();
        Mockito.when(userService.findByEmail(userEmails.get(0))).thenReturn(Mono.just(new User(10, "a@gmail.com")));
        Mockito.when(userService.findByEmail(userEmails.get(1))).thenReturn(Mono.just(new User(11, "b@gmail.com")));
        Mockito.when(userRelationShipRepository.save(any())).thenReturn(new UserRelationship(10, 11, AppConstant.RelationType.SUBSCRIBE));

        webTestClient.post()
                .uri("/api/v1/user-relationship/subscribe")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), FriendDto.Request.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.relationships[0].type").isEqualTo(3);
    }

    @Test
    void findBy2Id() {
        Integer idUserFirst = 1;
        Integer idUserSecond = 2;

        Mockito.when(userRelationShipRepository.findByUserFirstIdAndUserSecondId(any(), any())).
                thenReturn(new UserRelationship(1, 2, AppConstant.RelationType.FRIEND));

        webTestClient.get()
                .uri("/api/v1/user-relationship/" + idUserFirst + "/" + idUserSecond)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.type").isEqualTo(AppConstant.RelationType.FRIEND);
    }

    @Test
    void blockFriend() {
        FriendDto.Request request = FriendDto.Request.builder().friends(Arrays.asList("a@gmail.com", "b@gmail.com")).build();
        Mockito.when(userService.findByEmail(request.getFriends().get(0))).thenReturn(Mono.just(new User(10, "a@gmail.com")));
        Mockito.when(userService.findByEmail(request.getFriends().get(1))).thenReturn(Mono.just(new User(11, "b@gmail.com")));
        Mockito.when(userRelationShipRepository.findByUserFirstIdAndUserSecondId(any(), any())).
                thenReturn(new UserRelationship(10, 11, AppConstant.RelationType.FRIEND));
        Mockito.when(userRelationShipRepository.save(any())).thenReturn(new UserRelationship(10, 11, AppConstant.RelationType.BLOCK));

        webTestClient.post()
                .uri("/api/v1/user-relationship/block")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), FriendDto.Request.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.relationships[0].type").isEqualTo(AppConstant.RelationType.BLOCK);
    }

    @Test
    void getFriendsListCanReceiveUpdate() {
        ReceiveUpdateDto.Request request = ReceiveUpdateDto.Request.builder().sender("sender@gmail.com").text("hello! foo@gmail.com").build();
        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(
                new User(10, "a@gmail.com"),
                new User(11, "b@gmail.com"),
                new User(12, "c@gmail.com"),
                new User(13, "foo@gmail.com")));

        Mockito.when(userService.findByEmail("sender@gmail.com"))
                .thenReturn(Mono.just(new User(1, "sender@gmail.com")));
        Mockito.when(userRelationShipRepository.getIdUserSecondByUserFirst(1)).thenReturn(Arrays.asList(10, 11, 12));

        Mockito.when(userRelationShipRepository.findByUserFirstIdAndUserSecondId(1, 10)).
                thenReturn(UserRelationship.builder().type(AppConstant.RelationType.FRIEND).build());
        Mockito.when(userRelationShipRepository.findByUserFirstIdAndUserSecondId(1, 11)).
                thenReturn(UserRelationship.builder().type(AppConstant.RelationType.FRIEND).build());
        Mockito.when(userRelationShipRepository.findByUserFirstIdAndUserSecondId(1, 12)).
                thenReturn(UserRelationship.builder().type(AppConstant.RelationType.BLOCK).build());

        Mockito.when(userService.findById(10)).thenReturn(Mono.just(new User(10, "a@gmail.com")));
        Mockito.when(userService.findById(11)).thenReturn(Mono.just(new User(11, "b@gmail.com")));

        webTestClient.post()
                .uri("/api/v1/user-relationship/receive-update")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), FriendDto.Request.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.recipients[0]").isEqualTo("a@gmail.com")
                .jsonPath("$.recipients[1]").isEqualTo("b@gmail.com")
                .jsonPath("$.recipients[2]").isEqualTo("foo@gmail.com");
    }

}
