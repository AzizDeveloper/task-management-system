package dev.aziz.taskmanagementsystem.repositories;

import dev.aziz.taskmanagementsystem.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
