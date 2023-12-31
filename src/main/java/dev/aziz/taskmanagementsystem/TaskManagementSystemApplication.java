package dev.aziz.taskmanagementsystem;

import dev.aziz.taskmanagementsystem.config.security.PasswordConfig;
import dev.aziz.taskmanagementsystem.entities.Comment;
import dev.aziz.taskmanagementsystem.entities.Priority;
import dev.aziz.taskmanagementsystem.entities.Role;
import dev.aziz.taskmanagementsystem.entities.Status;
import dev.aziz.taskmanagementsystem.entities.Task;
import dev.aziz.taskmanagementsystem.entities.User;
import dev.aziz.taskmanagementsystem.repositories.RoleRepository;
import dev.aziz.taskmanagementsystem.repositories.TaskRepository;
import dev.aziz.taskmanagementsystem.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;
import java.util.Set;

@SpringBootApplication
@EnableAsync
public class TaskManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagementSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            TaskRepository taskRepository,
            PasswordConfig passwordConfig) {
        return args -> {

            Role authorRole = new Role("AUTHOR");
            Role performerRole = new Role("PERFORMER");

            roleRepository.save(authorRole);
            roleRepository.save(performerRole);

            Comment commentTask1 = Comment.builder()
                    .body("Create docker compose file and configure and start working with database.")
                    .build();

            Comment commentTask2 = Comment.builder()
                    .body("Add all test dependencies and use Mockito and so.")
                    .build();

            Comment commentTask3 = Comment.builder()
                    .body("Create all layers models, services, controllers.")
                    .build();

            Task task1 = Task.builder()
                    .title("Connect to database")
                    .status(Status.PROCESSING)
                    .priority(Priority.HIGH)
                    .comments(List.of(commentTask1))
                    .build();
            taskRepository.save(task1);
            Task task2 = Task.builder()
                    .title("Write Unit tests")
                    .status(Status.WAITING)
                    .priority(Priority.MEDIUM)
                    .comments(List.of(commentTask2))
                    .build();
            taskRepository.save(task2);

            Task task3 = Task.builder()
                    .title("Create REST API starting code")
                    .status(Status.DONE)
                    .priority(Priority.LOW)
                    .comments(List.of(commentTask3))
                    .build();
            taskRepository.save(task3);


            User aziz = new User(
                    "aziz",
                    "abdukarimov",
                    "azizdev",
                    passwordConfig.passwordEncoder().encode("asdasd"),
                    true,
                    Set.of(authorRole),
                    List.of(task1, task2, task3)
            );

            userRepository.save(aziz);

            User bob = new User(
                    "bob",
                    "john",
                    "bobdev",
                    passwordConfig.passwordEncoder().encode("asdasd"),
                    true,
                    Set.of(performerRole),
                    List.of(task1)
            );
            userRepository.save(bob);

            User azim = new User(
                    "Azim",
                    "Abdukarimov",
                    "azimdev",
                    passwordConfig.passwordEncoder().encode("asdasd"),
                    true,
                    Set.of(performerRole),
                    List.of(task2, task3)
            );
            userRepository.save(azim);

            User sergio = new User(
                    "sergio",
                    "lema",
                    "sergiodev",
                    passwordConfig.passwordEncoder().encode("asdasd"),
                    true,
                    Set.of(performerRole)
            );
            userRepository.save(sergio);
        };
    }

}
