package mk.finki.ukim.mk.lab2.service;


import mk.finki.ukim.mk.lab2.model.User;
import mk.finki.ukim.mk.lab2.model.enums.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User register(String username, String password, String repeatPassword, Role role);
}

