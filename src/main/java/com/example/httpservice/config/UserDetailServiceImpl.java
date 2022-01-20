package com.example.httpservice.config;

import com.example.httpservice.exceptions.PersonExistException;
import com.example.httpservice.model.Person;
import com.example.httpservice.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user " + email + " not found"));
        return SecurityPerson.fromUser(person);
    }
}
