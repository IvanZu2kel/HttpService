package com.example.httpservice.config;

import com.example.httpservice.model.Person;
import com.example.httpservice.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final PersonRepository personRepository;

    @Autowired
    public UserDetailServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<Person> person = personRepository.findByEmail(email);
        return person.orElse(null);
    }
}
