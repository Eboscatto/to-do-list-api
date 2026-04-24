package br.com.eboscatto.todolist.controller;

import br.com.eboscatto.todolist.DTO.TaskRequestDTO;
import br.com.eboscatto.todolist.DTO.TaskResponseDTO;
import br.com.eboscatto.todolist.authentication.JwtUtil;
import br.com.eboscatto.todolist.model.TaskModel;
import br.com.eboscatto.todolist.model.UserModel;
import br.com.eboscatto.todolist.repository.ITaskRepository;
import br.com.eboscatto.todolist.repository.IUserRepository;
import br.com.eboscatto.todolist.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

        // Vem do token JWT
        String username = auth.getName();

        UserModel user = userRepository.findByUserName(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado");
        }

        TaskModel task = new TaskModel();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setStartAt(dto.startAt());
        task.setEndAt(dto.endAt());
        task.setPriority(dto.priority());
        task.setUser(user);

        taskRepository.save(task);

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
    public ResponseEntity<List<TaskResponseDTO>> list(Authentication authentication) {

        // Busca usuário autenticado
        String username = authentication.getName();

        UserModel user = userRepository.findByUserName(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        List<TaskResponseDTO> tasks = taskRepository.findByUser(user)
                .stream()
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

        return ResponseEntity.ok(tasks);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id,
                                         Authentication auth) {

        // Vem do token
        String username = auth.getName();

        TaskModel task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Valida o dono da tarefa
        if (!task.getUser().getUserName().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão");
        }

        taskRepository.delete(task);

        return ResponseEntity.ok("Exclusão realizada com sucesso");
    }

}