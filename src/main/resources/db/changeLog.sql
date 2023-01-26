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
  age int,
  gender varchar(20),
  country varchar(50),
  city varchar(50),
  street varchar(50),
  house_number int,
  flat_number int
);