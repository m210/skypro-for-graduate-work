package ru.skypro.homework.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
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
