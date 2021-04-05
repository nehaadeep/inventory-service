package com.mywarehouse.inventory.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer seqId;

    @Column(name = "article_id")
    private String articleId;

    @Column(name = "name")
    private String name;

    @Column(name = "stock")
    private Integer stock;
}
