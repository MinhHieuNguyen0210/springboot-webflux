package com.springboot.webflux.repository;

import com.springboot.webflux.common.AppConstant;
import com.springboot.webflux.entity.User;
import com.springboot.webflux.entity.UserRelationship;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRelationshipRepositoryTest {

    @Autowired
    private UserRelationShipRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findAll(){
        UserRelationship relationship1 = new UserRelationship(1,2, AppConstant.RelationType.FRIEND);
        entityManager.persist(relationship1);
        UserRelationship relationship2 = new UserRelationship(1,3, AppConstant.RelationType.SUBSCRIBE);
        entityManager.persist(relationship2);
        UserRelationship relationship3 = new UserRelationship(1,4, AppConstant.RelationType.BLOCK);
        entityManager.persist(relationship3);

        Iterable<UserRelationship> userRelationships = repository.findAll();
        Assertions.assertThat(userRelationships).hasSize(3).containsAll(userRelationships);
    }

    @Test
    public void save(){
        UserRelationship relationship = repository.save(new UserRelationship(1,2, AppConstant.RelationType.FRIEND));
        Assertions.assertThat(relationship).hasFieldOrPropertyWithValue("userFirstId",1);
        Assertions.assertThat(relationship).hasFieldOrPropertyWithValue("userSecondId",2);
        Assertions.assertThat(relationship).hasFieldOrPropertyWithValue("type", AppConstant.RelationType.FRIEND);
    }

    @Test
    public void findByUserFirstIdAndUserSecondId(){

    }
}
