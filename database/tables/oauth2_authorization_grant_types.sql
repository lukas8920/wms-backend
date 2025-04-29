CREATE TABLE wms.oauth2_authorization_grant_types (
    oauth2_registered_client_id varchar(100) NOT NULL,
    authorization_grant_type varchar(255) NULL,
    CONSTRAINT authorization_grant_types_oauth2_registered_client_id_fkey FOREIGN KEY (oauth2_registered_client_id) REFERENCES wms.oauth2_registered_client(id)
);