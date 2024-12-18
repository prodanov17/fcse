package mk.finki.ukim.mk.lab2.service.impl;

import mk.finki.ukim.mk.lab2.model.User;
import mk.finki.ukim.mk.lab2.model.enums.Role;
import mk.finki.ukim.mk.lab2.repository.prodRepo.UserRepository;
import mk.finki.ukim.mk.lab2.service.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(String username, String password, String repeatPassword, Role role) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new RuntimeException("Username and password are required");
        }

        if (!password.equals(repeatPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        if (this.userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }

        User user = new User(username, passwordEncoder.encode(password), role);

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }


}
