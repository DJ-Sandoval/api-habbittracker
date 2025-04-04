package com.habbittracker.api_habbittracker.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "habit_completions")
@Getter @Setter
@NoArgsConstructor
public class HabitCompletionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate completionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private HabitEntity habit;

    public HabitCompletionEntity(LocalDate completionDate, HabitEntity habit) {
        this.completionDate = completionDate;
        this.habit = habit;
    }
}
