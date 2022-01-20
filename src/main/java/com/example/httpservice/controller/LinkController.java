package com.example.httpservice.controller;

import com.example.httpservice.api.response.DataResponse;
import com.example.httpservice.api.response.LinkData;
import com.example.httpservice.api.response.ListDataResponse;
import com.example.httpservice.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
@Tag(name = "Контроллер для работы с ссылками")
@RequestMapping("")
@RequiredArgsConstructor
public class LinkController {
    private final LinkService linkService;

    @Operation(summary = "Перейти по короткой ссылке")
    @GetMapping("{smth}")
    public ModelAndView getShortUrl(@PathVariable String smth) {
        return linkService.followTheLink(smth);
    }

    @Operation(summary = "Создание короткой ссылки с учетом времени ее жизни")
    @PostMapping("/api/link/create")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse<LinkData>> registration(@RequestParam String link,
                                                               @RequestParam(defaultValue = "-1") long timestamp,
                                                               Principal principal) {
        return new ResponseEntity<>(linkService.createNewLink(link, timestamp, principal), HttpStatus.OK);
    }

    @Operation(summary = "Получение списка коротких ссылок")
    @GetMapping("/api/link")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ListDataResponse<LinkData>> getAllLinks(@RequestParam(required = false, defaultValue = "0") int offset,
                                                                  @RequestParam(required = false, defaultValue = "20") int itemPerPage,
                                                                  Principal principal) {
        return new ResponseEntity<>(linkService.getAllLinks(offset, itemPerPage, principal), HttpStatus.OK);
    }

    @Operation(summary = "Изменение пути в короткой ссылке")
    @PutMapping("/api/link/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse<LinkData>> changePath(@PathVariable String id,
                                                             @RequestParam String link,
                                                             @RequestParam(defaultValue = "-1") long timestamp) {
        return new ResponseEntity<>(linkService.changePath(id, link, timestamp), HttpStatus.OK);
    }

    @Operation(summary = "Удаление ссылки")
    @DeleteMapping("/api/link/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public void deleteLink(@PathVariable String id) {
        linkService.deleteLink(id);
    }

    @Operation(summary = "Восстановление ссылки")
    @PutMapping("/api/link/{id}/recovery")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse<LinkData>> recoveryLink(@PathVariable String id) {
        return new ResponseEntity<>(linkService.recoveryLink(id), HttpStatus.OK);
    }
}
