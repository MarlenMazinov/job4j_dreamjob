create table if not exists post
(
    id          serial primary key,
    name        varchar(255),
    visible     boolean,
    city_id     int,
    description text,
    created     timestamp
)