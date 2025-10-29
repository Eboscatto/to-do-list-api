package br.com.eboscatto.todolist.model;

import jakarta.persistence.*;
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
    private String description;

    @Column(length = 50) // Limitar a 50 caraccteres
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;

    private UUID idUser;

    @CreationTimestamp
    private LocalDateTime createdAt;


    public void setTitle(String title) throws Exception {

        if (title.length() > 50) {
            throw  new Exception("O campo title deve conter no máximo 50 caracteres!");
        }

        this.title = title;

    }
}
