package ru.skypro.homework.controller;

import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.controller.Constants.*;

@WebMvcTest(controllers = AdsController.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TestAddControllerPost {
    private final String LOCAL_URL = URL + PORT + "/" + "ads";
    private final AdsMapper adsMapperTest = new AdsMapperImpl();
    private final CommentsMapper commentsMapperTest = new CommentsMapperImpl();
    private final ImagesMapper imagesMapperTest = new ImagesMapperImpl();
    private final UserMapper userMapperTest = new UserMapperImpl();
    private final JSONObject adsCommentDto = new JSONObject();

    private final JSONObject adsJsonDTO = new JSONObject();

    @Autowired
    private WebApplicationContext context;
//
//    @Mock
//    SecurityContext mockSecurityContext;

    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private AdsServiceImpl adsService;
    @MockBean
    private AdsRepository adsRepository;
//    @MockBean
//    private Authentication auth;
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
    public void startData() {
        IMAGE.setPk(IMAGE_ID);

        USER_DTO = userMapperTest.toUserDto(AUTHOR_MODEL);

        ADS_MODEL.setAuthor(AUTHOR_MODEL);

        LIST_ADS.add(ADS_MODEL);
        ADS_DTO = adsMapperTest.toAdsDto(ADS_MODEL);
        FULL_ADS_DTO = adsMapperTest.toFullAdsDto(ADS_MODEL);

        COMMENTS_MODEL.setAds(ADS_MODEL);
        COMMENTS_MODEL.setText(TEXT);
        NEW_COMMENTS_MODEL = COMMENTS_MODEL;
        NEW_COMMENTS_DTO = commentsMapperTest.toCommentsDto(NEW_COMMENTS_MODEL);

        COMMENTS_MODEL.setAuthor(AUTHOR_MODEL);
        COMMENTS_MODEL.setCreatedAt(TIMESTAMP);
        COMMENTS_MODEL.setPk(COMMENT_ID);
        COMMENTS_DTO = commentsMapperTest.toCommentsDto(COMMENTS_MODEL);

        MODEL_COMMENT_LIST.add(COMMENTS_MODEL);

//        adsCommentDto.put("author", AUTHORS_ID);
//        adsCommentDto.put("createdAt", COMMENTS_DTO.getCreatedAt());
//        adsCommentDto.put("pk", COMMENT_ID);
        adsCommentDto.put("text", TEXT);

        adsJsonDTO.put("description", "description");
        adsJsonDTO.put("price", "105");
        adsJsonDTO.put("title", "ads title");
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();


    }

    @Test
    public void contextLoads() {
        assertThat(adsController).isNotNull();
    }


    @Test
    @WithMockUser(username = AUTHORS_EMAIL, value = AUTHORS_EMAIL)
    public void testPostAdsComments() throws Exception {
        when(adsRepository.findById(ADS_ID)).thenReturn(Optional.of(ADS_MODEL));
        when(commentsMapper.toComments(any())).thenReturn(NEW_COMMENTS_MODEL);
        when(adsCommentsRepository.save(any())).thenReturn(NEW_COMMENTS_MODEL);


        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(AUTHOR_MODEL));
        when(commentsMapper.toCommentsDto(any())).thenReturn(COMMENTS_DTO);

        mockMvc.perform((post(LOCAL_URL + "/" + ADS_ID + "/comments")
                        .content(adsCommentDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(AUTHORS_ID))
                .andExpect(jsonPath("$.createdAt").value(COMMENTS_DTO.getCreatedAt()))
                .andExpect(jsonPath("$.pk").value(COMMENT_ID))
                .andExpect(jsonPath("$.text").value(TEXT));


    }

    @Test
    @WithMockUser(username = AUTHORS_EMAIL, value = AUTHORS_EMAIL)
    public void testPostAds() throws Exception {

//        when(auth.getName()).thenReturn(AUTHORS_EMAIL);
//        when(auth.getPrincipal()).thenReturn(AUTHOR_MODEL);


        mockMvc.perform((post(LOCAL_URL + "/" + ADS_ID)
//                        .with(user(AUTHORS_EMAIL).password(AUTHORS_PASSWORD).roles("USER", "ADMIN")))
                        .content(adsJsonDTO.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(AUTHORS_ID))
                .andExpect(jsonPath("$.createdAt").value(COMMENTS_DTO.getCreatedAt()))
                .andExpect(jsonPath("$.pk").value(COMMENT_ID))
                .andExpect(jsonPath("$.text").value(TEXT));

//        mockMvc.perform(post(LOCAL_URL + "/" + ADS_ID + "/comments")
//
//                        .param("ad_pk", ADS_ID.toString())
//                        .content(adsCommentDto.toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.author").value(AUTHORS_ID))
//                .andExpect(jsonPath("$.createdAt").value(COMMENTS_DTO.getCreatedAt()))
//                .andExpect(jsonPath("$.pk").value(COMMENT_ID))
//                .andExpect(jsonPath("$.text").value(TEXT));
    }


}

