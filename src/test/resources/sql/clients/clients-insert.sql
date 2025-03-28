INSERT INTO users (id, username, password, role) VALUES
(100, 'ana@gmail.com', '$2a$12$FqgCHaIfbdV5zdJ7i8NVEOL1XMybVlH9L3Kt3Owb1ED1NFKqOCxyO', 'ROLE_ADMIN'),
(101, 'bia@gmail.com', '$2a$12$FqgCHaIfbdV5zdJ7i8NVEOL1XMybVlH9L3Kt3Owb1ED1NFKqOCxyO', 'ROLE_CLIENT'),
(102, 'bob@gmail.com', '$2a$12$FqgCHaIfbdV5zdJ7i8NVEOL1XMybVlH9L3Kt3Owb1ED1NFKqOCxyO', 'ROLE_CLIENT'),
(103, 'toby@gmail.com', '$2a$12$FqgCHaIfbdV5zdJ7i8NVEOL1XMybVlH9L3Kt3Owb1ED1NFKqOCxyO', 'ROLE_CLIENT');

INSERT INTO clients (id, name, cpf, id_user) VALUES
(10, 'Bianca Silva', '69795308521', '101'),
(20, 'Roberto Gomes', '07216136594', '102');
