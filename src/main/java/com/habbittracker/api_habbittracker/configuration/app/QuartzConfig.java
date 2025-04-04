package com.habbittracker.api_habbittracker.configuration.app;
import com.habbittracker.api_habbittracker.util.jobs.HabitReminderJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail habitReminderJobDetail() {
        return JobBuilder.newJob(HabitReminderJob.class)
                .withIdentity("habitReminderJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger habitReminderTrigger(JobDetail habitReminderJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(habitReminderJobDetail)
                .withIdentity("habitReminderTrigger")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(8, 0)) // 8:00 AM
                .build();
    }

}
