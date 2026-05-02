package br.com.eboscatto.todolist.repository;

import br.com.eboscatto.todolist.model.TaskModel;
import br.com.eboscatto.todolist.model.UserModel;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;
public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    List<TaskModel> findByUser(UserModel user);

    Optional<TaskModel> findByIdAndUser(UUID id, UserModel user);
    @Query("""
    SELECT t FROM TaskModel t
    WHERE t.user.id = :userId
    AND (
        t.startAt < :endAt AND t.endAt > :startAt
    )
    """)
        List<TaskModel> findConflictingTasks(
                @Param("userId") Long userId,
                @Param("startAt") LocalDateTime startAt,
                @Param("endAt") LocalDateTime endAt
        );

    @Query("""
    SELECT t FROM TaskModel t
    WHERE t.user.id = :userId
    AND t.id <> :taskId
    AND (
        t.startAt < :endAt AND t.endAt > :startAt
    )
    """)
        List<TaskModel> findConflictingTasksForUpdate(
                @Param("userId") Long userId,
                @Param("taskId") UUID taskId,
                @Param("startAt") LocalDateTime startAt,
                @Param("endAt") LocalDateTime endAt
        );
}




