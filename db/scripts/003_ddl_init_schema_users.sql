create table if not exists users
(
    id    serial    primary    key,
    email    varchar(255),
    password text
    )