package dev.aziz.taskmanagementsystem.controllers;

import dev.aziz.taskmanagementsystem.entities.Task;
import dev.aziz.taskmanagementsystem.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
//@RestController(value = "api/v1")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    //TODO: i should remove this method in the end
    @GetMapping("/tasks/just")
    public ResponseEntity<?> just() {
        return ResponseEntity.ok("HELLO, this is unsecured endpoint!");
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/tasks/user")
    public ResponseEntity<Page<Task>> getAllTasksByPage(@RequestParam Long id, @RequestParam int page, @RequestParam int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(taskService.getTasksByPageRequest(id ,pageRequest));
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getOneTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getOneTask(id));
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.created(URI.create("/tasks/" + task.getId()))
                .body(taskService.createTask(task));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteTask(id));
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> editFullTask(@PathVariable Long id, @RequestBody Task task) {
        return ResponseEntity.ok(taskService.editFullTask(id, task));
    }

    @PatchMapping("/tasks/{id}")
    public ResponseEntity<Task> editTask(@PathVariable Long id, @RequestBody Task task) {
        return ResponseEntity.ok(taskService.editTask(id, task));
    }
}
