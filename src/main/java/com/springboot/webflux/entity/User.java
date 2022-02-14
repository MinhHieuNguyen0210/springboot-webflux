package com.springboot.webflux.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = User.TABLE_NAME, schema = "public")
public class User {

    public static final String TABLE_NAME = "user";

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

}