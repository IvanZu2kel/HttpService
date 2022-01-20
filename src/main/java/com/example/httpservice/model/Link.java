package com.example.httpservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.Instant;

@Setter
@Getter
@Entity
@Accessors(chain = true)
@Table(name = "links")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "parent_url")
    private String parentUrl;

    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "votes")
    private int votes;

    @Column(name = "is_deleted")
    private int isDeleted;

    @Column(name = "reg_date")
    private Instant regDate;

    @Column(name = "life_for_date")
    private Instant lifeForkDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    private Person person;
}
