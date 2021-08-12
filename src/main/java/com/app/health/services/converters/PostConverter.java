package com.app.health.services.converters;

import com.app.health.dao.entities.PostEntity;
import com.app.health.services.dto.PostDto;
import org.springframework.stereotype.Component;

@Component
public class PostConverter {

    public PostDto convert(PostEntity postEntity) {
        return PostDto.builder()
                .description(postEntity.getDescription())
                .imageUrl(postEntity.getImageUrl())
                .build();
    }

    public PostEntity convert(PostDto postDto) {
        return PostEntity.builder()
                .description(postDto.getDescription())
                .imageUrl(postDto.getImageUrl())
                .build();
    }
}
