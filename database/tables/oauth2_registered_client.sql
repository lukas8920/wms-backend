CREATE TABLE wms.oauth2_registered_client (
    id varchar(100) NOT NULL,
    client_id varchar(100) NOT NULL,
    client_id_issued_at timestamp NULL,
    client_secret varchar(200) NULL,
    client_secret_expires_at timestamp NULL,
    redirect_uris varchar(1000) NULL,
    scopes varchar(1000) NULL,
    token_duration int4 NOT NULL,
    post_logout_redirect_uris varchar(1000) NULL,
    client_name varchar(200) NULL,
    CONSTRAINT oauth2_registered_client_pkey PRIMARY KEY (id)
);