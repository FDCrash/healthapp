package com.app.health.services.dto.response;

import com.app.health.services.dto.PostDtoWithImageStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePostObjectWithImageStream {

    private List<PostDtoWithImageStream> postDtoWithoutImageStream;

    long totalCount = 0;
}
