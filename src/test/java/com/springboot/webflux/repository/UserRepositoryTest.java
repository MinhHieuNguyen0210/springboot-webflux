package com.springboot.webflux.repository;

import com.springboot.webflux.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
//@Sql(scripts = "classpath:data-test.sql")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findAll() {
        User user1 = new User(1, "test@gmail.com");
        entityManager.persist(user1);
        Iterable<User> users = userRepository.findAll();
        Assertions.assertThat(users).hasSize(1).contains(user1);
    }

    @Test
    public void findById() {
        User user1 = new User(1, "foo@gmail.com");
        entityManager.persist(user1);
        User user2 = new User(2, "bar@gmail.com");
        entityManager.persist(user2);
        User foundUser = userRepository.findById(1).orElse(new User());
        Assertions.assertThat(foundUser).isEqualTo(user1);
    }

    @Test
    public void findByEmail() {
        User user1 = new User(1, "foo@gmail.com");
        entityManager.persist(user1);
        User user2 = new User(2, "bar@gmail.com");
        entityManager.persist(user2);
        User foundUser = userRepository.findByEmail("foo@gmail.com").orElse(new User());
        Assertions.assertThat(foundUser).isEqualTo(user1);
    }

    @Test
    public void save() {
        User user = userRepository.save(new User(10, "testuser@gmail.com"));
        Assertions.assertThat(user).hasFieldOrPropertyWithValue("id", 10);
        Assertions.assertThat(user).hasFieldOrPropertyWithValue("email", "testuser@gmail.com");
    }

    @Test
    public void updateById() {
        User user1 = new User(1, "foo@gmail.com");
        entityManager.persist(user1);
        User user2 = new User(2, "bar@gmail.com");
        entityManager.persist(user2);

        User userUpdate = new User(2, "emailUpdate@gmail.com");

        User checkUserUpdate = userRepository.findById(2).orElse(new User());
        checkUserUpdate.setId(userUpdate.getId());
        checkUserUpdate.setEmail(userUpdate.getEmail());
        userRepository.save(checkUserUpdate);

        User updated = userRepository.findById(user2.getId()).get();

        Assertions.assertThat(updated.getId()).isEqualTo(user2.getId());
        Assertions.assertThat(updated.getEmail()).isEqualTo(user2.getEmail());
    }

    @Test
    public void deleteById(){
        User user1 = new User(1, "foo@gmail.com");
        entityManager.persist(user1);
        User user2 = new User(2, "bar@gmail.com");
        entityManager.persist(user2);
        User user3 = new User(3, "zoo@gmail.com");
        entityManager.persist(user3);

        userRepository.deleteById(2);
        Iterable<User> users = userRepository.findAll();
        Assertions.assertThat(users).hasSize(2).contains(user1,user3);
    }

    @Test
    public void deleteAll(){
        User user1 = new User(1, "foo@gmail.com");
        entityManager.persist(user1);
        User user2 = new User(2, "bar@gmail.com");
        entityManager.persist(user2);
        User user3 = new User(3, "zoo@gmail.com");
        entityManager.persist(user3);

        userRepository.deleteAll();
        Assertions.assertThat(userRepository.findAll()).isEmpty();
    }
}
