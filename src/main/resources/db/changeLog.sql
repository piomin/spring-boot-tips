--liquibase formatted sql

--changeset piomin:1
create table tip (
  id int auto_increment,
  title varchar(255),
  description varchar(255)
);
create table person (
  id serial primary key,
  name varchar(255),
  age int
);