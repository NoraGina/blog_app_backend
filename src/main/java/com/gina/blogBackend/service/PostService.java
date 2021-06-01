package com.gina.blogBackend.service;

import com.gina.blogBackend.model.Post;
import com.gina.blogBackend.payload.dto.PostDto;
import com.gina.blogBackend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
        postDto.setTime(post.getTime());
        postDto.setUpdateDate(post.getUpdateDate());
        postDto.setUpdateTime(post.getUpdateTime());
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
        post.setDate(postDto.getDate());
        post.setTime(postDto.getTime());
        post.setUpdateDate(postDto.getUpdateDate());
        post.setUpdateTime(postDto.getUpdateTime());
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

    @Transactional
    public List<PostDto> showAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::mapFromPostToDto).collect(Collectors.toList());
    }

    @Transactional
    public void createPost(PostDto postDto)  {
        Post post = mapFromDtoToPost(postDto);
        postRepository.save(post);
    }

    @Transactional
    public PostDto readSinglePost(Long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("For id " + id));
        return mapFromPostToDto(post);
    }

    @Transactional
    public void deleteById(Long id){
        postRepository.deleteById(id);
    }

    @Transactional
    public void updateLike(Long id, int countLike){
        postRepository.updateLike(id, countLike);
    }

    @Transactional
    public void updateDislike(Long id, int countDislike){
        postRepository.updateDislike(id, countDislike);
    }

    @Transactional
    public void updateCountComments(Long id, int countComments){
        postRepository.updateCommentsCount(id, countComments);
    }

    @Transactional
    public void updateCountViews(Long id, int countViews){
        postRepository.updateViews(id, countViews);
    }
}
