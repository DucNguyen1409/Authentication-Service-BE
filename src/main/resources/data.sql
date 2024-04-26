INSERT INTO setting VALUES(UUID(), "config.jwt.secret.key", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
INSERT INTO setting VALUES(UUID(), "config.mail.passwd", "bW5yZyB0ZHZ6IHRxZ2EgZGJ1cQ==");

-- table: User
INSERT INTO user (id, email, name, passwd, role)
VALUES(UUID(), "admin@mail.com", "admin", "$2a$10$VmJLTHTg1CBxv8o6HUA3ZOWQ4Pnyg6E7PYcelYM2kowkTSJlHEV4O", "ADMIN");