package br.com.eboscatto.todolist.dto;

import java.time.LocalDateTime;

public record UserResponseDTO(Long id, String userName, String name, LocalDateTime createdAt) {

    // Limita as informações a serem exibidas
}
