create table post
(
    id          serial primary key,
    name        varchar(255),
    visible     boolean,
    city        type_city,
    description text,
    created     timestamp
)