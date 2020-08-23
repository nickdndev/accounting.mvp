drop table if exists accounts;
create table accounts
(
    id      int    not null auto_increment,
    user_id int    not null,
    balance bigint not null default 0,
    version bigint not null default 0,

    primary key (id)
);