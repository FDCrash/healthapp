package com.app.health.services.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDtoWithImageStream {

    private long id;

    private String description;

    private String imageName;

    private byte[] image;

}
