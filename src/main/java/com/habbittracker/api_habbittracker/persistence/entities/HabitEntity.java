package com.habbittracker.api_habbittracker.persistence.entities;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "habits")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class HabitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private LocalTime reminderTime;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private int targetDaysPerWeek;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HabitCompletionEntity> completions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
