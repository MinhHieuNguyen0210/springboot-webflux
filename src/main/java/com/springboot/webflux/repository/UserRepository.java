package com.springboot.webflux.repository;

import com.springboot.webflux.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT u.email FROM \"user\" u INNER JOIN user_relationship ur ON u.id = ur.user_second_id WHERE ur.user_first_id = :id and ur.type = 1", nativeQuery = true)
    List<String> getFriendsListById(@Param("id") Integer id);

}
