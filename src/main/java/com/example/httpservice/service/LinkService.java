package com.example.httpservice.service;

import com.example.httpservice.api.response.DataResponse;
import com.example.httpservice.api.response.LinkData;
import com.example.httpservice.api.response.ListDataResponse;
import com.example.httpservice.model.Link;
import com.example.httpservice.model.Person;
import com.example.httpservice.model.UniqueView;
import com.example.httpservice.repository.LinkRepository;
import com.example.httpservice.repository.PersonRepository;
import com.example.httpservice.repository.UniqueViewRepository;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final PersonRepository personRepository;
    private final LinkRepository linkRepository;
    private final UniqueViewRepository uniqueViewRepository;

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
                    .setView(0)
                    .setIsDeleted(0)
                    .setRegDate(Instant.from(Instant.now().atZone(ZoneId.systemDefault())))
                    .setParentUrl(link)
                    .setPerson(person.get());
            if (timestamp != -1) {
                newLink.setLifeByDate(Instant.from(Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault())));
            }
            List<Link> all = linkRepository.findAll();
            List<String> strings = all.stream().map(Link::getShortUrl).toList();
            if (strings.contains(newLink.getShortUrl())) {
                newLink.setShortUrl(newLink.getShortUrl() + plusSymbols());
            }
            savedLink = linkRepository.save(newLink);
        }
        return getDataResponse(savedLink);
    }

    public ModelAndView followTheLink(String smth) {
        Optional<Link> parentLinkBySmth = linkRepository.findParentLinkBySmth(smth);
        if (parentLinkBySmth.isEmpty()) {
            return null;
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email.matches("^(.+)@(.+)$")) {
            Optional<Person> person = personRepository.findByEmail(email);
            if (person.isPresent()) {
                UniqueView uniqueView = new UniqueView()
                        .setLink(parentLinkBySmth.get())
                        .setPerson(person.get());
                uniqueViewRepository.save(uniqueView);
            }
        } else {
            parentLinkBySmth.get().setView(parentLinkBySmth.get().getView() + 1);
            linkRepository.updateViewCount(parentLinkBySmth.get().getId(), parentLinkBySmth.get().getView());
        }
        if (parentLinkBySmth.get().getLifeByDate() != null && !parentLinkBySmth.get().getLifeByDate()
                .isAfter(Instant.from(Instant.now().atZone(ZoneId.systemDefault())))) {
            linkRepository.delete(parentLinkBySmth.get());
            return null;
        }
        return new ModelAndView("redirect:" + parentLinkBySmth.get().getParentUrl());
    }


    public DataResponse<LinkData> changePath(Long id, String link, long timestamp) {
        Optional<Link> linkOptional = linkRepository.findByLinkId(id);
        if (linkOptional.isPresent()) {
            if (!link.trim().equals("")) {
                linkOptional.get().setParentUrl(link);
                if (timestamp != -1) {
                    linkOptional.get().setLifeByDate(Instant.from(Instant.now().atZone(ZoneId.systemDefault())));
                    Link save = linkRepository.save(linkOptional.get());
                    return getDataResponse(save);
                }
                Link save = linkRepository.save(linkOptional.get());
                return getDataResponse(save);
            }
        }
        return null;
    }

    public ListDataResponse<LinkData> getAllLinks(int offset, int itemPerPage, Principal principal) {
        Pageable pageable = PageRequest.of(offset, itemPerPage);
        Person person = personRepository.findByEmail(principal.getName()).orElseThrow();
        Page<Link> linkPage = linkRepository.findAllLinkByPersonId(person.getId(), pageable);
        return getListDataResponse(linkPage, pageable);
    }

    public void deleteLink(Long id) {
        Optional<Link> linkOptional = linkRepository.findByLinkId(id);
        if (linkOptional.isPresent()) {
            linkOptional.get().setIsDeleted(1);
            linkRepository.save(linkOptional.get());
        }
    }

    public DataResponse<LinkData> recoveryLink(Long id) {
        Optional<Link> linkOptional = linkRepository.findDeletedByLinkId(id);
        if (linkOptional.isPresent()) {
            linkOptional.get().setIsDeleted(0);
            Link save = linkRepository.save(linkOptional.get());
            getDataResponse(save);
        }
        return null;
    }

    private String getShortUrl() {
        return baseUrl + "/" + RandomStringUtils.randomAlphanumeric(4, 6);
    }

    private String plusSymbols() {
        return RandomStringUtils.randomAlphanumeric(1, 2);
    }

    private DataResponse<LinkData> getDataResponse(Link link) {
        return new DataResponse<LinkData>()
                .setTimestamp(Instant.from(Instant.now().atZone(ZoneId.systemDefault())))
                .setData(getLinkData(link));
    }


    private ListDataResponse<LinkData> getListDataResponse(Page<Link> linkPage, Pageable pageable) {
        ListDataResponse<LinkData> listDataResponse = new ListDataResponse<>();
        listDataResponse
                .setTimestamp(Instant.from(Instant.now().atZone(ZoneId.systemDefault())))
                .setItemPerPage(pageable.getPageSize())
                .setOffset((int) pageable.getOffset())
                .setTotal(linkPage.getTotalPages())
                .setData(getLinkDataForList(linkPage.toList()));
        return listDataResponse;
    }

    private List<LinkData> getLinkDataForList(List<Link> links) {
        List<LinkData> linkDataList = new ArrayList<>();
        links.forEach(l -> {
            LinkData linkData = getLinkData(l);
            linkDataList.add(linkData);
        });
        return linkDataList;
    }

    private LinkData getLinkData(Link link) {
        List<UniqueView> uniqueViewList = uniqueViewRepository.findAllByLinkId(link.getId());
        Map<String, Integer> map = new HashMap<>();
        for (UniqueView uv : uniqueViewList) {
            String email = uv.getPerson().getEmail();
            map.put(email, (int) uniqueViewList.stream().filter(u -> u.getPerson().getEmail().equals(email)).count());
        }
        return new LinkData()
                .setShortUrl(link.getShortUrl())
                .setViewCount(link.getView())
                .setId(link.getId())
                .setUniqueViews(map);
    }
}
