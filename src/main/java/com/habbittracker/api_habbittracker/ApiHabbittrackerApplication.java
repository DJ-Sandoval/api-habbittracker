package com.habbittracker.api_habbittracker;

import com.habbittracker.api_habbittracker.persistence.entities.PermissionEntity;
import com.habbittracker.api_habbittracker.persistence.entities.RoleEntity;
import com.habbittracker.api_habbittracker.persistence.entities.RoleEnum;
import com.habbittracker.api_habbittracker.persistence.entities.UserEntity;
import com.habbittracker.api_habbittracker.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class ApiHabbittrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiHabbittrackerApplication.class, args);
	}
}