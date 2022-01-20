package com.example.httpservice.api.response;

import com.example.httpservice.model.UniqueView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkData implements Content{
    private Long id;
    @JsonProperty(value = "short_url")
    private String shortUrl;
    @JsonProperty(value = "view_count")
    private int viewCount;
    @JsonProperty(value = "unique_views")
    private Map<String, Integer> uniqueViews;
}
