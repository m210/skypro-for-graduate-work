package ru.skypro.homework.controller;

import ru.skypro.homework.models.dto.*;
import ru.skypro.homework.models.entity.Ads;
import ru.skypro.homework.models.entity.Comments;
import ru.skypro.homework.models.entity.Images;
import ru.skypro.homework.models.entity.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Constants {

    protected final static String URL = "http://localhost:";
    protected final static String PORT = "8080";
    protected final static String ADS = "ads";
    protected final static Ads ADS_MODEL = new Ads();
    protected final static String TITLE = "title ads dto";
    protected final static String DESCRIPTION = "description ads dto";
    protected final static int PRICE = 150;
    protected final static Images IMAGE = new Images();
    protected final static Integer IMAGE_ID = 12;
    protected final static List<Ads> LIST_ADS = new ArrayList<>();
    protected final static List<Comments> MODEL_COMMENT_LIST = new ArrayList<>();
    protected final static User AUTHOR_MODEL = new User();
    protected final static Integer AUTHORS_ID = 2;
    protected final static String AUTHORS_LAST_NAME = "Last name";
    protected final static String AUTHORS_FIRST_NAME = "First name";
    protected final static String AUTHORS_PHONE = "+79992214290";
    protected final static String AUTHORS_EMAIL = "stranger@mail.ru";
    protected final static String AUTHORS_PASSWORD = "password";
    protected final static Integer ADS_ID = 56;
    protected final static Integer COMMENT_ID = 3;
    protected final static String TEXT = "COMMENT TEXT";
    protected final static Comments COMMENTS_MODEL = new Comments();
    protected static Comments NEW_COMMENTS_MODEL = new Comments();
    protected final static Timestamp TIMESTAMP = Timestamp.valueOf(LocalDateTime.now());
    protected static AdsCommentDto COMMENTS_DTO = new AdsCommentDto();
    protected static AdsCommentDto NEW_COMMENTS_DTO = new AdsCommentDto();
    protected static AdsDto ADS_DTO = new AdsDto();
    protected static FullAdsDto FULL_ADS_DTO = new FullAdsDto();
    protected static UserDto USER_DTO = new UserDto();

}