package com.app.health.services.implementation;

import com.app.health.services.converters.PostConverter;
import com.app.health.dao.repositories.PostRepository;
import com.app.health.services.CrudService;
import com.app.health.services.dto.PostDto;
import com.app.health.services.dto.PostDtoWithImageStream;
import com.app.health.services.dto.PostDtoWithoutImageStream;
import com.app.health.services.dto.response.ResponsePostObject;
import com.app.health.services.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService implements CrudService<PostDto> {

    private PostRepository postRepository;
    private PostConverter postConverter;
    private FileUtil fileUtil;

    @Autowired
    public PostService(PostRepository postRepository, PostConverter postConverter, FileUtil fileUtil) {
        this.postRepository = postRepository;
        this.postConverter = postConverter;
        this.fileUtil = fileUtil;
    }


    @Override
    public String add(PostDto postDto) {
        postDto.setImageUrl(fileUtil.uploadFileAndGetPath(postDto.getFile()));
        return String.valueOf(postRepository.save(postConverter.convert(postDto)).getId());
    }

    @Override
    public List<PostDto> getAll() {
        return postRepository.findAll().stream()
                .map(postEntity -> postConverter.convert(postEntity))
                .collect(Collectors.toList());
    }

    public List<PostDtoWithImageStream> getAllPostsWithImageStream(Pageable pageable) {

        return postRepository.findAll(pageable).stream()
                .map(postEntity -> PostDtoWithImageStream.builder()
                        .id(postEntity.getId())
                        .description(postEntity.getDescription())
                        .image(fileUtil.getStreamOfImage(postEntity.getImageUrl()))
                        .imageName(postEntity.getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    public ResponsePostObject<PostDtoWithImageStream> getPageablePostsWithStream(Pageable pageable) {
        ResponsePostObject<PostDtoWithImageStream> responsePostObject = new ResponsePostObject();
        responsePostObject.setPostDtoList(getAllPostsWithImageStream(pageable));
        responsePostObject.setTotalCount(postRepository.count());
        return responsePostObject;
    }

    public ResponsePostObject<PostDtoWithoutImageStream> getPageablePostsWithoutStream(Pageable pageable) {
        ResponsePostObject<PostDtoWithoutImageStream> responsePostObject = new ResponsePostObject();
        responsePostObject.setPostDtoList(getAllPostsWithoutImageStream(pageable));
        responsePostObject.setTotalCount(postRepository.count());
        return responsePostObject;
    }

    public List<PostDtoWithoutImageStream> getAllPostsWithoutImageStream(Pageable pageable) {
        return postRepository.findAll(pageable).stream()
                .map(postEntity -> PostDtoWithoutImageStream.builder()
                        .id(postEntity.getId())
                        .description(postEntity.getDescription())
                        .imageName(postEntity.getImageUrl())
                        .downloadUri(
                                ServletUriComponentsBuilder.fromCurrentContextPath()
                                        .path("/image/")
                                        .path(postEntity.getImageUrl())
                                        .toUriString())
                        .build())
                .collect(Collectors.toList());
    }
}
