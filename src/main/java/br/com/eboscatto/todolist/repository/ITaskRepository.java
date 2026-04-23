package br.com.eboscatto.todolist.repository;

import br.com.eboscatto.todolist.model.TaskModel;
import br.com.eboscatto.todolist.model.UserModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    List<TaskModel> findByUser(UserModel user);

    Optional<TaskModel> findByIdAndUser(UUID id, UserModel user);
}

