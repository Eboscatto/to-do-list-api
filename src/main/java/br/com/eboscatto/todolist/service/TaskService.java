package br.com.eboscatto.todolist.service;

import br.com.eboscatto.todolist.authentication.JwtUtil;
import br.com.eboscatto.todolist.model.TaskModel;
import br.com.eboscatto.todolist.model.UserModel;
import br.com.eboscatto.todolist.repository.ITaskRepository;
import br.com.eboscatto.todolist.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private IUserRepository userRepository;

    public TaskModel createTask(TaskModel task, String token) throws Exception {

        // extrai o username do token
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUserName(username);

        // vincula a tarefa ao usuário logado
        task.setUser(user);

        return taskRepository.save(task);
    }
    public List<TaskModel> listTasks(String username) {
        var user = userRepository.findByUserName(username);
        return taskRepository.findByUser(user);
    }
}

