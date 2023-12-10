package dev.aziz.taskmanagementsystem.controllers;

import dev.aziz.taskmanagementsystem.dtos.SelectPerformerDto;
import dev.aziz.taskmanagementsystem.dtos.UserDto;
import dev.aziz.taskmanagementsystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getOneUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PatchMapping("/users/edit")
    public ResponseEntity<UserDto> selectPerformer(@RequestBody SelectPerformerDto selectPerformerDto) {
        return ResponseEntity.ok(userService.selectThePerformer(selectPerformerDto));
    }

}
