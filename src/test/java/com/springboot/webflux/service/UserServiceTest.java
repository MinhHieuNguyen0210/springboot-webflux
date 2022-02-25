package com.springboot.webflux.service;

import com.springboot.webflux.dto.CommonFriendDto;
import com.springboot.webflux.dto.GetFriendsListDto;
import com.springboot.webflux.dto.SaveOrUpdateUserDto;
import com.springboot.webflux.entity.User;
import com.springboot.webflux.repository.UserRepository;
import com.springboot.webflux.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void save() {
        SaveOrUpdateUserDto.Request request = SaveOrUpdateUserDto.Request.builder().id(10).email("test@gmail.com").build();
        Mockito.when(userRepository.save(any())).thenReturn(User.builder().id(10).email("test@gmail.com").build());
        StepVerifier
                .create(userService.insert(request))
                .expectNextMatches(saved -> saved.getUser().getEmail().equalsIgnoreCase("test@gmail.com"))
                .verifyComplete();
    }

    @Test
    public void findAll() {
        List<User> userList = Arrays.asList(new User(1, "a@gmail.com"), new User(2, "b@gmail.com"));
        Mockito.when(userRepository.findAll()).thenReturn(userList);
        StepVerifier
                .create(userService.loadAllUser())
                .expectNextMatches(data -> data.getUsers().size() == 2)
                .verifyComplete();
    }

    @Test
    public void update() {
        SaveOrUpdateUserDto.Request request = SaveOrUpdateUserDto.Request.builder().id(10).email("testUpdate@gmail.com").build();
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(User.builder().email("test@gmail.com").id(10).build()));
        Mockito.when(userRepository.save(any())).thenReturn(User.builder().email("testUpdate@gmail.com").build());


        StepVerifier
                .create(userService.update(10, request))
                .expectNextMatches(data -> data.getUser().getEmail().equalsIgnoreCase("testUpdate@gmail.com"))
                .verifyComplete();
    }

    @Test
    public void updateError() {
        SaveOrUpdateUserDto.Request request = SaveOrUpdateUserDto.Request.builder().id(10).email("testUpdate@gmail.com").build();
        Mockito.when(userRepository.findById(any())).thenReturn(null);

        StepVerifier
                .create(userService.update(10, request))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("update exception"))
                .verify();
    }

    @Test
    public void findById() {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(User.builder().email("test@gmail.com").id(10).build()));
        StepVerifier
                .create(userService.findById(10))
                .expectNextMatches(data -> data.getId() == 10)
                .verifyComplete();
    }

    @Test
    public void findByEmail() {
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.of(User.builder().email("test@gmail.com").id(10).build()));
        StepVerifier
                .create(userService.findByEmail("test@gmail.com"))
                .expectNextMatches(data -> data.getEmail().equalsIgnoreCase("test@gmail.com"))
                .verifyComplete();
    }

    @Test
    public void getFriendsListByEmail() {
        GetFriendsListDto.Request request = GetFriendsListDto.Request.builder().email("test@gmail.com").build();
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User(1, "test@gmail.com")));
        Mockito.when(userRepository.getFriendsListById(any())).thenReturn(Arrays.asList("foo@gmail.com", "bar@gmail.com"));
        StepVerifier
                .create(userService.getFriendsListByEmail(request))
                .expectNextMatches(data -> data.getFriends().size() == 2)
                .verifyComplete();
    }

    @Test
    public void getCommonFriend() {
        List<String> userEmails = Arrays.asList("userFirst@gmail.com", "userSecond@gmail.com");
        CommonFriendDto.Request request = CommonFriendDto.Request.builder().friends(userEmails).build();

        Mockito.when(userRepository.findByEmail(userEmails.get(0))).thenReturn(Optional.of(new User(1, "userFirst@gmail.com")));
        Mockito.when(userRepository.getFriendsListById(1)).thenReturn(Arrays.asList("foo@gmail.com", "bar@gmail.com"));

        Mockito.when(userRepository.findByEmail(userEmails.get(1))).thenReturn(Optional.of(new User(2, "userSecond@gmail.com")));
        Mockito.when(userRepository.getFriendsListById(2)).thenReturn(Arrays.asList("foo@gmail.com", "bar@gmail.com", "zoo@gmail.com"));

        Predicate<CommonFriendDto.Response> p = response -> response.getFriends().get(0).equals("foo@gmail.com")
                && response.getFriends().get(1).equals("bar@gmail.com")
                && response.getCount() == 2;

        StepVerifier
                .create(userService.getCommonFriend(request))
                .expectNextMatches(p)
                .verifyComplete();
    }

    @Test
    public void deleteById() {
        Mockito.doNothing().when(userRepository).deleteById(any());
        userService.deleteById(1);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1);

        StepVerifier
                .create(userService.deleteById(1))
                .expectComplete();
    }

    @Test
    public void deleteAll() {
        Mockito.doNothing().when(userRepository).deleteAll();
        userService.deleteAll();
        Mockito.verify(userRepository, Mockito.times(1)).deleteAll();
    }

}
