package br.com.eboscatto.todolist.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data //Insere os Getters e Setters
@Entity(name = "tb_users")

public class UserModel {
    @Id
    @GeneratedValue(generator = "UUID") //Gerencia o Id
    private UUID id; // Mais seguro do que um Id sequencial

    @Column(unique = true)
    private String userName;
    private String name;
    private String password;

    @CreationTimestamp // Cria automaticamente os dados de data e hora
    private LocalDateTime createdAt;
}
