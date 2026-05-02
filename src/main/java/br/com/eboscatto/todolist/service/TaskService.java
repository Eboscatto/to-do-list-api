package br.com.eboscatto.todolist.service;

import br.com.eboscatto.todolist.model.TaskModel;
import br.com.eboscatto.todolist.model.UserModel;
import br.com.eboscatto.todolist.repository.ITaskRepository;
import br.com.eboscatto.todolist.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private IUserRepository userRepository;


    // Buscar usuário
    public UserModel getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUserName(username))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuário não encontrado"
                ));
    }

    // Validação de conflito
    public void validateConflict(Long userId, LocalDateTime startAt, LocalDateTime endAt) {

        List<TaskModel> conflitos = taskRepository.findConflictingTasks(
                userId, startAt, endAt
        );

        if (!conflitos.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe uma tarefa nesse horário"
            );
        }
    }

    public void validateConflict(Long userId, UUID taskId, LocalDateTime startAt, LocalDateTime endAt) {

        List<TaskModel> conflitos = taskRepository.findConflictingTasksForUpdate(
                userId, taskId, startAt, endAt
        );

        if (!conflitos.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe uma tarefa nesse horário"
            );
        }
    }

    // Criar tarefa
    public TaskModel createTask(TaskModel task, String username) {

        UserModel user = getUserByUsername(username);

        validateConflict(user.getId(), task.getStartAt(), task.getEndAt());

        task.setUser(user);

        return taskRepository.save(task);
    }

    // Atualizar tarefa
    public TaskModel updateTask(UUID id, TaskModel updatedTask, String username) throws Exception {

        UserModel user = getUserByUsername(username);

        TaskModel task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tarefa não encontrada"
                ));

        // Validar que a tarefa pertence ao usuário
        if (!task.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão");
        }

        validateConflict(user.getId(), id, updatedTask.getStartAt(), updatedTask.getEndAt());

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setStartAt(updatedTask.getStartAt());
        task.setEndAt(updatedTask.getEndAt());
        task.setPriority(updatedTask.getPriority());

        return taskRepository.save(task);
    }


    // Listar tarefas
    public List<TaskModel> listTasks(String username) {
        UserModel user = getUserByUsername(username);
        return taskRepository.findByUser(user);
    }


    // Deletar tarefa
    public void deleteTask(UUID id, Authentication auth) {

        String username = auth.getName();

        UserModel user = Optional.ofNullable(userRepository.findByUserName(username))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuário não encontrado"
                ));

        TaskModel task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tarefa não encontrada"
                ));

        // Comparação correta
        if (!task.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão");
        }

        taskRepository.delete(task);
    }
}

