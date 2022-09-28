package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.models.dto.CreateUserDto;
import ru.skypro.homework.models.dto.NewPasswordDto;
import ru.skypro.homework.models.dto.UserDto;
import ru.skypro.homework.models.entity.User;

import java.util.List;

public interface UserService {

    CreateUserDto addUser(CreateUserDto user);

    List<UserDto> getUsers();

    UserDto updateUser(UserDto user);

    NewPasswordDto setPassword(NewPasswordDto newPassword);

    User getUser(Integer id);

    UserDto getUserDto(Integer id);

    User getUser(String username);

    UserDto getUserDto(String username);

    boolean isAdmin(Authentication authentication);
}
