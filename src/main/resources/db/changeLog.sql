--liquibase formatted sql

--changeset piomin:1
create table tip (
  id serial primary key,
  title varchar(255),
  description varchar(255)
);
create table person (
  id serial primary key,
  name varchar(255),
  age int
);