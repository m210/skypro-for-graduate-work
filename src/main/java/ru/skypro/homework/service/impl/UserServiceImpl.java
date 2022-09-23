package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.skypro.homework.models.dto.CreateUserDto;
import ru.skypro.homework.models.dto.NewPasswordDto;
import ru.skypro.homework.models.dto.UserDto;
import ru.skypro.homework.models.entity.User;
import ru.skypro.homework.models.mappers.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserDetailsManager manager;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public CreateUserDto addUser(CreateUserDto user) {
        logger.info("Trying to add new user");
        User response = userRepository.save(userMapper.toUser(user));
        logger.info("The user with id = {} was saved ", response.getId());
        return userMapper.toCreateUserDto(response);
    }

    @Override
    public List<UserDto> getUsers() {
        logger.info("Trying to get all users");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        logger.info("Trying to update the userDto with id = {}", userDto.getId());
        Optional<User> userOpt = userRepository.findById(userDto.getId());
        if (userOpt.isEmpty()) {
            logger.warn("The userDto with id = {} doesn't exist", userDto.getId());
            throw new NotFoundException("The userDto with id = " + userDto.getId() + " doesn't exist");
        }

        logger.info("The userDto is found, updating...");
        User user = userOpt.get();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());

        User response = userRepository.save(user);
        logger.info("The userDto with id = {} was updated ", response.getId());
        return userMapper.toUserDto(response);
    }

    @Override
    public NewPasswordDto setPassword(String username, NewPasswordDto newPassword) {
        logger.info("trying to set new password");

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPass = newPassword.getNewPassword();
        String pass = "{bcrypt}" + passwordEncoder.encode(newPass);

        User user = userRepository
                .findUserByEmail(username)
                .orElseThrow((Supplier<RuntimeException>) () -> new NotFoundException("The user with name = " + username + " doesn't exist"));

        user.setPassword(pass);
        User response = userRepository.save(user);
        logger.info("The user with id = {} was updated ", response.getId());

        return newPassword;
    }

    @Override
    public UserDto getUser(Integer id) {
        logger.info("trying to get the user with id = {}", id);
        User user = userRepository
                .findById(id)
                .orElseThrow((Supplier<RuntimeException>) () -> new NotFoundException("The user with id = " + id + " doesn't exist"));
        logger.info("The user with id = {} was found", id);
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto getUser(String username) {
        logger.info("trying to get the user by name = {}", username);
        User user = userRepository
                .findUserByEmail(username)
                .orElseThrow((Supplier<RuntimeException>) () -> new NotFoundException("The user with name = " + username + " doesn't exist"));
        logger.info("The user with name = {} was found", username);
        return userMapper.toUserDto(user);
    }
}
