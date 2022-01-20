package com.example.httpservice.controller;

import com.example.httpservice.api.response.DataResponse;
import com.example.httpservice.api.response.LinkData;
import com.example.httpservice.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Tag(name = "Контроллер для работы с ссылками")
@RequestMapping("/api/link")
@RequiredArgsConstructor
public class LinkController {
    private final LinkService linkService;

    @Operation(summary = "Создание короткой ссылки с учетом времени ее жизни")
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse<LinkData>> registration(@RequestParam String link,
                                                               @RequestParam(defaultValue = "-1") long timestamp,
                                                               Principal principal) {
        return new ResponseEntity<>(linkService.createNewLink(link, timestamp, principal), HttpStatus.OK);
    }
}
