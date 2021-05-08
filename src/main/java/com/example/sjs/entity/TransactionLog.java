package com.example.sjs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLog {

    @Id
    private String id;

    @Column(nullable = false)
    private String business;

    @Column(nullable = false)
    private String foreignKey;
}
