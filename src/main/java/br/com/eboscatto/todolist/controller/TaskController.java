package br.com.eboscatto.todolist.controller;

import br.com.eboscatto.todolist.authentication.JwtUtil;
import br.com.eboscatto.todolist.model.TaskModel;
import br.com.eboscatto.todolist.repository.ITaskRepository;
import br.com.eboscatto.todolist.repository.IUserRepository;
import br.com.eboscatto.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskModel task,
                                        @RequestHeader("Authorization") String token) {
        // extrai o username do token
        String username = JwtUtil.validateToken(token.replace("Bearer ", ""));
        var user = userRepository.findByUserName(username);

        // vincula a tarefa ao usuário logado
        task.setIdUser(user.getId());

        var taskCreated = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskCreated);
    }
    @GetMapping
    public ResponseEntity<?> listTasks(@RequestHeader("Authorization") String token) {
        String username = JwtUtil.validateToken(token.replace("Bearer ", ""));
        var user = userRepository.findByUserName(username);

        // agora o método bate com o atributo idUser
        var tasks = taskRepository.findByIdUser(user.getId()); // agora funciona, ambos são Long
        return ResponseEntity.ok(tasks);
    }
}