package ru.skypro.homework.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "username")
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String password;
    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private List<Comments> commentsList;

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private List<Ads> adsList;
}
