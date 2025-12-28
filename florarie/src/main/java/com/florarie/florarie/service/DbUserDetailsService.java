package com.florarie.florarie.service;

import com.florarie.florarie.model.AppUser;
import com.florarie.florarie.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DbUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser u = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User inexistent: " + email));

        // Spring Security vrea roluri sub forma ROLE_ADMIN / ROLE_USER
        String role = "ROLE_" + u.getRole().name();

        return new org.springframework.security.core.userdetails.User(
                u.getEmail(),
                u.getPasswordHash(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
