package com.APIZero.Zero.repositories;

import com.APIZero.Zero.entities.Todo;
import com.APIZero.Zero.enums.TodoState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    Optional<Todo> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT t FROM Todo t WHERE t.user.id = :userId "+
    "AND (:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%')))" +
    "AND (:description IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%')))" +
    "AND (:state IS NULL OR t.state = :state)")
    Page<Todo> findTodosWithFilters(
            @Param("userId") Long userId,
            @Param("title") String title,
            @Param("description") String description,
            @Param("state") TodoState state,
            Pageable pageable);
}