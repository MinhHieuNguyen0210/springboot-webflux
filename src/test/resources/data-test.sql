create table if not exists "user"
(
    id    integer      not null
        constraint user_pk
            primary key,
    email varchar(255) not null
);

insert into "user" values (1,'a@gmail.com');
insert into "user" values (2,'b@gmail.com');
insert into "user" values (3,'c@gmail.com');

create table type if not exists
(
    type_id   integer      not null
        constraint type_pk
            primary key,
    type_name varchar(255) not null
);

insert into type values (1,"friends");
insert into type values (2,"block");
insert into type values (3,"subscribe");

create table user_relationship if not exists
(
    user_first_id  integer not null,
    user_second_id integer not null,
    type           integer not null,
    constraint pk_user_relationship
        primary key (user_first_id, user_second_id)
);

insert into user_relationship values (1,1,2)
insert into user_relationship values (1,2,1)
insert into user_relationship values (1,3,1)