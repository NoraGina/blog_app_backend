package com.gina.blogBackend.service;

import com.gina.blogBackend.exception.CommentNotFoundException;
import com.gina.blogBackend.model.Comment;
import com.gina.blogBackend.payload.dto.CommentDto;
import com.gina.blogBackend.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    private CommentDto mapFromCommentToDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        if(comment.getId()!=null){
            commentDto.setId(comment.getId());
        }
        commentDto.setText(comment.getText());
        commentDto.setDate(LocalDate.now());
        commentDto.setTime((LocalTime.now()));
        commentDto.setPost(comment.getPost());

        return commentDto;
    }

    private Comment mapFromDtoToComment(CommentDto commentDto){
        Comment comment = new Comment();
        if(commentDto.getId()!= null){
            comment.setId(commentDto.getId());
        }
        comment.setText(commentDto.getText());
        comment.setDate(LocalDate.now());
        comment.setTime(LocalTime.now());
        comment.setUsername(commentDto.getUsername());
        comment.setPost(commentDto.getPost());
        return comment;
    }

    @Transactional
    public void saveComment(CommentDto commentDto)  {
        Comment comment = mapFromDtoToComment(commentDto);
        commentRepository.save(comment);
    }

    @Transactional
    public List<CommentDto> showAllComments(Long postId) {
        List<Comment> comments = commentRepository.findAllCommentsByPostId(postId);
        return comments.stream().map(this::mapFromCommentToDto).collect(Collectors.toList());
    }

    @Transactional
    public CommentDto readSingleComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException("For id " + id));
        return mapFromCommentToDto(comment);
    }

    @Transactional
    public CommentDto findByIdAndPostId(Long id, Long postId){
        Comment comment = commentRepository.findByIdAndPostId(id, postId);
        if(comment == null){
            throw new CommentNotFoundException("Cannot find comment");
        }
        return mapFromCommentToDto(comment);
    }

    @Transactional
    public void deleteById(Long id){
        commentRepository.deleteById(id);
    }
}
