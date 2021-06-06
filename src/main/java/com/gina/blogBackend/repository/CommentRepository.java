package com.gina.blogBackend.repository;

import com.gina.blogBackend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByIdAndPostId(Long id, Long postId);

    List<Comment> findAllCommentsByPostId(Long postId);
   
    @Override
    void delete(Comment comment);
    @Query("SELECT COUNT(c) FROM Comment c WHERE post.id=:id")
    long countCommentsByPost(@Param("id") Long id);
}
