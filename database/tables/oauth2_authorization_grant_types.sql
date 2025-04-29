CREATE TABLE wms.oauth2_authorization_grant_types (
    oauth2_registered_client_id varchar(255) NOT NULL,
    authorization_grant_type varchar(255) NULL,
    CONSTRAINT fkelk605vr10ift0i54poq5akyu FOREIGN KEY (oauth2_registered_client_id) REFERENCES wms.oauth2_registered_client(id) ON DELETE CASCADE
);