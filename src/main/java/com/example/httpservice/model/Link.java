package com.example.httpservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

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

    @Column(name = "view")
    private int view;

    @Column(name = "is_deleted")
    private int isDeleted;

    @Column(name = "reg_date")
    private Instant regDate;

    @Column(name = "life_for_date")
    private Instant lifeByDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "link")
    private List<UniqueView> uniqueVies;
}
