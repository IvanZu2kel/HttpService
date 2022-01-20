package com.example.httpservice.api.response;

import com.example.httpservice.model.Link;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
public class AuthData implements Content{
    private Long id;
    private String email;
}
