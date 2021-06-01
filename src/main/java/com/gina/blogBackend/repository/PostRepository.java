package com.gina.blogBackend.repository;

import com.gina.blogBackend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying
    @Query("update Post p set p.countLike = :countLike where p.id = :id")
    void updateLike(@Param(value = "id") Long id, @Param(value = "countLike") int countLike);

    //dislikes
    @Modifying
    @Query("update Post p set p.countDislike = :countDislike where p.id = :id")
    void updateDislike(@Param(value = "id") Long id, @Param(value = "countDislike") int countDislike);

    @Modifying
    @Query("update Post p set p.countComments = :countComments where p.id = :id")
    void updateCommentsCount(@Param(value = "id") Long id, @Param(value = "countComments") int countComments);

    @Modifying
    @Query("update Post p set p.countViews = :countViews where p.id = :id")
    void updateViews(@Param(value = "id") Long id, @Param(value = "countViews") int countViews);
}
