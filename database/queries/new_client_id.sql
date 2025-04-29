INSERT INTO wms.oauth2_registered_client (
    id,
    client_id,
    client_secret,
    redirect_uris,
    token_duration
)
VALUES (
           'c4d53a1c-95e3-4f04-8a4d-b672dc02c9ef',
           'test-client',
           '$2a$10$YVcZQoZbqWfxvJw8QTlXmOMkeAZSoDUvBY46NuKUpK9HKCbwbuZ7e',
           'http://localhost',
           60
       );
insert into wms.oauth2_authorization_grant_types (
    oauth2_registered_client_id,
    authorization_grant_type
)
values (
           'c4d53a1c-95e3-4f04-8a4d-b672dc02c9ef',
           'client_credentials'
       );
insert into wms.oauth2_client_authentication_methods (
    oauth2_registered_client_id,
    client_authentication_method
)
values (
           'c4d53a1c-95e3-4f04-8a4d-b672dc02c9ef',
           'client_secret_basic'
       );
insert into wms.oauth2_scopes (
    oauth2_registered_client_id,
    "scope"
)
values (
           'c4d53a1c-95e3-4f04-8a4d-b672dc02c9ef',
           'read'
       )