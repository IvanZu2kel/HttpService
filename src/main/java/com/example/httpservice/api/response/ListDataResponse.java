package com.example.httpservice.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListDataResponse<T extends Content>{
    Instant timestamp;
    int total;
    private List<T> data;
}
