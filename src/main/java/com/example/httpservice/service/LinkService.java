package com.example.httpservice.service;

import com.example.httpservice.api.response.DataResponse;
import com.example.httpservice.api.response.LinkData;
import com.example.httpservice.api.response.ListDataResponse;
import com.example.httpservice.model.Link;
import com.example.httpservice.model.Person;
import com.example.httpservice.repository.LinkRepository;
import com.example.httpservice.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
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
                    .setView(0)
                    .setIsDeleted(0)
                    .setRegDate(Instant.from(Instant.now().atZone(ZoneId.systemDefault())))
                    .setParentUrl(link)
                    .setPerson(person.get());
            if (timestamp != -1) {
                newLink.setLifeByDate(Instant.from(Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault())));
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
        parentLinkBySmth.get().setView(parentLinkBySmth.get().getView() + 1);
        linkRepository.updateViewCount(parentLinkBySmth.get().getId(), parentLinkBySmth.get().getView());
        if (parentLinkBySmth.get().getLifeByDate() != null && !parentLinkBySmth.get().getLifeByDate().isAfter(Instant.from(Instant.now().atZone(ZoneId.systemDefault())))) {
            linkRepository.delete(parentLinkBySmth.get());
            return null;
        }
        return new ModelAndView("redirect:" + parentLinkBySmth.get().getParentUrl());
    }


    public DataResponse<LinkData> changePath(String id, String link, long timestamp) {
        Optional<Link> linkOptional = linkRepository.findByLinkId(id);
        if (linkOptional.isPresent()) {
            if (!link.trim().equals("")){
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

    public void deleteLink(String id) {
        Optional<Link> linkOptional = linkRepository.findByLinkId(id);
        if (linkOptional.isPresent()) {
            linkOptional.get().setIsDeleted(1);
            linkRepository.save(linkOptional.get());
        }
    }

    public DataResponse<LinkData> recoveryLink(String id) {
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
        return new LinkData()
                .setShortUrl(link.getShortUrl())
                .setViewCount(link.getView())
                .setId(link.getId());
    }
}
