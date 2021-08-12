package com.app.health.services.dto.response;

import com.app.health.services.dto.PostDtoWithoutImageStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePostObject <T> {

    private List<T> postDtoList;

    long totalCount = 0;

}
