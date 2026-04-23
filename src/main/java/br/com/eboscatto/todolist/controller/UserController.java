package br.com.eboscatto.todolist.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.eboscatto.todolist.DTO.UserResponseDTO;
import br.com.eboscatto.todolist.model.UserModel;
import br.com.eboscatto.todolist.repository.IUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    // Não exibe a senha do usuário
    @GetMapping
    public ResponseEntity<?> listAll() {
        var users = this.userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getUserName(), user.getName(), user.getCreatedAt()))
                .toList();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(Authentication authentication) {
        String username = authentication.getName();

        UserModel user = userRepository.findByUserName(username);

        if (user == null) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
        var dto = new UserResponseDTO(
                user.getId(),
                user.getUserName(),
                user.getName(),
                user.getCreatedAt()
        );

        return ResponseEntity.ok(dto);
    }

}