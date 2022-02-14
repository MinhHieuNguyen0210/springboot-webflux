package com.springboot.webflux.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRelationshipId implements Serializable {

    private Integer userFirstId;

    private Integer userSecondId;

}
