-- table: User
INSERT INTO user (id, email, name, password, account_locked, enable)
VALUES("a7d659b7-0a11-11ef-9ca2-0242ac110002", "admin@admin.com", "admin", "$2a$10$Gwu8.j65XkcS5oE6OGdIluJo5mKhdjDpdJXxvjTgyPrhmroWs/emy", 0, 1);

-- table: token
INSERT INTO token (id, token, token_type, user_id, expired, revoked)
VALUES("f38a2341-1030-48da-9432-9a2de036b39d", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBhZG1pbi5jb20iLCJpYXQiOjE3MTQ4MzIwMTIsImV4cCI6MTcxNDkxODQxMn0.afqVJmIielcJGXKzxT2TPvol6usIasJrPi399SZKRG4", "BEARER", "a7d659b7-0a11-11ef-9ca2-0242ac110002", 0, 0);

-- table: role
INSERT INTO role (id, name)
VALUES("218e2b69-0a3b-11ef-9ca2-0242ac110002", "USER");

INSERT INTO role (id, name)
VALUES("2dd0d93b-0a90-11ef-9ca2-0242ac110002", "ADMIN");