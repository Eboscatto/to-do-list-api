package br.com.eboscatto.todolist.DTO;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data // Cria os Getters e Setters
public class LoginRequest {
    private String userName;
    private String password;
}
