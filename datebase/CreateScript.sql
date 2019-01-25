-- Table: public.currency

-- DROP TABLE public.currency;

CREATE TABLE public.currency
(
    id bigint NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    description text COLLATE pg_catalog."default" NOT NULL,
    value double precision NOT NULL,
    last_update timestamp with time zone,
    CONSTRAINT currency_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.currency
    OWNER to postgres;