package ru.skypro.homework.models;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.skypro.homework.models.dto.CreateUserDto;
import ru.skypro.homework.models.dto.UserDto;
import ru.skypro.homework.models.entity.User;
import ru.skypro.homework.models.mappers.UserMapper;
import ru.skypro.homework.models.mappers.UserMapperImpl;

import static ru.skypro.homework.models.Constants.*;

public class UserMapperTest {

    private final UserMapper userMapper = new UserMapperImpl();


    @Test
    public void shouldProperlyMapUserDtoToModel() {
        //given
        UserDto dto = new UserDto();
        dto.setEmail(USER_EMAIL);
        dto.setPhone(USER_PHONE);
        dto.setFirstName(USER_FIRST_NAME);
        dto.setLastName(USER_LAST_NAME);

        //when
        User model = userMapper.toUser(dto);

        //then
        Assertions.assertNotNull(model);
        Assertions.assertEquals(USER_EMAIL, model.getEmail());
        Assertions.assertEquals(USER_PHONE, model.getPhone());
        Assertions.assertEquals(USER_FIRST_NAME, model.getFirstName());
        Assertions.assertEquals(USER_LAST_NAME, model.getLastName());
        Assertions.assertNull(model.getId());
        Assertions.assertNull(model.getPassword());
        Assertions.assertNull(model.getAdsList());
        Assertions.assertNull(model.getCommentsList());

    }

    @Test
    public void shouldProperlyMapCreateUserDtoToModel() {
        //given
        CreateUserDto dto = new CreateUserDto();
        dto.setEmail(USER_EMAIL);
        dto.setPhone(USER_PHONE);
        dto.setFirstName(USER_FIRST_NAME);
        dto.setLastName(USER_LAST_NAME);
        dto.setPassword(USER_PASSWORD);

        //when
        User model = userMapper.toUser(dto);

        //then
        Assertions.assertNotNull(model);
        Assertions.assertEquals(USER_EMAIL, model.getEmail());
        Assertions.assertEquals(USER_PHONE, model.getPhone());
        Assertions.assertEquals(USER_FIRST_NAME, model.getFirstName());
        Assertions.assertEquals(USER_LAST_NAME, model.getLastName());
        Assertions.assertEquals(USER_PASSWORD, model.getPassword());
        Assertions.assertNull(model.getId());
        Assertions.assertNull(model.getAdsList());
        Assertions.assertNull(model.getCommentsList());

    }

    @Test
    public void shouldProperlyMapModelToUserDto() {
        //given
        User model = new User();

        model.setPhone(USER_PHONE);
        model.setId(USER_ID);
        model.setFirstName(USER_FIRST_NAME);
        model.setLastName(USER_LAST_NAME);
        model.setEmail(USER_EMAIL);
        model.setPassword(USER_PASSWORD);
        model.setAdsList(ADS_LIST);
        model.setCommentsList(COMMENTS_LIST);


        //when
        UserDto dto = userMapper.toUserDto(model);

        //then
        Assertions.assertNotNull(model);
        Assertions.assertEquals(USER_EMAIL, dto.getEmail());
        Assertions.assertEquals(USER_PHONE, dto.getPhone());
        Assertions.assertEquals(USER_FIRST_NAME, dto.getFirstName());
        Assertions.assertEquals(USER_LAST_NAME, dto.getLastName());

    }

    @Test
    public void shouldProperlyMapModelToCreateUserDto() {
        //given
        User model = new User();

        model.setPhone(USER_PHONE);
        model.setId(USER_ID);
        model.setFirstName(USER_FIRST_NAME);
        model.setLastName(USER_LAST_NAME);
        model.setEmail(USER_EMAIL);
        model.setPassword(USER_PASSWORD);
        model.setAdsList(ADS_LIST);
        model.setCommentsList(COMMENTS_LIST);


        //when
        CreateUserDto dto = userMapper.toCreateUserDto(model);

        //then
        Assertions.assertNotNull(model);
        Assertions.assertEquals(USER_EMAIL, dto.getEmail());
        Assertions.assertEquals(USER_PHONE, dto.getPhone());
        Assertions.assertEquals(USER_FIRST_NAME, dto.getFirstName());
        Assertions.assertEquals(USER_LAST_NAME, dto.getLastName());
        Assertions.assertEquals(USER_PASSWORD, dto.getPassword());

    }
}
