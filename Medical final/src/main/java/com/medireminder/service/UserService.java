package com.medireminder.service;

import com.medireminder.entity.User;
import com.medireminder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean validateCredentials(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElse(null);
        return user != null && user.getPassword().equals(password);
    }
}
