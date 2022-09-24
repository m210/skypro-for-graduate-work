package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;
import ru.skypro.homework.models.dto.CreateUserDto;
import ru.skypro.homework.models.dto.NewPasswordDto;
import ru.skypro.homework.models.dto.UserDto;
import ru.skypro.homework.models.entity.User;
import ru.skypro.homework.models.mappers.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Admin access only
     * @param user Dto with information about new user. The password should be encrypted!
     * @return Dto with added to database information.
     */
    @Override
    public CreateUserDto addUser(CreateUserDto user) {
        logger.info("Trying to add new user");
        User response = userRepository.save(userMapper.toUser(user));
        logger.info("The user with id = {} was saved ", response.getId());
        return userMapper.toCreateUserDto(response);
    }

    /**
     * @return list of the all users in data base
     */
    @Override
    public List<UserDto> getUsers() {
        logger.info("Trying to get all users");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    /**
     * @param userDto - Dto with update information, may contain NULL. The method will update not null fields only.
     * @return UserDto response with the actual data
     */
    @Override
    public UserDto updateUser(UserDto userDto) {
        logger.info("Trying to update the userDto with id = {}", userDto.getId());
        try {
            User user = getUser(userDto.getEmail());
            logger.info("The userDto is found, updating...");
            if(userDto.getFirstName() != null) {
                user.setFirstName(userDto.getFirstName());
            }

            if(userDto.getLastName() != null) {
                user.setLastName(userDto.getLastName());
            }

            if(userDto.getPhone() != null) {
                user.setPhone(userDto.getPhone());
            }

            User response = userRepository.save(user);
            logger.info("The userDto with id = {} was updated ", response.getId());
            return userMapper.toUserDto(response);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * @param username - email of the user
     * @param newPassword - Dto with current password and new password
     * @return newPasswordDto if the password is changed or ResponseStatusException (status == 403)
     *         if current password of the user doesn't match with entered password
     */
    @Override
    public NewPasswordDto setPassword(String username, NewPasswordDto newPassword) {
        logger.info("trying to set new password");

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = getUser(username);
        if(!passwordEncoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        String newPass = passwordEncoder.encode(newPassword.getNewPassword());
        user.setPassword(newPass);
        User response = userRepository.save(user);
        logger.info("The user with id = {} was updated ", response.getId());

        return newPassword;
    }

    /**
     * @param id - primary key of the user in "users" table
     * @return UserDto or NotFoundException (status 404) if is not found
     */
    @Override
    public UserDto getUser(Integer id) {
        logger.info("trying to get the user with id = {}", id);
        User user = userRepository
                .findById(id)
                .orElseThrow((Supplier<RuntimeException>) () -> new NotFoundException("The user with id = " + id + " doesn't exist"));
        logger.info("The user with id = {} was found", id);
        return userMapper.toUserDto(user);
    }

    /**
     * @param username - email of the user in "users" table
     * @return User entity or NotFoundException (status 404) if is not found
     */
    @Override
    public User getUser(String username) {
        logger.info("trying to get the user by name = {}", username);
        User user = userRepository
                .findUserByEmail(username)
                .orElseThrow((Supplier<RuntimeException>) () -> new NotFoundException("The user with name = " + username + " doesn't exist"));
        logger.info("The user with name = {} was found", username);
        return user;
    }
}
