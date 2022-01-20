drop table if exists persons;
drop table if exists links;

CREATE TABLE persons
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    email      VARCHAR(255)          NOT NULL,
    password   VARCHAR(255)          NOT NULL,
    reg_date   datetime              NOT NULL,
    `role`     VARCHAR(255)          NOT NULL,
    is_deleted INT                   NOT NULL,
    CONSTRAINT pk_persons PRIMARY KEY (id)
);

CREATE TABLE links
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    parent_url    VARCHAR(255)          NULL,
    short_url     VARCHAR(255)          NULL,
    view          INT                   NULL,
    is_deleted    INT                   NULL,
    reg_date      datetime              NULL,
    life_for_date datetime              NULL,
    person_id     BIGINT                NULL,
    CONSTRAINT pk_links PRIMARY KEY (id)
);

ALTER TABLE links
    ADD CONSTRAINT FK_LINKS_ON_PERSON FOREIGN KEY (person_id) REFERENCES persons (id);

CREATE TABLE unique_views
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    person_id BIGINT                NULL,
    link_id   BIGINT                NULL,
    CONSTRAINT pk_unique_views PRIMARY KEY (id)
);

ALTER TABLE unique_views
    ADD CONSTRAINT FK_UNIQUE_VIEWS_ON_LINK FOREIGN KEY (link_id) REFERENCES links (id);

ALTER TABLE unique_views
    ADD CONSTRAINT FK_UNIQUE_VIEWS_ON_PERSON FOREIGN KEY (person_id) REFERENCES persons (id);