package br.com.eboscatto.todolist.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity(name = "tasks_tb")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @NotBlank(message = "A descrição é obrigatória")
    private String description;

    @Column(length = 50) // Limitar a 50 caraccteres
    @NotBlank(message = "O título é obrigatório")
    private String title;

    @FutureOrPresent(message = "A data de início não pode ser no passado")
    private LocalDateTime startAt;

    @Future(message = "A data de término deve ser no futuro")
    private LocalDateTime endAt;
    @AssertTrue(message = "A data de início deve ser antes da data de término")
    public boolean isStartBeforeEnd() {
        if (startAt == null || endAt == null) return true;
        return startAt.isBefore(endAt);
    }

    private String priority;

    @Column(name = "id_user")
    private Long idUser;

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    public void setTitle(String title) throws Exception {

        if (title.length() > 50) {
            throw  new Exception("O campo title deve conter no máximo 50 caracteres!");
        }

        this.title = title;

    }
    public void setUser(Long user) {

    }
}