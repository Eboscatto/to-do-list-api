package br.com.eboscatto.todolist.dto;


import java.time.LocalDateTime;
import java.util.UUID;

// DTO de saída enviar dados da tarefa para o front
public record TaskResponseDTO(  UUID id,
                                String title,
                                String description,
                                LocalDateTime startAt,
                                LocalDateTime endAt,
                                String priority,
                                LocalDateTime createdAt) {}
