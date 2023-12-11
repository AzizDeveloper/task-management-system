package dev.aziz.taskmanagementsystem.services;

import dev.aziz.taskmanagementsystem.dtos.CredentialsDto;
import dev.aziz.taskmanagementsystem.dtos.SelectPerformerDto;
import dev.aziz.taskmanagementsystem.dtos.SignUpDto;
import dev.aziz.taskmanagementsystem.dtos.UserDto;
import dev.aziz.taskmanagementsystem.entities.Confirmation;
import dev.aziz.taskmanagementsystem.entities.Role;
import dev.aziz.taskmanagementsystem.entities.Task;
import dev.aziz.taskmanagementsystem.entities.User;
import dev.aziz.taskmanagementsystem.exceptions.AppException;
import dev.aziz.taskmanagementsystem.mappers.UserMapper;
import dev.aziz.taskmanagementsystem.repositories.ConfirmationRepository;
import dev.aziz.taskmanagementsystem.repositories.RoleRepository;
import dev.aziz.taskmanagementsystem.repositories.TaskRepository;
import dev.aziz.taskmanagementsystem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final TaskRepository taskRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        return userMapper.usersToUserDtos(userRepository.findAll());
    }

    public UserDto getUserById(Long id) {
        return userMapper.toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND))
        );
    }


    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByEmail(credentialsDto.getEmail())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto signUpDto) {
        Optional<User> optionalUser = userRepository.findByEmail(signUpDto.getEmail());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(signUpDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.getPassword())));

        Set<Role> roles = new HashSet<>();
        for (int i = 0; i < signUpDto.getRoles().size(); i++) {
            Role role = roleRepository.findByName(signUpDto.getRoles().get(i).getName())
                    .orElseThrow(() -> new AppException("Unknown role", HttpStatus.NOT_FOUND));
            roles.add(role);
        }
        user.setRoles(roles);
        user.setIsEnabled(false);
        User savedUser = userRepository.save(user);

        Confirmation confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);

        emailService.sendSimpleMailMessage(user.getFirstName(), user.getEmail(), confirmation.getActivationCode());

        return userMapper.toUserDto(savedUser);
    }

    public UserDto findByLogin(String login) {
        User user = userRepository.findByEmail(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

    public UserDto selectThePerformer(SelectPerformerDto performerDto) {
        Task task = taskRepository.findById(performerDto.getTaskId())
                .orElseThrow(() -> new AppException("Task not found", HttpStatus.NOT_FOUND));
        UserDto oldPerformer = getUserById(performerDto.getOldPerformer());
        UserDto newPerformer = getUserById(performerDto.getNewPerformer());
        oldPerformer.setTasks(Collections.emptyList());
        newPerformer.setTasks(List.of(task));
        userRepository.save(userMapper.toUser(oldPerformer));
        userRepository.save(userMapper.toUser(newPerformer));
        return newPerformer;
    }


    public Boolean verifyActivationCode(String activationCode) {
        Confirmation confirmation = confirmationRepository.findByActivationCode(activationCode);
        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        user.setIsEnabled(true);
        userRepository.save(user);
//        confirmationRepository.delete(confirmation);
        return Boolean.TRUE;
    }
}
