package com.app.health.controllers;

import com.app.health.services.utils.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("/image")
public class ImageController {

    private FileUtil fileUtil;

    @Autowired
    public ImageController(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    @ApiOperation(value = "Download image", nickname = "PostController.downloadImage",
            notes = "Use this method for download image")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Image downloaded")})
    @GetMapping(value = "download/{filename:.*}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Object> downloadImage(@PathVariable String filename) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(fileUtil.getStreamOfImage(filename));
    }

    @ApiOperation(value = "Get image", nickname = "PostController.getImage",
            notes = "Use this method for getting image as static content format - .jpg")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Image is got")})
    @GetMapping(value = "/{filename:.*}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {

        return new ResponseEntity<>(fileUtil.getStreamOfImage(filename), HttpStatus.OK);
    }
}
