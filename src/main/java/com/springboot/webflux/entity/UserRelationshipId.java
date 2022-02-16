package com.springboot.webflux.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRelationshipId implements Serializable {

    private Integer userFirstId;

    private Integer userSecondId;

}
