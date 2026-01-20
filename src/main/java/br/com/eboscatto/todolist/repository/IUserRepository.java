package br.com.eboscatto.todolist.repository;

import br.com.eboscatto.todolist.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserModel, Long> {
   UserModel findByUserName(String userName);
}