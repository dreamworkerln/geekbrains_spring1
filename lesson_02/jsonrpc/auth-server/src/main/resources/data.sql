-- noinspection SqlNoDataSourceInspectionForFile

-- OAUTH CREDENTIALS

-- Resource server that wanna check token, password is `respass`
-- INSERT INTO oauth_client_details (client_id, client_secret, scope, authorized_grant_types, authorities)
--  VALUES ('resource', '{bcrypt}$2a$10$5yMP6gAMsMV9RnH8SPvCzu24e67nRZU1pzPptzIVQvRV/8Sr1RX12', 'read,write', 'password,refresh_token,client_credentials', 'ROLE_RESOURCE');

-- The encrypted client_secret it `secret`
INSERT INTO oauth_client_details (client_id, client_secret, scope, authorized_grant_types, authorities, access_token_validity, refresh_token_validity)
  VALUES ('clientId', '{bcrypt}$2a$10$vCXMWCn7fDZWOcLnIEhmK.74dvK1Eh8ae2WrWlhr2ETPLoxQctN4.', 'read,write', 'password,refresh_token,client_credentials', 'ROLE_CLIENT', 600, 60000);

-- ------------------------------------------------------------------------------------------------

-- USERS CREDENTIALS

-- The encrypted password is `nooneguessthis`
INSERT INTO users (id, username, password, enabled) VALUES (1, 'admin', '{bcrypt}$2y$10$CrXT7.C1EReQ4w3yZnxQueai3Ig.s2xBDNLVJ209KdFOgJkXbJt6u', 1);
-- The encrypted password is `pass`
INSERT INTO users (id, username, password, enabled) VALUES (2, 'user', '{bcrypt}$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC', 1);
-- The encrypted password is `pass`
INSERT INTO users (id, username, password, enabled) VALUES (3, 'guest', '{bcrypt}$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC', 1);



INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) VALUES ('user', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('guest', 'ROLE_GUEST');
