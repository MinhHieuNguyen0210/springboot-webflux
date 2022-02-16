package com.springboot.webflux.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = UserRelationship.TABLE_NAME, schema = "public")
@IdClass(UserRelationshipId.class)
public class UserRelationship implements Serializable {

    public static final String TABLE_NAME = "user_relationship";

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_first_id")
    private Integer userFirstId;

    @Id
    @Column(name = "user_second_id")
    private Integer userSecondId;

    @Column(name = "type")
    private Integer type;

}
