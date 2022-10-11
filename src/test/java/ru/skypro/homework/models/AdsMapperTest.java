package ru.skypro.homework.models;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.skypro.homework.models.dto.AdsDto;
import ru.skypro.homework.models.dto.CreateAdsDto;
import ru.skypro.homework.models.dto.FullAdsDto;
import ru.skypro.homework.models.entity.Ads;

import ru.skypro.homework.models.mappers.AdsMapper;
import ru.skypro.homework.models.mappers.AdsMapperImpl;

import java.util.ArrayList;

import static ru.skypro.homework.models.Constants.*;

public class AdsMapperTest {

    private final AdsMapper adsMapper = new AdsMapperImpl();

    @Before
    public void init() {
        IMAGE_MODEL.setPk(IMAGE_ID);

    }


    @Test
    public void shouldProperlyMapAdsDtoToModel() {
        //given
        AdsDto dto = new AdsDto();
        dto.setAuthor(USER_ID);
        dto.setImage(IMAGE_URL);
        dto.setTitle(TITLE);
        dto.setPk(ADS_ID);
        dto.setPrice(PRICE);

        //when
        Ads model = adsMapper.fromAdsDto(dto, AUTHOR_MODEL, IMAGE_MODEL, new ArrayList<>());

        //then
        Assertions.assertNotNull(model);
        Assertions.assertEquals(ADS_ID, model.getPk());
        Assertions.assertEquals(PRICE, model.getPrice());
        Assertions.assertEquals(AUTHOR_MODEL, model.getAuthor());
        Assertions.assertEquals(TITLE, model.getTitle());
        Assertions.assertEquals(IMAGE_MODEL, model.getImage());
        Assertions.assertNull(model.getDescription());
    }

    @Test
    public void shouldProperlyMapCreateAdsDtoToModel() {
        //given
        CreateAdsDto dto = new CreateAdsDto();
        dto.setTitle(TITLE);
        dto.setPrice(PRICE);
        dto.setDescription(DESCRIPTION);

        //when
        Ads model = adsMapper.fromCreateAds(dto, AUTHOR_MODEL, IMAGE_MODEL);

        //then
        Assertions.assertNotNull(model);
        Assertions.assertEquals(PRICE, model.getPrice());
        Assertions.assertEquals(AUTHOR_MODEL, model.getAuthor());
        Assertions.assertEquals(TITLE, model.getTitle());
        Assertions.assertEquals(IMAGE_MODEL, model.getImage());
        Assertions.assertEquals(DESCRIPTION, model.getDescription());

    }

    @Test
    public void shouldProperlyMapModelToAdsDto() {
        //given
        Ads model = new Ads();

        model.setAuthor(AUTHOR_MODEL);

        model.setPk(ADS_ID);
        model.setTitle(TITLE);
        model.setDescription(DESCRIPTION);
        model.setPrice(PRICE);
        model.setImage(IMAGE_MODEL);

//        when
        AdsDto dto = adsMapper.toAdsDto(model);

//        then
        Assertions.assertNotNull(model);
        Assertions.assertEquals(ADS_ID, dto.getPk());
        Assertions.assertEquals(USER_ID, dto.getAuthor());
        Assertions.assertEquals(PRICE, dto.getPrice());
        Assertions.assertEquals(IMAGE_URL, dto.getImage());
        Assertions.assertEquals(TITLE, dto.getTitle());
    }

    @Test
    public void shouldProperlyMapModelToFullAdsDto() {
        //given
        Ads model = new Ads();

        model.setAuthor(AUTHOR_MODEL);

        model.setPk(ADS_ID);
        model.setTitle(TITLE);
        model.setDescription(DESCRIPTION);
        model.setPrice(PRICE);
        model.setImage(IMAGE_MODEL);

//        when
        FullAdsDto dto = adsMapper.toFullAdsDto(model);

//        then
        Assertions.assertNotNull(model);
        Assertions.assertEquals(ADS_ID, dto.getPk());
        Assertions.assertEquals(USER_FIRST_NAME, dto.getAuthorFirstName());
        Assertions.assertEquals(USER_LAST_NAME, dto.getAuthorLastName());
        Assertions.assertEquals(DESCRIPTION, dto.getDescription());
        Assertions.assertEquals(USER_EMAIL, dto.getEmail());
        Assertions.assertEquals(IMAGE_FULL_URL, dto.getImage());
        Assertions.assertEquals(USER_PHONE, dto.getPhone());
        Assertions.assertEquals(PRICE, dto.getPrice());
        Assertions.assertEquals(TITLE, dto.getTitle());
    }


}
