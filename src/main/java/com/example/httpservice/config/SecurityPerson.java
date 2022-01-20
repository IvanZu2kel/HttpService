package com.example.httpservice.config;

import com.example.httpservice.model.Person;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class SecurityPerson implements UserDetails {
    private final String username;
    private final String password;
    private final Set<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserDetails fromUser(Person person) {
        return new org.springframework.security.core.userdetails.User(
                person.getEmail(),
                person.getPassword(),
                true,
                true,
                true,
                true,
                person.getRole().getAuthorities());
    }
}
