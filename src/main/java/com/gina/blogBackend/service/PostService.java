package com.gina.blogBackend.service;

import com.gina.blogBackend.model.Post;
import com.gina.blogBackend.payload.dto.PostDto;
import com.gina.blogBackend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    private PostDto mapFromPostToDto(Post post) {
        PostDto postDto = new PostDto();
        if(post.getId()!= null){
            postDto.setId(post.getId());
        }
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        postDto.setImage(post.getImage());
        postDto.setDate(post.getDate());
        //User loggedInUser = authService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        postDto.setAuthor(post.getAuthor());
        postDto.setCountLike(post.getCountLike());
        postDto.setCountDislike(post.getCountDislike());
        postDto.setCountComments(post.getCountComments());
        postDto.setCountViews(post.getCountViews());
        return postDto;
    }

    private Post mapFromDtoToPost(PostDto postDto) {
        Post post = new Post();
        post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setDate(LocalDate.now());
        post.setContent(postDto.getContent());
        post.setImage(postDto.getImage());
        //User loggedInUser = authService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        post.setAuthor(postDto.getAuthor());
        post.setCountLike(postDto.getCountDislike());
        post.setCountDislike(postDto.getCountDislike());
        post.setCountComments(postDto.getCountComments());
        post.setCountViews(postDto.getCountViews());
        return post;
    }
}
