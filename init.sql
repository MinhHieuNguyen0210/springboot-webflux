create table if not exists "user"
(
    id    integer      not null
        constraint user_pk
            primary key,
    email varchar(255) not null
);

insert into "user" values (1,'lmason0@hud.gov');
insert into "user" values (2,'sfursse1@icio.us');
insert into "user" values (3,'ljouandet2@freewebs.com');
insert into "user" values (4,'rhinchshaw3@google.com.br');
insert into "user" values (5,'jenny@gmail.com');
insert into "user" values (6,'nmhieu@gmail.com');

create table if not exists type
(
    type_id   integer      not null
        constraint type_pk
            primary key,
    type_name varchar(255) not null
);

insert into type values (1,'friend');
insert into type values (2,'block');
insert into type values (3,'subscribe');

create table  if not exists user_relationship
(
    user_first_id  integer not null,
    user_second_id integer not null,
    type           integer not null,
    constraint pk_user_relationship
        primary key (user_first_id, user_second_id)
);

insert into user_relationship values (1,1,2);
insert into user_relationship values (1,2,1);
insert into user_relationship values (1,3,1);