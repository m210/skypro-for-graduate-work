package ru.skypro.homework.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;


import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.models.mappers.*;
import ru.skypro.homework.repository.AdsCommentsRepository;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AdsCommentsServiceImpl;
import ru.skypro.homework.service.impl.AdsServiceImpl;
import ru.skypro.homework.service.impl.ImageServiceImpl;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.skypro.homework.controller.Constants.*;

@WebMvcTest(controllers = AdsController.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestAddController {

    private final String LOCAL_URL = URL + PORT + "/" + ADS;

    private final AdsMapper adsMapperTest = new AdsMapperImpl();

    private final CommentsMapper commentsMapperTest = new CommentsMapperImpl();

    private final ImagesMapper imagesMapperTest = new ImagesMapperImpl();

    private final UserMapper userMapperTest = new UserMapperImpl();

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private AdsServiceImpl adsService;

    @MockBean
    private AdsRepository adsRepository;

    @SpyBean
    private AdsCommentsServiceImpl adsCommentsService;

    @MockBean
    private AdsCommentsRepository adsCommentsRepository;

    @MockBean
    private ImageRepository imageRepository;

    @MockBean
    private UserRepository userRepository;

    @SpyBean
    private UserServiceImpl userService;

    @SpyBean
    private ImageServiceImpl imageService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private AdsMapper adsMapper;

    @MockBean
    private CommentsMapper commentsMapper;

    @MockBean
    private ImagesMapper imagesMapper;


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

        USER_DTO = userMapperTest.toUserDto(AUTHOR_MODEL);

        ADS_MODEL.setPk(ADS_ID);
        ADS_MODEL.setImage(IMAGE);
        ADS_MODEL.setDescription(DESCRIPTION);
        ADS_MODEL.setTitle(TITLE);
        ADS_MODEL.setPrice(PRICE);
        ADS_MODEL.setAuthor(AUTHOR_MODEL);

        LIST_ADS.add(ADS_MODEL);
        ADS_DTO = adsMapperTest.toAdsDto(ADS_MODEL);
        FULL_ADS_DTO=adsMapperTest.toFullAdsDto(ADS_MODEL);

        COMMENTS_MODEL.setAds(ADS_MODEL);
        COMMENTS_MODEL.setText(TEXT);
        NEW_COMMENTS_MODEL = COMMENTS_MODEL;
        NEW_COMMENTS_DTO = commentsMapperTest.toCommentsDto(NEW_COMMENTS_MODEL);

        COMMENTS_MODEL.setAuthor(AUTHOR_MODEL);
        COMMENTS_MODEL.setCreatedAt(TIMESTAMP);
        COMMENTS_MODEL.setPk(COMMENT_ID);
        COMMENTS_DTO = commentsMapperTest.toCommentsDto(COMMENTS_MODEL);

        MODEL_COMMENT_LIST.add(COMMENTS_MODEL);

    }

    @Test
    public void contextLoads() {
        assertThat(adsController).isNotNull();
    }

    @Test
    @WithMockUser
    public void testGetAllAdsWithAuthority() throws Exception {
        when(adsRepository.findAll()).thenReturn(LIST_ADS);
        mockMvc.perform(get(LOCAL_URL))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test//TODO починить, не работает
    @WithAnonymousUser
    public void testGetAllAdsWithoutAuthority() throws Exception {
        when(adsRepository.findAll()).thenReturn(LIST_ADS);
        mockMvc.perform(get(LOCAL_URL))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetOneAdsWithAuthority() throws Exception {
        System.out.println(ADS_MODEL.getAuthor());
        when(adsRepository.findById(ADS_ID)).thenReturn(Optional.of(ADS_MODEL));
        when(adsMapper.toFullAdsDto(ADS_MODEL)).thenReturn(FULL_ADS_DTO);
        mockMvc.perform(get(LOCAL_URL + "/" + ADS_ID)
                        .param("ad_pk", String.valueOf(ADS_ID))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
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
        when(adsRepository.findById(ADS_ID)).thenReturn(Optional.of(ADS_MODEL));
        when(adsMapper.toFullAdsDto(ADS_MODEL)).thenReturn(FULL_ADS_DTO);
        mockMvc.perform(get(LOCAL_URL + "/" + ADS_ID)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser
    public void testGetCommentsOneAdWithAuthority() throws Exception {
        String expected = "{author=" + AUTHORS_ID +
                ", createdAt=" + COMMENTS_DTO.getCreatedAt() + ", " +
                "pk=" + COMMENT_ID + ", " +
                "text=" + TEXT +
                "}";

        when((adsRepository.findById(ADS_ID))).thenReturn(Optional.of(ADS_MODEL));
        when((adsCommentsRepository.findCommentsByAds(ADS_MODEL))).thenReturn(MODEL_COMMENT_LIST);
        when(commentsMapper.toCommentsDto(any())).thenReturn(COMMENTS_DTO);
        mockMvc.perform(get(LOCAL_URL + "/" + ADS_ID + "/comments/")
                        .param("ad_pk", String.valueOf(ADS_ID))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
//                .andExpect(jsonPath("$.results").value(expected))  //TODO не работает
        ;
    }

    @Test
    @WithAnonymousUser
    public void testGetCommentsOneAdWithoutAuthority() throws Exception {
        when((adsRepository.findById(ADS_ID))).thenReturn(Optional.of(ADS_MODEL));
        when((adsCommentsRepository.findCommentsByAds(ADS_MODEL))).thenReturn(MODEL_COMMENT_LIST);
        when(commentsMapper.toCommentsDto(any())).thenReturn(COMMENTS_DTO);
        mockMvc.perform(get(LOCAL_URL + "/" + ADS_ID + "/comments/")
                        .param("ad_pk", String.valueOf(ADS_ID))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void testGetAdsCommentsWithAuthority() throws Exception {
        mockMvc.perform(get(LOCAL_URL + "/" + ADS_ID + "/comments/" + COMMENT_ID)
                        .param("ad_pk", String.valueOf(ADS_ID))
                        .param("id", String.valueOf(COMMENT_ID))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound()); //ads not found exception


        when((adsRepository.findById(ADS_ID))).thenReturn(Optional.of(ADS_MODEL));
        when(adsCommentsRepository.findById(COMMENT_ID)).thenReturn(Optional.of(COMMENTS_MODEL));
        when(commentsMapper.toCommentsDto(COMMENTS_MODEL)).thenReturn(COMMENTS_DTO);

        mockMvc.perform(get(LOCAL_URL + "/" + ADS_ID + "/comments/" + COMMENT_ID)
                        .param("ad_pk", String.valueOf(ADS_ID))
                        .param("id", String.valueOf(COMMENT_ID))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(AUTHORS_ID))
                .andExpect(jsonPath("$.createdAt").value(COMMENTS_DTO.getCreatedAt()))
                .andExpect(jsonPath("$.pk").value(COMMENT_ID))
                .andExpect(jsonPath("$.text").value(TEXT));
    }

    @Test
    @WithAnonymousUser
    public void testGetAdsCommentsWithoutAuthority() throws Exception {
        when((adsRepository.findById(ADS_ID))).thenReturn(Optional.of(ADS_MODEL));
        when(adsCommentsRepository.findById(COMMENT_ID)).thenReturn(Optional.of(COMMENTS_MODEL));
        when(commentsMapper.toCommentsDto(COMMENTS_MODEL)).thenReturn(COMMENTS_DTO);

        mockMvc.perform(get(LOCAL_URL + "/" + ADS_ID + "/comments/" + COMMENT_ID)
                        .param("ad_pk", String.valueOf(ADS_ID))
                        .param("id", String.valueOf(COMMENT_ID))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    @WithMockUser(username = "stranger@mail.ru", authorities = "USER")
    public void testGetMyAdsWithAuthority() throws Exception {
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(AUTHOR_MODEL));
        when(adsRepository.findAll()).thenReturn(LIST_ADS);
        when(adsMapper.toAdsDto(any())).thenReturn(ADS_DTO);
        mockMvc.perform(get(LOCAL_URL + "/me")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
    @WithAnonymousUser
    public void testGetMyAdsWithoutAuthority() throws Exception {
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(AUTHOR_MODEL));
        when(adsRepository.findAll()).thenReturn(LIST_ADS);
        mockMvc.perform(get(LOCAL_URL + "/me"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    @WithMockUser(username = "stranger@mail.ru", authorities = "USER")
    public void testPostAdsComments() throws Exception {
        when(adsRepository.findById(ADS_ID)).thenReturn(Optional.of(ADS_MODEL));
        when(commentsMapper.toComments(any())).thenReturn(NEW_COMMENTS_MODEL);
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(AUTHOR_MODEL));
        when(commentsMapper.toCommentsDto(any())).thenReturn(COMMENTS_DTO);
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

