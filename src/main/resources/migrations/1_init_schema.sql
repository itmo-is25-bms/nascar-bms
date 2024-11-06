create table events
(
    id             text primary key,
    name           text,
    status         text                                                    not null,
    passcode       text                                                    not null,
    start_datetime timestamptz                                             not null,
    created_by     text                                                    not null,
    created_at     timestamptz default timezone('utc', now())::timestamptz not null,
    updated_by     text                                                    not null,
    updated_at     timestamptz default timezone('utc', now())::timestamptz not null
);

create table bars
(
    id         text primary key,
    name       text                                                    not null,
    address    text                                                    not null,
    created_by text                                                    not null,
    created_at timestamptz default timezone('utc', now())::timestamptz not null,
    updated_by text                                                    not null,
    updated_at timestamptz default timezone('utc', now())::timestamptz not null
);
create unique index unq_bars_name_address on bars (name, address);

create table event_participants
(
    id         text primary key,
    event_id   text                                                    not null,
    user_id    text                                                    not null,
    joined_at  timestamptz default timezone('utc', now())::timestamptz not null,
    created_by text                                                    not null,
    created_at timestamptz default timezone('utc', now())::timestamptz not null,

    constraint fk_event_participants_events foreign key (event_id) references events (id)
);
create unique index unq_event_participants_event_id_user_id on event_participants (event_id, user_id);

create table event_bars
(
    id         text primary key,
    event_id   text                                                    not null,
    bar_id     text                                                    not null,
    created_by text                                                    not null,
    created_at timestamptz default timezone('utc', now())::timestamptz not null,

    constraint fk_event_bars_events foreign key (event_id) references events (id),
    constraint fk_event_bars_bars foreign key (bar_id) references bars (id)
);
create unique index unq_event_bars_event_id_bar_id on event_bars (event_id, bar_id);

create table bar_reviews
(
    id         text primary key,
    event_id   text                                                    not null,
    bar_id     text                                                    not null,
    score      integer                                                 not null,
    comment    text,
    created_by text                                                    not null,
    created_at timestamptz default timezone('utc', now())::timestamptz not null,

    constraint fk_bar_reviews_events foreign key (event_id) references events (id),
    constraint fk_bar_reviews_bars foreign key (bar_id) references bars (id)
);
create unique index unq_bar_reviews_event_id_bar_id_user_id on bar_reviews (event_id, bar_id, created_by);

create table receipts
(
    id           text primary key,
    receipt_link text                                                    not null,
    created_by   text                                                    not null,
    created_at   timestamptz default timezone('utc', now())::timestamptz not null
);

create table event_receipts
(
    id         text primary key,
    event_id   text                                                    not null,
    bar_id     text                                                    not null,
    receipt_id text                                                    not null,
    created_by text                                                    not null,
    created_at timestamptz default timezone('utc', now())::timestamptz not null,

    constraint fk_event_receipts_events foreign key (event_id) references events (id),
    constraint fk_event_receipts_bars foreign key (bar_id) references bars (id),
    constraint fk_event_receipts_receipts foreign key (receipt_id) references receipts (id)
);
create unique index unq_event_receipts_receipt_id on event_receipts (receipt_id);