package com.example.httpservice.repository;

import com.example.httpservice.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface LinkRepository extends JpaRepository<Link, Integer> {

    @Query("select l from Link l " +
            "where l.person.id = :id " +
            "and l.isDeleted = 0")
    Set<Link> findByPersonId(Long id);

    @Query("select l from Link l " +
            "where lower(l.parentUrl) = :link ")
    Optional<Link> findByParentLink(String link);
}
