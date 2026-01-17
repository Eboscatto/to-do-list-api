package br.com.eboscatto.todolist.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.eboscatto.todolist.DTO.UserResponseDTO;
import br.com.eboscatto.todolist.model.UserModel;
import br.com.eboscatto.todolist.repository.IUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserModel userModel) {
        var existingUser = this.userRepository.findByUserName(userModel.getUserName());

        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Usuário já existe!"));
        }

        var passwordHashed = BCrypt.withDefaults()
                .hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashed);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    // Exibe também a senha do usuário

    /*
    @GetMapping
    public ResponseEntity<?> listAll() {
        var users = this.userRepository.findAll();
        return ResponseEntity.ok(users);
    }

     */

    // Não exibe a senha do usuário
    @GetMapping
    public ResponseEntity<?> listAll() {
        var users = this.userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getUserName(), user.getName(), user.getCreatedAt()))
                .toList();

        return ResponseEntity.ok(users);
    }


}