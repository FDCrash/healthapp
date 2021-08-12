package com.app.health.controllers;

import com.app.health.services.dto.PostDto;
import com.app.health.services.dto.response.ResponsePostObject;
import com.app.health.services.implementation.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Map;

@Api
@RestController
@RequestMapping("/post")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @ApiOperation(value = "Add post", nickname = "PostController.addPost")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Post is adding")})
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addPost(@RequestPart(value = "file", required = false) MultipartFile file,
                                          @RequestPart String description) {
        PostDto postDto = PostDto.builder()
                .description(description)
                .file(file)
                .build();

        Map id = Collections.singletonMap("id", postService.add(postDto));

        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get all posts", nickname = "PostController.getAllPosts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Posts returned")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponsePostObject> getPosts(
            @PageableDefault (sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {
        ResponsePostObject responsePostObject =
                postService.getPageablePostsWithStream(pageable);
        return new ResponseEntity<>(responsePostObject, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all posts without image stream", nickname = "PostController.getAllPostsWithoutImage")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Posts returned")})
    @GetMapping(value = "/light",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponsePostObject> getPostsWithoutImage(
            @PageableDefault (sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {
        ResponsePostObject responsePostObject =
                postService.getPageablePostsWithoutStream(pageable);
        return new ResponseEntity<>(responsePostObject, HttpStatus.OK);
    }
}
