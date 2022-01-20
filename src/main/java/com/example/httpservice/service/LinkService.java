package com.example.httpservice.service;

import com.example.httpservice.api.response.DataResponse;
import com.example.httpservice.api.response.LinkData;
import com.example.httpservice.model.Link;
import com.example.httpservice.model.Person;
import com.example.httpservice.repository.LinkRepository;
import com.example.httpservice.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final PersonRepository personRepository;
    private final LinkRepository linkRepository;

    @Value("${server.base_url}")
    private String baseUrl;

    public DataResponse<LinkData> createNewLink(String link, long timestamp, Principal principal) {
        Optional<Person> person = personRepository.findByEmail(principal.getName());
        if (link.equals("")) {
            return null;
        }
        Optional<Link> linkOptional = linkRepository.findByParentLink(link.trim().toLowerCase());
        Link savedLink;
        if (linkOptional.isPresent()) {
            return getDataResponse(linkOptional.get());
        } else {
            Link newLink = new Link()
                    .setShortUrl(getShortUrl())
                    .setVotes(0)
                    .setIsDeleted(0)
                    .setRegDate(Instant.from(Instant.now().atZone(ZoneId.systemDefault())))
                    .setLifeByDate(Instant.from(Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault())))
                    .setParentUrl(link)
                    .setPerson(person.get());
            savedLink = linkRepository.save(newLink);
        }
        return getDataResponse(savedLink);
    }

    private String getShortUrl() {
        return baseUrl + "/" + RandomStringUtils.randomAlphanumeric(4);
    }

    private DataResponse<LinkData> getDataResponse(Link link) {
        return new DataResponse<LinkData>()
                .setTimestamp(Instant.from(Instant.now().atZone(ZoneId.systemDefault())))
                .setData(getLinkData(link));
    }

    private LinkData getLinkData(Link link) {
        return new LinkData()
                .setShortUrl(link.getShortUrl());
    }
}
