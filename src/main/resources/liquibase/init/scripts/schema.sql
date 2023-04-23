CREATE SEQUENCE IF NOT EXISTS user_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS role_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS reservation_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS restaurant_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS review_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS image_id_seq START WITH 1;

CREATE TABLE IF NOT EXISTS users
(
    id         int PRIMARY KEY DEFAULT (nextval('user_id_seq')),
    password   varchar(256),
    email      varchar(256),
    first_name varchar(128),
    last_name  varchar(128)
);

CREATE TABLE IF NOT EXISTS roles
(
    id   int PRIMARY KEY DEFAULT (nextval('role_id_seq')),
    name text
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id int REFERENCES users (id),
    role_id int REFERENCES roles (id),
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS restaurants
(
    id                        int PRIMARY KEY DEFAULT (nextval('restaurant_id_seq')),
    name                      text,
    city                      text,
    address                   text,
    description               text,
    latitude                  numeric(3, 10), -- todo посмотреть как это можно хранить и мб притащить сюда постгис
    longitude                 numeric(3, 10), -- todo посмотреть как это можно хранить и мб притащить сюда постгис
    open_from                 time,
    open_till                 time,
    scheme_link               text,
    min_reservation_time_mins int,
    max_reservation_time_mins int CHECK ( max_reservation_time_mins >= restaurants.min_reservation_time_mins )
);


CREATE TABLE IF NOT EXISTS reservations
(
    id            int PRIMARY KEY DEFAULT (nextval('reservation_id_seq')),
    booked_from   timestamp with time zone,
    booked_to     timestamp with time zone,
    booked_by     int references users (id),
    restaurant_id int references restaurants (id)
);

CREATE TABLE IF NOT EXISTS images
(
    id              int PRIMARY KEY DEFAULT (nextval('image_id_seq')),
    type            text,
    original_name   text,
    filesystem_name text,
    link            text,
    restaurant_id   int references restaurants (id)
);

CREATE TABLE IF NOT EXISTS reviews
(
    id            int PRIMARY KEY DEFAULT (nextval('review_id_seq')),
    author_id     int references users (id),
    restaurant_id int references restaurants (id),
    review_text   text,
    rating        int
);

CREATE TABLE IF NOT EXISTS reviews_images
(
    review_id int references reviews (id),
    image_id  int references images (id)
)
