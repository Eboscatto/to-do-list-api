package br.com.eboscatto.todolist.repository;

import br.com.eboscatto.todolist.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {

}
