package com.github.gabrielsmartins.taskmanager.service;

import com.github.gabrielsmartins.taskmanager.exception.ResourceNotFoundException;
import com.github.gabrielsmartins.taskmanager.model.User;
import com.github.gabrielsmartins.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
        }

    public void save(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists.");
        }
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
    }
}
