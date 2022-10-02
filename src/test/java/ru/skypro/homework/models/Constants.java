package ru.skypro.homework.models;

import ru.skypro.homework.models.entity.Ads;
import ru.skypro.homework.models.entity.Comments;
import ru.skypro.homework.models.entity.Images;
import ru.skypro.homework.models.entity.User;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    protected final static String TEXT = "Text in comment";
    protected final static Integer COMMENT_PK = 5;
    protected final static String CREATED_AT = "2022-06-08T10:10:10Z";
    protected final static Integer USER_ID = 4;
    protected final static String USER_LAST_NAME = "Last name";
    protected final static String USER_FIRST_NAME = "First name";
    protected final static String USER_PHONE = "+79992214290";
    protected final static String USER_EMAIL = "25@ya.ru";
    protected final static String USER_PASSWORD = "password";
    protected final static List<Ads> ADS_LIST = new ArrayList<>();
    protected final static List<Comments> COMMENTS_LIST = new ArrayList<>();
    protected final static String IMAGE_URL = "/image/12";
    protected final static Integer IMAGE_ID = 12;
    protected final static String TITLE = "Title ads";
    protected final static int PRICE = 500;
    protected final static Integer ADS_ID = 54;
    protected final static String DESCRIPTION = "Description ads";

    protected final static Images IMAGE_MODEL = new Images();

    protected final static User AUTHOR_MODEL= new User();

    protected final static String IMAGE_FULL_URL = "http://127.0.0.1:8080/image/12";



}
