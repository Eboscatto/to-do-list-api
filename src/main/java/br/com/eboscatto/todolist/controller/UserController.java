package br.com.eboscatto.todolist.controller;

import br.com.eboscatto.todolist.model.UserModel;
import br.com.eboscatto.todolist.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired // Gerencia o cilo de vida do IUserRepository
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){
        var user = this.userRepository.findByUserName(userModel.getUserName());

        if(user != null) {

            // Mensagem de erro
            // Status Code

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe!");
        }

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userCreated);

    }

}
