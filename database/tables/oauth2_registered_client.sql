CREATE TABLE wms.oauth2_registered_client (
    id varchar(255) NOT NULL,
    client_id varchar(255) NULL,
    client_id_issued_at timestamp(6) NULL,
    client_name varchar(255) NULL,
    client_secret varchar(255) NULL,
    redirect_uris varchar(255) NULL,
    scopes varchar(255) NULL,
    token_duration int4 NULL,
    CONSTRAINT oauth2_registered_client_pkey PRIMARY KEY (id)
);