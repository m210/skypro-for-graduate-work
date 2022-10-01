--liquibase formatted sql

--changeSet eradomskaya:1

CREATE TABLE users
(
    id         serial       NOT NULL PRIMARY KEY,
    first_name varchar(255) NOT NULL,
    last_name  varchar(255) NOT NULL,
    phone      varchar(255) NOT NULL,
    email      varchar(255) NOT NULL,
    password   varchar(255) NOT NULL,
    role       varchar(255) NOT NULL DEFAULT 'USER'

);


--changeSet eradomskaya:2

CREATE TABLE images
(
    pk         serial       NOT NULL PRIMARY KEY,
    data       BYTEA,
    file_path  varchar(255) NOT NULL,
    file_size  integer      NOT NULL,
    media_type varchar(255) NOT NULL

);

--changeSet eradomskaya:3

CREATE TABLE ads
(
    pk          serial       NOT NULL PRIMARY KEY,
    price       integer      NOT NULL,
    title       varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    image_id    bigint       NOT NULL REFERENCES images (pk),
    author_id   bigint       NOT NULL REFERENCES users (id)

);



--changeSet eradomskaya:4

CREATE TABLE comments
(
    pk         serial    NOT NULL PRIMARY KEY,
    author_id  bigint    NOT NULL REFERENCES users (id),
    ads_pk     bigint    NOT NULL REFERENCES ads (pk),
    created_at timestamp NOT NULL,
    text       text      NOT NULL

);

--changeSet amakarov:5

ALTER TABLE users
    ADD enabled boolean NOT NULL DEFAULT TRUE;

ALTER TABLE users
    DROP COLUMN role;

CREATE TABLE authorities
(
    username VARCHAR(50) NOT NULL,
    role VARCHAR(68) NOT NULL
);

--changeSet amakarov:6

ALTER TABLE users
    ALTER COLUMN first_name DROP NOT NULL;

ALTER TABLE users
    ALTER COLUMN last_name DROP NOT NULL;

ALTER TABLE users
    ALTER COLUMN phone DROP NOT NULL;

--changeSet eradomskaya:7

ALTER TABLE users
    RENAME COLUMN email to username;

--changeSet eradomskaya:8

ALTER TABLE authorities
    RENAME COLUMN role to authority;
