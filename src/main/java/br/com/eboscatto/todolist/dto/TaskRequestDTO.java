package br.com.eboscatto.todolist.dto;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;


// DTO de entrada receber daddos do front para criar/editar tarefa
public record TaskRequestDTO(

        @NotBlank(message = "O título é obrigatório")
        @Size(max = 50, message = "O título deve ter no máximo 50 caracteres")
        String title,

        @NotBlank(message = "A descrição é obrigatória")
        String description,

        @FutureOrPresent(message = "A data de início não pode ser no passado")
        LocalDateTime startAt,

        @Future(message = "A data de término deve ser no futuro")
        LocalDateTime endAt,

        String priority) {

}
