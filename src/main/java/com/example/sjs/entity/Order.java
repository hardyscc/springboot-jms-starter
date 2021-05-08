package com.example.sjs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ordr")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private Long id;

    @Column(nullable = false)
    private String header;

    @Column(nullable = false)
    private String details;
}
