package ru.skypro.homework.models.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ResponseWrapper<T> {

    private int count;
    private List<T> results;

    public ResponseWrapper(List<T> object) {
        this.count = object.size();
        this.results = object;
    }
}