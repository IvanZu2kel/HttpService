package com.example.httpservice.repository;

import com.example.httpservice.model.Link;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Integer> {

    @Query("select l from Link l " +
            "where lower(l.parentUrl) = :link " +
            "and l.isDeleted = 0")
    Optional<Link> findByParentLink(String link);

    @Query("select l from Link l " +
            "where l.shortUrl like %:smth " +
            "and l.isDeleted = 0")
    Optional<Link> findParentLinkBySmth(String smth);

    @Modifying
    @Transactional
    @Query(value = "update links set view =:votes where id = :id", nativeQuery = true)
    void updateViewCount(Long id, int votes);

    @Query("select l from Link l " +
            "where l.person.id = :id " +
            "and l.isDeleted = 0 " +
            "order by l.regDate desc")
    Page<Link> findAllLinkByPersonId(Long id, Pageable pageable);

    @Query("select l from Link l " +
            "where l.id = :id " +
            "and l.isDeleted = 0")
    Optional<Link> findByLinkId(Long id);

    @Query("select l from Link l " +
            "where l.id = :id " +
            "and l.isDeleted = 1")
    Optional<Link> findDeletedByLinkId(Long id);
}
