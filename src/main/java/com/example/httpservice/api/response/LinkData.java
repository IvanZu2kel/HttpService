package com.example.httpservice.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LinkData implements Content{
    @JsonProperty(value = "short_url")
    private String shortUrl;
}
