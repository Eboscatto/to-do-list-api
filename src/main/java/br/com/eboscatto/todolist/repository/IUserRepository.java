package br.com.eboscatto.todolist.repository;

import br.com.eboscatto.todolist.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {
   UserModel findByUserName(String userName);
}
