package com.example.httpservice.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataResponse<T extends Content>{
    Instant timestamp;
    private T data;
}
