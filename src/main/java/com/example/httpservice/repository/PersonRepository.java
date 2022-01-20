package com.example.httpservice.repository;

import com.example.httpservice.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    @Query("select p from Person p " +
            "where p.email = :email and p.isDeleted = 0")
    Optional<Person> findByEmail(String email);
}
