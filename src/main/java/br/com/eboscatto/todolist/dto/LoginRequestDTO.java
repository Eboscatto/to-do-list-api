package br.com.eboscatto.todolist.dto;

import lombok.Data;

@Data // Cria os Getters e Setters
public class LoginRequestDTO {
    private String userName;
    private String password;
}
