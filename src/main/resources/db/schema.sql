CREATE TABLE roles (
  id serial primary key,
  name varchar(50) not null
);

CREATE TABLE persons(
    id serial primary key,
    name varchar(50) not null unique ,
    password varchar(200) not null ,
    role_id int not null references roles(id)
);

CREATE TABLE rooms (
    id serial primary key,
    name varchar(100) not null unique
);

CREATE TABLE messages(
    id serial primary key,
    text varchar(2000) not null,
    created timestamp not null default now(),
    person_id int not null references persons(id),
    room_id int not null references rooms(id)
);

INSERT INTO roles (name) VALUES ('ROLE_ADMINS'), ('ROLE_USERS');
/*credentials: admin/admin*/
INSERT INTO persons (name, password, role_id) VALUES ('admin', '$2a$10$5hou/N59ffJFJAYx7syiwOwUdRDUkma7oLLXhXlv1naxzTZptTOoe', 1);