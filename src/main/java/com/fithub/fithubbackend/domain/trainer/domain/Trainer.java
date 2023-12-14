package com.fithub.fithubbackend.domain.trainer.domain;


import com.fithub.fithubbackend.domain.user.domain.User;
import com.fithub.fithubbackend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trainer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @NotNull
    @Size(min = 2)
    private String name;

    @NotNull
    private String location;

    @Builder
    public Trainer(User user, String location) {
        this.user = user;
        this.name = user.getName();
        this.location = location;
    }
}
