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

    @ApiOperation(value = "Add post", nickname = "PostController.addPost",
            notes = "Use this method for add new post with 'formData'<br/>" +
                    "'file' parameter - for adding file <br/>" +
                    "'description' parameter - for add description to post <br/>" +
                    "Response - map - \"id\":2 (id of post)")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Post is added")})
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

    @ApiOperation(value = "Get all posts with image stream", nickname = "PostController.getAllPosts",
            notes = "Use this method for get paginated posts<br/>" +
                    "Pagination example:  /post?page=2&size=2<br/>" +
                    "Response: Content-Type - application/json<br/>" +
                    " {<br/>" +
                    " &nbsp;\"postDtoList\": [<br/>" +
                    "  &nbsp;&nbsp;&nbsp;  {<br/>" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"id\": 6,<br/>" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"description\": \"example\",<br/>" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"imageName\": \"797ce4cc-f1e8.jpg\",<br/>" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"image(byte[])\": \"QAU/2Q==\"<br/>" +
                    "  &nbsp;&nbsp;&nbsp;}, <br/>" +
                    "  &nbsp; ],<br/>" +
                    "  &nbsp;\"totalCount\": 6<br/>" +
                    "}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Posts with image stream returned")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponsePostObject> getPosts(
            @PageableDefault (sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {
        ResponsePostObject responsePostObject =
                postService.getPageablePostsWithStream(pageable);
        return new ResponseEntity<>(responsePostObject, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all posts without image stream", nickname = "PostController.getAllPostsWithoutImage",
            notes = "Use this method for get paginated posts<br/>" +
            "Pagination example:  /post?page=2&size=2<br/>" +
            "Response: Content-Type - application/json<br/>" +
            " {<br/>" +
            " &nbsp; \"postDtoList\": [<br/>" +
            " &nbsp;&nbsp;&nbsp;  {<br/>" +
            " &nbsp;&nbsp;&nbsp;&nbsp;     \"id\": 6,<br/>" +
            " &nbsp;&nbsp;&nbsp;&nbsp;     \"description\": \"example\",<br/>" +
            " &nbsp;&nbsp;&nbsp;&nbsp;     \"imageName\": \"797ce4cc-f1e8.jpg\",<br/>" +
            " &nbsp;&nbsp;&nbsp;&nbsp;      \"downloadUri\": \"https://example.com/797ce4cc-f1e8.jpg\",<br/>" +
            " &nbsp;&nbsp;&nbsp;   }, <br/>" +
            "  &nbsp; ],<br/>" +
            " &nbsp; \"totalCount\": 6<br/>" +
            "}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Posts without image stream returned")})
    @GetMapping(value = "/light",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponsePostObject> getPostsWithoutImage(
            @PageableDefault (sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {
        ResponsePostObject responsePostObject =
                postService.getPageablePostsWithoutStream(pageable);
        return new ResponseEntity<>(responsePostObject, HttpStatus.OK);
    }
}
