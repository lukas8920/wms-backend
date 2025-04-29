CREATE TABLE wms.oauth2_client_authentication_methods (
    oauth2_registered_client_id varchar(100) NOT NULL,
    client_authentication_method varchar(255) NULL,
    CONSTRAINT client_authentication_methods_oauth2_registered_client_id_fkey FOREIGN KEY (oauth2_registered_client_id) REFERENCES wms.oauth2_registered_client(id)
);