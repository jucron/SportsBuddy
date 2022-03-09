--#Initial data to be created in each schema:

create table client (
        id bigint generated by default as identity,
        email varchar(255),
        name varchar(255),
        primary key (id)
        );
create table login_access (
        id bigint generated by default as identity,
        password varchar(255),
        username varchar(255),
        user_id bigint,
        primary key (id)
        );
create table match (
        id bigint generated by default as identity,
        date varchar(255),
        details varchar(255),
        hour varchar(255),
        location varchar(255),
        name varchar(255),
        number_of_participants integer not null,
        ownerid bigint,
        owner_name varchar(255),
        sport_name varchar(255),
        primary key (id));
create table client_participating_matches (
        user_id bigint not null,
        participating_matches_id bigint not null
        );
create table match_users_attending (
        match_id bigint not null,
        users_attending_id bigint not null
        );
create table user_favourite_sports (
        user_id bigint not null,
        favourite_sports varchar(255)
        );
alter table client_participating_matches add constraint FK_RELATIONAL_ParticipatingMatches_Matches
        foreign key (participating_matches_id) references match;
alter table client_participating_matches add constraint FK_RELATIONAL_User_Client
        foreign key (user_id) references client;
alter table login_access add constraint FK_user_client
        foreign key (user_id) references client;
alter table match_users_attending add constraint FK_RELATIONAL_UsersAttending_Client
        foreign key (users_attending_id) references client;
alter table match_users_attending add constraint FK_RELATIONAL_Match_Client
        foreign key (match_id) references match;
alter table user_favourite_sports add constraint FK_RELATIONAL_Users_Client
        foreign key (user_id) references client;
