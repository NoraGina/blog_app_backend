package com.gina.blogBackend.controller;

import com.gina.blogBackend.exception.ResourceNotFoundException;
import com.gina.blogBackend.model.Comment;
import com.gina.blogBackend.model.Post;
import com.gina.blogBackend.payload.dto.CommentDto;
import com.gina.blogBackend.payload.dto.PostDto;
import com.gina.blogBackend.repository.CommentRepository;
import com.gina.blogBackend.repository.PostRepository;
import com.gina.blogBackend.service.CommentService;
import com.gina.blogBackend.service.PostService;
import com.gina.blogBackend.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private byte[] bytes;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @GetMapping("/all")

    public ResponseEntity<List<PostDto>> getAllPosts() {
        return new ResponseEntity<>(postService.showAllPosts(), HttpStatus.OK);
    }


    @PostMapping("/add")
    public ResponseEntity createPost(@RequestBody PostDto postDto, @AuthenticationPrincipal UserDetails currentUser) {
        postDto.setAuthor(currentUser.getUsername());
        postDto.setImage(this.bytes);
        postDto.setCountLike(0);
        postDto.setCountDislike(0);
        postDto.setCountComments(0);
        postDto.setCountViews(0);
        postDto.setDate(LocalDate.now());
        postDto.setTime(LocalTime.now());
        postDto.setUpdateTime(LocalTime.now());
        postDto.setUpdateDate(LocalDate.now());
        postService.createPost(postDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/upload")
    public void uploadImage(@RequestParam("imageFile") MultipartFile file) throws IOException {
        this.bytes = file.getBytes();
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public void updatePost(@RequestBody PostDto postDto, @AuthenticationPrincipal UserDetails currentUser) {
        Post post = postRepository.getById(postDto.getId());
        postDto.setAuthor(currentUser.getUsername());
        postDto.setImage(this.bytes);
        postDto.setDate(post.getDate());
        postDto.setTime(post.getTime());
        postDto.setUpdateDate(LocalDate.now());
        postDto.setUpdateTime(LocalTime.now());
        postDto.setCountLike(post.getCountLike());
        postDto.setCountDislike(post.getCountDislike());
        postDto.setCountComments(post.getCountComments());
        postDto.setCountViews(post.getCountViews());
        postService.createPost(postDto);
    }

    @DeleteMapping(path = { "/{id}" })
    @PreAuthorize("hasRole('ADMIN')")
    public PostDto deletePost(@PathVariable("id") Long id) {
        PostDto post = postService.readSinglePost(id);
        postService.deleteById(id);
        return post;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PostDto> getSinglePost(@PathVariable @RequestBody Long id) {
        postService.updateCountViews(id, postRepository.getOne(id).getCountViews()+1);
        return new ResponseEntity<>(postService.readSinglePost(id), HttpStatus.OK);
    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable @RequestBody Long id) {
        
        return new ResponseEntity<>(postService.readSinglePost(id), HttpStatus.OK);
    }


    @PutMapping("/{id}/like")
    public PostDto updateLike(@PathVariable(value = "id") Long postId,@RequestBody PostDto postDto)throws ResourceNotFoundException{
        postDto = postService.readSinglePost(postId);

        postService.updateLike(postId, postDto.getCountLike()+1);
        return postDto;
    }

    @PutMapping("/{id}/dislike")
    public void updateDislike(@PathVariable(value = "id") Long postId,
                              @RequestBody PostDto postDto) throws ResourceNotFoundException {
        postDto = postService.readSinglePost(postId);

        postService.updateDislike(postId, postDto.getCountDislike()+1);

    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity <List<Comment>> getAllCommentsByPostId(@PathVariable (value = "postId") Long postId
    ) {
        return new ResponseEntity<>(commentRepository.findAllCommentsByPostId(postId), HttpStatus.OK);
    }

    @PostMapping("/{postId}/comment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity createComment(@PathVariable (value = "postId") Long postId,
                                        @Valid @RequestBody CommentDto comment, @AuthenticationPrincipal UserDetails currentUser) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if(optionalPost.isPresent()){
            final Post post = optionalPost.get();
            comment.setPost(post);
            comment.setUsername(currentUser.getUsername());
            commentService.saveComment(comment);
            postService.updateCountComments(postId,post.getCountComments()+1 );
        }else{
            throw new ResourceNotFoundException("Post Id "+ postId+ " not found");
        }

        return new ResponseEntity(HttpStatus.OK);
    }

}
