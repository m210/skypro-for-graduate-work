package ru.skypro.homework.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;


import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.models.dto.AdsCommentDto;
import ru.skypro.homework.models.mappers.*;
import ru.skypro.homework.service.impl.AdsCommentsServiceImpl;
import ru.skypro.homework.service.impl.AdsServiceImpl;
import ru.skypro.homework.service.impl.ImageServiceImpl;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.skypro.homework.controller.Constants.*;

@WebMvcTest(controllers = AdsController.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestAddController {

    private final String LOCAL_URL = URL + PORT + "/" + ADS;

    private final AdsMapper adsMapper = new AdsMapperImpl();

    private final CommentsMapper commentsMapper = new CommentsMapperImpl();

    private final ImagesMapper imagesMapper = new ImagesMapperImpl();

    private final UserMapper userMapper = new UserMapperImpl();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdsServiceImpl adsService;

    @MockBean
    private AdsCommentsServiceImpl adsCommentsService;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private ImageServiceImpl imageService;

    @InjectMocks
    private AdsController adsController;

    @Before
    public void StartData() {
        IMAGE.setPk(IMAGE_ID);

        AUTHOR_MODEL.setId(AUTHORS_ID);
        AUTHOR_MODEL.setLastName(AUTHORS_LAST_NAME);
        AUTHOR_MODEL.setEmail(AUTHORS_EMAIL);
        AUTHOR_MODEL.setPassword(AUTHORS_PASSWORD);
        AUTHOR_MODEL.setFirstName(AUTHORS_FIRST_NAME);
        AUTHOR_MODEL.setPhone(AUTHORS_PHONE);

        ADS_MODEL.setPk(ADS_ID);
        ADS_MODEL.setImage(IMAGE);
        ADS_MODEL.setDescription(DESCRIPTION);
        ADS_MODEL.setTitle(TITLE);
        ADS_MODEL.setPrice(PRICE);
        ADS_MODEL.setAuthor(AUTHOR_MODEL);

        COMMENTS_MODEL.setAds(ADS_MODEL);
        COMMENTS_MODEL.setAuthor(AUTHOR_MODEL);
        COMMENTS_MODEL.setText(TEXT);
        NEW_COMMENTS_DTO = commentsMapper.toCommentsDto(COMMENTS_MODEL);

        COMMENTS_MODEL.setCreatedAt(TIMESTAMP);
        COMMENTS_MODEL.setPk(COMMENT_ID);
        COMMENTS_DTO = commentsMapper.toCommentsDto(COMMENTS_MODEL);
    }

    @Test
    public void contextLoads() {
        assertThat(adsController).isNotNull();
    }

    @Test
    @WithMockUser
    public void testGetAllAdsWithAuthority() throws Exception {

        mockMvc.perform(get(LOCAL_URL))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test//TODO починить, не работает
    @WithAnonymousUser
    public void testGetAllAdsWithoutAuthority() throws Exception {
        when(adsService.getALLAds()).thenReturn(LIST_ADS_DTO);
        mockMvc.perform(get("http://localhost:8080/test"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    public void testGetOneAdsWithAuthority() throws Exception {
        when(adsService.getFullAds(ADS_ID)).thenReturn(adsMapper.toFullAdsDto(ADS_MODEL));
        mockMvc.perform(get(LOCAL_URL + "/" + ADS_ID)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorLastName").value(AUTHORS_LAST_NAME))
                .andExpect(jsonPath("$.authorFirstName").value(AUTHORS_FIRST_NAME))
                .andExpect(jsonPath("$.description").value(DESCRIPTION))
                .andExpect(jsonPath("$.email").value(AUTHORS_EMAIL))
                .andExpect(jsonPath("$.image").value("http://127.0.0.1:8080/image/" + IMAGE_ID))
                .andExpect(jsonPath("$.phone").value(AUTHORS_PHONE))
                .andExpect(jsonPath("$.pk").value(ADS_ID))
                .andExpect(jsonPath("$.price").value(PRICE))
                .andExpect(jsonPath("$.title").value(TITLE))
        ;

    }

    @Test
    @WithAnonymousUser
    public void testGetOneAdsWithoutAuthority() throws Exception {
        when(adsService.getFullAds(ADS_ID)).thenReturn(adsMapper.toFullAdsDto(ADS_MODEL));
        mockMvc.perform(get(LOCAL_URL + "/" + ADS_ID)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());

    }


    @Test
    @WithMockUser
    public void testGetCommentsOneAdWithAuthority() throws Exception {
        List<AdsCommentDto> list = new ArrayList<>();
        list.add(COMMENTS_DTO);
        String expected = "{author=" + AUTHORS_ID +
                ", createdAt=" + COMMENTS_DTO.getCreatedAt() + ", " +
                "pk=" + COMMENT_ID + ", " +
                "text=" + TEXT +
                "}";

        when(adsCommentsService.getAdsComments(String.valueOf(ADS_ID))).thenReturn(list);
        mockMvc.perform(get(LOCAL_URL + "/" + ADS_ID + "/comments/")
                        .param("ad_pk", String.valueOf(ADS_ID))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
//                .andExpect(jsonPath("$.results_author").value(expected)) - //TODO не работает
        ;
    }

    @Test
    @WithAnonymousUser
    public void testGetCommentsOneAdWithoutAuthority() throws Exception {
        List<AdsCommentDto> list = new ArrayList<>();
        list.add(COMMENTS_DTO);
        when(adsCommentsService.getAdsComments(String.valueOf(ADS_ID))).thenReturn(list);
        mockMvc.perform(get(LOCAL_URL + "/" + ADS_ID + "/comments/")
                        .param("ad_pk", String.valueOf(ADS_ID))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void testGetAdsCommentsWithAuthority() throws Exception {
        when(adsCommentsService.getAdsComment(String.valueOf(ADS_ID), COMMENT_ID)).thenReturn(COMMENTS_DTO);
        mockMvc.perform(get(LOCAL_URL + "/" + ADS_ID + "/comments/" + COMMENT_ID)
                        .param("ad_pk", String.valueOf(ADS_ID))
                        .param("id", String.valueOf(COMMENT_ID))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(AUTHORS_ID))
                .andExpect(jsonPath("$.createdAt").value(COMMENTS_DTO.getCreatedAt()))
                .andExpect(jsonPath("$.pk").value(COMMENT_ID))
                .andExpect(jsonPath("$.text").value(TEXT))
        ;
    }

    @Test
    @WithAnonymousUser
    public void testGetAdsCommentsWithoutAuthority() throws Exception {
        when(adsCommentsService.getAdsComment(String.valueOf(ADS_ID), COMMENT_ID)).thenReturn(COMMENTS_DTO);
        mockMvc.perform(get(LOCAL_URL + "/" + ADS_ID + "/comments/" + COMMENT_ID)
                        .param("ad_pk", String.valueOf(ADS_ID))
                        .param("id", String.valueOf(COMMENT_ID))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    @WithMockUser
    public void testGetMyAdsWithAuthority() throws Exception {
        mockMvc.perform(get(LOCAL_URL + "/me")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
        ;
    }

    @Test
    @WithAnonymousUser
    public void testGetMyAdsWithoutAuthority() throws Exception {
        mockMvc.perform(get(LOCAL_URL + "/me"))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    @WithMockUser//TODO Не работает
    public void testPostAdsComments() throws Exception {
        when(adsCommentsService.addAdsComments(String.valueOf(ADS_ID), NEW_COMMENTS_DTO)).thenReturn(COMMENTS_DTO);
        System.out.println(NEW_COMMENTS_DTO);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(LOCAL_URL + "/" + ADS_ID + "/comments")
                        .param("ad_pk", ADS_ID.toString())
                        .content("{\"text\": \"COMMENT TEXT\""+"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(AUTHORS_ID))
                .andExpect(jsonPath("$.createdAt").value(COMMENTS_DTO.getCreatedAt()))
                .andExpect(jsonPath("$.pk").value(COMMENT_ID))
                .andExpect(jsonPath("$.text").value(TEXT));
    }


}

