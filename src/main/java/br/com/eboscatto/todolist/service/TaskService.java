package br.com.eboscatto.todolist.service;

import br.com.eboscatto.todolist.authentication.JwtUtil;
import br.com.eboscatto.todolist.model.TaskModel;
import br.com.eboscatto.todolist.repository.ITaskRepository;
import br.com.eboscatto.todolist.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String username = JwtUtil.validateToken(token.replace("Bearer ", ""));
        var user = userRepository.findByUserName(username);

        // vincula ao usuário logado
        task.setIdUser(user.getId());

        return taskRepository.save(task);
    }
    public List<TaskModel> listTasks(String username) {
        var user = userRepository.findByUserName(username);
        return taskRepository.findByIdUser(user.getId());
    }
}

