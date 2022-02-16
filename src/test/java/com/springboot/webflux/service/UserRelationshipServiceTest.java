package com.springboot.webflux.service;

import com.springboot.webflux.common.AppConstant;
import com.springboot.webflux.dto.FriendDto;
import com.springboot.webflux.dto.ReceiveUpdateDto;
import com.springboot.webflux.entity.User;
import com.springboot.webflux.entity.UserRelationship;
import com.springboot.webflux.repository.UserRelationShipRepository;
import com.springboot.webflux.repository.UserRepository;
import com.springboot.webflux.service.impl.UserRelationShipServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class UserRelationshipServiceTest {

    @InjectMocks
    UserRelationShipServiceImpl userRelationShipService;

    @Mock
    UserRelationShipRepository userRelationShipRepository;

    @Mock
    UserService userService;

    @Mock
    UserRepository userRepository;


    @Test
    void findAll() {
        List<UserRelationship> list = Arrays.asList(new UserRelationship(1, 2, AppConstant.RelationType.FRIEND),
                new UserRelationship(1, 3, AppConstant.RelationType.SUBSCRIBE));
        Mockito.when(userRelationShipRepository.findAll()).thenReturn(list);

        StepVerifier.create(userRelationShipService.findAll())
                .expectNextMatches(data -> data.getType() == 1)
                .expectNextMatches(data -> data.getType() == 3)
                .verifyComplete();
    }

    @Test
    void create() {
        UserRelationship request = new UserRelationship(1, 2, AppConstant.RelationType.FRIEND);
        Mockito.when(userRelationShipRepository.save(any())).thenReturn(request);

        StepVerifier.create(userRelationShipService.createUserRelationShip(request))
                .expectNextMatches(data -> data.getType() == AppConstant.RelationType.FRIEND)
                .verifyComplete();
    }

    @Test
    void findByUserFirstIdAndUserSecondId() {
        Mockito.when(userRelationShipRepository.findByUserFirstIdAndUserSecondId(any(), any())).
                thenReturn(new UserRelationship(1, 2, AppConstant.RelationType.FRIEND));
        StepVerifier.create(userRelationShipService.findByUserFirstIdAndUserSecondId(1, 2))
                .expectNextMatches(data -> data.getType() == AppConstant.RelationType.FRIEND)
                .verifyComplete();
    }

    @Test
    void connectByRelationType() {
        List<String> userEmail = Arrays.asList("a@gmail.com", "b@gmail.com");
        Mockito.when(userService.findByEmail(userEmail.get(0))).thenReturn(Mono.just(new User(10, "a@gmail.com")));
        Mockito.when(userService.findByEmail(userEmail.get(1))).thenReturn(Mono.just(new User(11, "b@gmail.com")));
        Mockito.when(userRelationShipRepository.save(any())).thenReturn(new UserRelationship(10, 11, AppConstant.RelationType.FRIEND));

        Predicate<UserRelationship> p = relationship -> {
            return relationship.getUserFirstId() == 10 && relationship.getUserSecondId() == 11 && relationship.getType() == AppConstant.RelationType.FRIEND;
        };
        StepVerifier.create(userRelationShipService.connectByRelationType(userEmail, AppConstant.RelationType.FRIEND))
                .expectNextMatches(p)
                .verifyComplete();
    }

    @Test
    void addFriend() {
        List<String> userEmails = Arrays.asList("a@gmail.com", "b@gmail.com");
        FriendDto.Request request = FriendDto.Request.builder().friends(userEmails).build();
        Mockito.when(userService.findByEmail(userEmails.get(0))).thenReturn(Mono.just(new User(10, "a@gmail.com")));
        Mockito.when(userService.findByEmail(userEmails.get(1))).thenReturn(Mono.just(new User(11, "b@gmail.com")));

        Mockito.when(userRelationShipRepository.save(UserRelationship.builder().userFirstId(10).userSecondId(11).type(1).build()))
                .thenReturn(new UserRelationship(10, 11, AppConstant.RelationType.FRIEND));

        Mockito.when(userRelationShipRepository.save(UserRelationship.builder().userFirstId(11).userSecondId(10).type(1).build()))
                .thenReturn(new UserRelationship(11, 10, AppConstant.RelationType.FRIEND));

        StepVerifier.create(userRelationShipService.addFriends(request))
                .expectNextMatches(data -> data.getRelationships().size() == 2)
                .verifyComplete();
    }

    @Test
    void subscribeToUpdate() {
        List<String> userEmails = Arrays.asList("a@gmail.com", "b@gmail.com");
        FriendDto.Request request = FriendDto.Request.builder().friends(userEmails).build();
        Mockito.when(userService.findByEmail(userEmails.get(0))).thenReturn(Mono.just(new User(10, "a@gmail.com")));
        Mockito.when(userService.findByEmail(userEmails.get(1))).thenReturn(Mono.just(new User(11, "b@gmail.com")));
        Mockito.when(userRelationShipRepository.save(any())).thenReturn(new UserRelationship(10, 11, AppConstant.RelationType.SUBSCRIBE));

        StepVerifier.create(userRelationShipService.subscribeToUpdate(request))
                .expectNextMatches(data -> data.getRelationships().get(0).getType() == AppConstant.RelationType.SUBSCRIBE)
                .verifyComplete();
    }

    @Test
    void blockFriend() {
        List<String> userEmails = Arrays.asList("a@gmail.com", "b@gmail.com");
        FriendDto.Request request = FriendDto.Request.builder().friends(userEmails).build();
        Mockito.when(userService.findByEmail(userEmails.get(0))).thenReturn(Mono.just(new User(10, "a@gmail.com")));
        Mockito.when(userService.findByEmail(userEmails.get(1))).thenReturn(Mono.just(new User(11, "b@gmail.com")));
        Mockito.when(userRelationShipRepository.findByUserFirstIdAndUserSecondId(any(), any())).
                thenReturn(new UserRelationship(10, 11, AppConstant.RelationType.FRIEND));
        Mockito.when(userRelationShipRepository.save(any())).thenReturn(new UserRelationship(10, 11, AppConstant.RelationType.BLOCK));

        StepVerifier.create(userRelationShipService.blockFriend(request))
                .expectNextMatches(data -> data.getRelationships().get(0).getType() == AppConstant.RelationType.BLOCK)
                .verifyComplete();
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

        Predicate<ReceiveUpdateDto.Response> p = response -> {
                return response.getRecipients().get(0).equals("a@gmail.com")
                    && response.getRecipients().get(1).equals("b@gmail.com")
                    && response.getRecipients().get(2).equals("foo@gmail.com")
                    && response.getRecipients().size() == 3;
        };

        StepVerifier.create(userRelationShipService.getFriendsListCanReceiveUpdate(request))
                .expectNextMatches(p)
                .verifyComplete();
    }
}
