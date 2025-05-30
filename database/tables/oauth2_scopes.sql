CREATE TABLE wms.oauth2_scopes (
    oauth2_registered_client_id varchar(255) NOT NULL,
    "scope" varchar(255) NULL,
    CONSTRAINT fkelk605vr10ift0i54poq5akyz FOREIGN KEY (oauth2_registered_client_id) REFERENCES wms.oauth2_registered_client(id) ON DELETE CASCADE
);