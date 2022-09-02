create table candidate
(
    id          serial primary key,
    name        varchar(255),
    photo       bytea,
    description text,
    created     timestamp
)
