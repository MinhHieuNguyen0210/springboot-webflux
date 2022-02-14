package com.springboot.webflux.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = Type.TABLE_NAME, schema = "public")
public class Type {

    public static final String TABLE_NAME = "type";

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "type_name")
    private String typeName;

}
