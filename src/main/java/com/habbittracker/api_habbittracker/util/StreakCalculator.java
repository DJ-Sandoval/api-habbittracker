package com.habbittracker.api_habbittracker.util;

import com.habbittracker.api_habbittracker.persistence.entities.HabitCompletionEntity;

import java.time.LocalDate;
import java.util.List;

public class StreakCalculator {
    public static int calculateCurrentStreak(List<HabitCompletionEntity> completions) {
        if (completions.isEmpty()) {
            return 0;
        }
        // Ordenar por fecha descendete
        completions.sort((c1, c2) -> c2.getCompletionDate().compareTo(c1.getCompletionDate()));
        int streak = 0;
        LocalDate expectedDate = LocalDate.now();
        for (HabitCompletionEntity completion : completions) {
            if (completion.getCompletionDate().equals(expectedDate) ||
                    (streak == 0 && completion.getCompletionDate().equals(LocalDate.now().minusDays(1)))) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else if (streak > 0) {
                break;
            }
        }
        return streak;
        }
    }
