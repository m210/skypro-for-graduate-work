package ru.skypro.homework.models;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.skypro.homework.models.dto.AdsCommentDto;
import ru.skypro.homework.models.entity.Comments;
import ru.skypro.homework.models.entity.User;
import ru.skypro.homework.models.mappers.CommentsMapper;
import ru.skypro.homework.models.mappers.CommentsMapperImpl;

import java.sql.Timestamp;
import java.time.Instant;

import static ru.skypro.homework.models.Constants.*;
public class CommentsMapperTest {

    private final CommentsMapper commentsMapper = new CommentsMapperImpl();


    @Test
    public void shouldProperlyMapDtoToModel() {
        //given
        AdsCommentDto dto = new AdsCommentDto();
        dto.setText(TEXT);
        dto.setPk(COMMENT_PK);
        dto.setAuthor(USER_ID);
        dto.setCreatedAt(CREATED_AT);

        //when
        Comments model = commentsMapper.toComments(dto);

        //then
        Assertions.assertNotNull(model);
        Assertions.assertEquals(COMMENT_PK, model.getPk());
        Assertions.assertEquals(Timestamp.from(Instant.parse(CREATED_AT)).toString(), model.getCreatedAt().toString());
        Assertions.assertEquals(USER_ID, model.getAuthor().getId());
        Assertions.assertEquals(TEXT, model.getText());
    }

    @Test
    public void shouldProperlyMapModelToDto() {
        //given
        Comments model = new Comments();

        User user = new User();
        user.setPhone(USER_PHONE);
        user.setId(USER_ID);
        user.setFirstName(USER_FIRST_NAME);

        model.setText(TEXT);
        model.setPk(COMMENT_PK);
        model.setAuthor(user);
        model.setCreatedAt(Timestamp.from(Instant.parse(CREATED_AT)));

        //when
        AdsCommentDto dto = commentsMapper.toCommentsDto(model);

        //then
        Assertions.assertNotNull(model);
        Assertions.assertEquals(COMMENT_PK, dto.getPk());
        Assertions.assertEquals(model.getCreatedAt(), Timestamp.from(Instant.parse(dto.getCreatedAt())));
        Assertions.assertEquals(USER_ID, dto.getAuthor());
        Assertions.assertEquals(TEXT, dto.getText());
    }
}
