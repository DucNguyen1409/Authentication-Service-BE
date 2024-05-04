-- table: User
INSERT INTO user (id, email, name, password, role)
VALUES("a7d659b7-0a11-11ef-9ca2-0242ac110002", "admin@admin.com", "admin", "$2a$10$Gwu8.j65XkcS5oE6OGdIluJo5mKhdjDpdJXxvjTgyPrhmroWs/emy", "ADMIN");

-- table: token
INSERT INTO token (id, token, token_type, user_id, expired, revoked)
VALUES("f38a2341-1030-48da-9432-9a2de036b39d", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBhZG1pbi5jb20iLCJpYXQiOjE3MTQ4MzIwMTIsImV4cCI6MTcxNDkxODQxMn0.afqVJmIielcJGXKzxT2TPvol6usIasJrPi399SZKRG4", "BEARER", "a7d659b7-0a11-11ef-9ca2-0242ac110002", 0, 0);