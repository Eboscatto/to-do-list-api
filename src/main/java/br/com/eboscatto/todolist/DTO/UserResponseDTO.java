package br.com.eboscatto.todolist.DTO;

import java.time.LocalDateTime;

public record UserResponseDTO(Long id, String userName, String name, LocalDateTime createdAt) {

}
