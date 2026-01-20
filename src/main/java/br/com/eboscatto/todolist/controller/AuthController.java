package br.com.eboscatto.todolist.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.eboscatto.todolist.authentication.JwtUtil;
import br.com.eboscatto.todolist.model.UserModel;
import br.com.eboscatto.todolist.repository.ITaskRepository;
import br.com.eboscatto.todolist.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ITaskRepository iTaskRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel loginRequest) {
        // Buscar usuário pelo username
        var user = this.userRepository.findByUserName(loginRequest.getUserName());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuário não encontrado!"));
        }

        // Validar senha com BCrypt
        var result = BCrypt.verifyer()
                .verify(loginRequest.getPassword().toCharArray(), user.getPassword());

        if (!result.verified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Senha inválida!"));
        }
        // Gerar Token (JWT)
        String token = JwtUtil.generateToken(user.getUserName());

        return ResponseEntity.ok(Map.of(
                "message", "Login realizado com sucesso!",
                "token", token

        ));
    }
}