package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.models.dto.RegisterReq;
import ru.skypro.homework.models.dto.Role;
import ru.skypro.homework.models.dto.UserDto;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;

    private final PasswordEncoder encoder;

    private final UserService userService;

    public AuthServiceImpl(UserDetailsManager manager, UserService userService) {
        this.manager = manager;
        this.encoder = new BCryptPasswordEncoder();
        this.userService = userService;
    }

    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        String encryptedPassword = userDetails.getPassword();
        return encoder.matches(password, encryptedPassword);
    }

    @Override
    public boolean register(RegisterReq registerReq, Role role) {
        if (manager.userExists(registerReq.getUsername())) {
            return false;
        }

        UserDetails userDetails = User.builder()
                .password(encoder.encode(registerReq.getPassword()))
                .username(registerReq.getUsername())
                .roles(Role.USER.name())
                .build();

        manager.createUser(userDetails);

        // update user's firstname, lastname, phone after added by manager
        UserDto userDto = new UserDto();
        userDto.setEmail(userDetails.getUsername());
        userDto.setFirstName(registerReq.getFirstName());
        userDto.setLastName(registerReq.getLastName());
        userDto.setPhone(registerReq.getPhone());
        userService.updateUser(userDto);

        return true;
    }
}
