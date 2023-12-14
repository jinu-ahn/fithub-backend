package com.fithub.fithubbackend.domain.trainer.domain;

import com.fithub.fithubbackend.global.domain.Document;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrainerLicenseImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Trainer trainer;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private Document document;
}
