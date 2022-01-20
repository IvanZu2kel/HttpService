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
    parent_url    VARCHAR(255)          NOT NULL,
    short_url     VARCHAR(255)          NOT NULL,
    votes         INT                   NULL,
    is_deleted    INT                   NOT NULL,
    reg_date      datetime              NOT NULL,
    life_for_date datetime              NULL,
    person_id     BIGINT                NOT NULL,
    CONSTRAINT pk_links PRIMARY KEY (id)
);

ALTER TABLE links
    ADD CONSTRAINT FK_LINKS_ON_PERSON FOREIGN KEY (person_id) REFERENCES persons (id);