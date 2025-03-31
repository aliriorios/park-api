INSERT INTO users (id, username, password, role) VALUES
(100, 'ana@gmail.com', '$2a$12$FqgCHaIfbdV5zdJ7i8NVEOL1XMybVlH9L3Kt3Owb1ED1NFKqOCxyO', 'ROLE_ADMIN'),
(101, 'bia@gmail.com', '$2a$12$FqgCHaIfbdV5zdJ7i8NVEOL1XMybVlH9L3Kt3Owb1ED1NFKqOCxyO', 'ROLE_CLIENT'),
(102, 'bob@gmail.com', '$2a$12$FqgCHaIfbdV5zdJ7i8NVEOL1XMybVlH9L3Kt3Owb1ED1NFKqOCxyO', 'ROLE_CLIENT');

INSERT INTO parking_spots (id, spot_code, status) VALUES
(10, 'A-01', 'FREE'),
(20, 'A-02', 'FREE'),
(30, 'A-03', 'OCCUPIED'),
(40, 'A-04', 'FREE');
