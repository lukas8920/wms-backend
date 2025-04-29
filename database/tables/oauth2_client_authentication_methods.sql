CREATE TABLE wms.oauth2_client_authentication_methods (
    oauth2_registered_client_id varchar(255) NOT NULL,
    client_authentication_method varchar(255) NULL,
    CONSTRAINT fki3jtumbh5d80qd60r4hdoclb0 FOREIGN KEY (oauth2_registered_client_id) REFERENCES wms.oauth2_registered_client(id) ON DELETE CASCADE
);