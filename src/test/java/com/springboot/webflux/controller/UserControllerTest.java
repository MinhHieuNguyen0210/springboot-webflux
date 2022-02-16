package com.springboot.webflux.controller;

import com.springboot.webflux.controller.UserController;
import com.springboot.webflux.dto.LoadAllUserDto;
import com.springboot.webflux.dto.SaveOrUpdateUserDto;
import com.springboot.webflux.entity.User;
import com.springboot.webflux.repository.UserRepository;
import com.springboot.webflux.service.UserService;
import com.springboot.webflux.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void getAllUser(){
        List<User> userList = Arrays.asList(new User(1,"a@gmail.com"), new User(2,"b@gmail.com"));
        Mono<LoadAllUserDto.Response> userMono = Mono.just(LoadAllUserDto.Response.builder().users(userList).success(true).build());
        Mockito.when(userService.loadAllUser()).thenReturn(userMono);

        webTestClient.get()
                .uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.success").isEqualTo(true);
    }

}
