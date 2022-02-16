package com.springboot.webflux.repository;

import com.springboot.webflux.entity.UserRelationship;
import com.springboot.webflux.entity.UserRelationshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRelationShipRepository extends JpaRepository<UserRelationship, UserRelationshipId> {

    UserRelationship findByUserFirstIdAndUserSecondId(Integer userFirstId, Integer userSecondId);

    @Query(value = "SELECT ur.user_second_id FROM user_relationship ur WHERE ur.user_first_id = :userFirstId", nativeQuery = true)
    List<Integer> getIdUserSecondByUserFirst(@Param(value = "userFirstId") Integer userFirstId);

    @Query(value = "SELECT u.email FROM user_relationship ur join \"user\" u on ur.user_second_id = u.id WHERE user_first_id = :userFirstId", nativeQuery = true)
    List<String> getEmailUserSecondByUserFirst(@Param(value = "userFirstId") Integer userFirstId);

}
