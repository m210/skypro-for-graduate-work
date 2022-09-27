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
    protected final static String TITLE = "title ads dto";
    protected final static String DESCRIPTION = "description ads dto";
    protected final static Integer ADS_ID = 10;
    protected final static Integer ADS_ID_2 = 59;

    protected final static int PRICE = 150;
    protected final static Images IMAGE = new Images();
    protected final static Integer IMAGE_ID = 12;
    protected final static Ads ADS_MODEL = new Ads(ADS_ID, PRICE, TITLE, DESCRIPTION, IMAGE, new User(), new ArrayList<>() );
    protected final static Ads ADS_MODEL_2 = new Ads(ADS_ID_2, PRICE+2, TITLE+"_2", DESCRIPTION+"_2", IMAGE, new User(), new ArrayList<>() );

    protected final static List<Ads> LIST_ADS = new ArrayList<>(List.of(ADS_MODEL, ADS_MODEL_2));
    protected final static Integer AUTHORS_ID = 2;
    protected final static String AUTHORS_LAST_NAME = "Last name";
    protected final static String AUTHORS_FIRST_NAME = "First name";
    protected final static String AUTHORS_PHONE = "+79992214290";
    protected final static String AUTHORS_EMAIL = "ed2408@yandex.ru";
    protected final static String AUTHORS_PASSWORD = "password1";
    protected final static User AUTHOR_MODEL= new User(AUTHORS_ID, AUTHORS_EMAIL, AUTHORS_FIRST_NAME, AUTHORS_LAST_NAME, AUTHORS_PHONE, AUTHORS_PASSWORD, new ArrayList<>(), new ArrayList<>());
    protected final static Integer COMMENT_ID = 3;
    protected final static String TEXT = "COMMENT TEXT";
    protected static Comments NEW_COMMENTS_MODEL = new Comments();
    protected final static Timestamp TIMESTAMP = Timestamp.valueOf(LocalDateTime.now());
    protected final static Comments COMMENTS_MODEL = new Comments();
    protected final static List<Comments> MODEL_COMMENT_LIST = new ArrayList<>();
    protected static AdsCommentDto COMMENTS_DTO = new AdsCommentDto();
    protected static AdsCommentDto NEW_COMMENTS_DTO = new AdsCommentDto();
    protected static AdsDto ADS_DTO = new AdsDto();
    protected static FullAdsDto FULL_ADS_DTO = new FullAdsDto();
    protected static UserDto USER_DTO = new UserDto();

}
