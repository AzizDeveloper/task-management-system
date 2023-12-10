package dev.aziz.taskmanagementsystem.services;

import dev.aziz.taskmanagementsystem.entities.Comment;
import dev.aziz.taskmanagementsystem.entities.Task;
import dev.aziz.taskmanagementsystem.exceptions.AppException;
import dev.aziz.taskmanagementsystem.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Page<Task> getTasksByPageRequest(Long id, PageRequest pageRequest) {
        return taskRepository.findAllByPageRequest(id, pageRequest);
    }

    public Task getOneTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new AppException("Task not found.", HttpStatus.NOT_FOUND));
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task deleteTask(Long id) {
        Task task = getOneTask(id);
        taskRepository.deleteById(id);
        return task;
    }

    public Task editFullTask(Long id, Task task) {
        Task foundTask = getOneTask(id);
        foundTask.setTitle(task.getTitle());
        foundTask.setStatus(task.getStatus());
        foundTask.setPriority(task.getPriority());
        foundTask.setComments(task.getComments());
        return taskRepository.save(foundTask);
    }

    public Task editTask(Long id, Task task) {
        Task foundTask = getOneTask(id);

        if (task.getTitle() != null) {
            foundTask.setTitle(task.getTitle());
        }

        if (task.getStatus() != null) {
            foundTask.setStatus(task.getStatus());
        }

        if (task.getPriority() != null) {
            foundTask.setPriority(task.getPriority());
        }

        if (task.getComments() != null) {
            List<Comment> newComments = new ArrayList<>();
            for (Comment comment : task.getComments()) {
                if ("".equals(comment.getBody())) {
                    throw new AppException("Comment must not be empty.", HttpStatus.BAD_REQUEST);
                } else {
                    newComments.add(comment);
                }
            }
            foundTask.setComments(newComments);
        }
        return taskRepository.save(foundTask);
    }
}
