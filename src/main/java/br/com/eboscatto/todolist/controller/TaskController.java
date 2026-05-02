package br.com.eboscatto.todolist.controller;

import br.com.eboscatto.todolist.DTO.TaskRequestDTO;
import br.com.eboscatto.todolist.DTO.TaskResponseDTO;
import br.com.eboscatto.todolist.model.TaskModel;
import br.com.eboscatto.todolist.repository.ITaskRepository;
import br.com.eboscatto.todolist.repository.IUserRepository;
import br.com.eboscatto.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(
            @RequestBody @Valid TaskRequestDTO dto,
            Authentication auth) throws Exception {

        TaskModel task = new TaskModel();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setStartAt(dto.startAt());
        task.setEndAt(dto.endAt());
        task.setPriority(dto.priority());

        TaskModel saved = taskService.createTask(task, auth.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new TaskResponseDTO(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStartAt(),
                        task.getEndAt(),
                        task.getPriority(),
                        task.getCreatedAt()
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> list(Authentication auth) {
        List<TaskModel> tasks = taskService.listTasks(auth.getName());

        List<TaskResponseDTO> response = tasks.stream()
                .map(task -> new TaskResponseDTO(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStartAt(),
                        task.getEndAt(),
                        task.getPriority(),
                        task.getCreatedAt()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid TaskRequestDTO dto,
            Authentication auth) throws Exception {

        TaskModel task = new TaskModel();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setStartAt(dto.startAt());
        task.setEndAt(dto.endAt());
        task.setPriority(dto.priority());

        TaskModel updated = taskService.updateTask(
                id,
                task,
                auth.getName()
        );

        return ResponseEntity.ok(
                new TaskResponseDTO(
                        updated.getId(),
                        updated.getTitle(),
                        updated.getDescription(),
                        updated.getStartAt(),
                        updated.getEndAt(),
                        updated.getPriority(),
                        updated.getCreatedAt()
                )
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id,
                                         Authentication auth) {
        taskService.deleteTask(id, auth);

        return ResponseEntity.noContent().build();
    }

}
