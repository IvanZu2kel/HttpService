package com.example.httpservice.repository;

import com.example.httpservice.model.UniqueView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniqueViewRepository extends JpaRepository<UniqueView, Long> {

    @Query("select uv from UniqueView uv " +
            "where uv.link.id = :id ")
    List<UniqueView> findAllByLinkId(Long id);
}
