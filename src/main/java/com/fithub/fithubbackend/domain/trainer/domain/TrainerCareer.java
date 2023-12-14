package com.fithub.fithubbackend.domain.trainer.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrainerCareer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Trainer trainer;

    @NotNull
    @Size(min = 2)
    private String company;
    @NotNull
    @Size(min = 2)
    private String work;

    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;

    @ColumnDefault("false")
    @NotNull
    private boolean working;
}
