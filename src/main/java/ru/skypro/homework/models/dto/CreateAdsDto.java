package ru.skypro.homework.models.dto;

import lombok.Data;

@Data
public class CreateAdsDto {
    private String description;
    private String image;
    private Integer price;
    private String title;
}
