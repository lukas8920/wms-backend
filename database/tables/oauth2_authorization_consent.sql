CREATE TABLE wms.oauth2_authorization_consent (
    registered_client_id varchar(100) NOT NULL,
    principal_name varchar(200) NOT NULL,
    authorities varchar(1000) NOT NULL,
    CONSTRAINT oauth2_authorization_consent_pk PRIMARY KEY (registered_client_id, principal_name)
);