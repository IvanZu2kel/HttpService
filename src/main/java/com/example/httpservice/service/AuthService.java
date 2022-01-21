package com.example.httpservice.service;

import com.example.httpservice.api.request.RegisterRequest;
import com.example.httpservice.api.response.AuthData;
import com.example.httpservice.api.response.DataResponse;
import com.example.httpservice.exceptions.AuthenticationException;
import com.example.httpservice.exceptions.InvalidInputFormat;
import com.example.httpservice.exceptions.PersonExistException;
import com.example.httpservice.model.Person;
import com.example.httpservice.model.enums.Role;
import com.example.httpservice.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PersonRepository personRepository;

    public DataResponse<AuthData> registration(RegisterRequest registerRequest) throws PersonExistException, InvalidInputFormat {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        Optional<Person> byEmail = personRepository.findByEmail(registerRequest.getEmail());
        Person savedPerson;
        if (byEmail.isPresent()) {
            throw new PersonExistException("Person is exist");
        }
        if (registerRequest.getEmail().matches("^(.+)@(.+)$") && registerRequest.getPassword().length() >= 4) {
            Person person = new Person()
                    .setEmail(registerRequest.getEmail())
                    .setPassword(passwordEncoder.encode(registerRequest.getPassword()))
                    .setRegDate(Instant.from(Instant.now().atZone(ZoneId.systemDefault())))
                    .setIsDeleted(0)
                    .setRole(Role.USER);
            savedPerson = personRepository.save(person);
            return getDataResponse(savedPerson);
        }
        throw new InvalidInputFormat("Не верный формат ввода email или пароля");
    }

    public DataResponse<AuthData> login(RegisterRequest registerRequest) throws AuthenticationException {
        Optional<Person> person = personRepository.findByEmail(registerRequest.getEmail());
        if (person.isPresent()) {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            Person user = getUser(auth);
            return getDataResponse(user);
        }
        throw new AuthenticationException();
    }

    private DataResponse<AuthData> getDataResponse(Person person) {
        return new DataResponse<AuthData>()
                .setTimestamp(Instant.from(Instant.now().atZone(ZoneId.systemDefault())))
                .setData(getAuthData(person));
    }

    private AuthData getAuthData(Person person) {
        return new AuthData()
                .setId(person.getId())
                .setEmail(person.getEmail());
    }

    private Person getUser(Authentication auth) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        return personRepository
                .findByEmail(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));
    }
}
