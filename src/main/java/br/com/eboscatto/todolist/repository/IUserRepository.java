package br.com.eboscatto.todolist.repository;

import br.com.eboscatto.todolist.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<UserModel, Long> {
   UserModel findByUserName(String userName);
}