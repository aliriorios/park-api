insert into users (id, username, password, role) values
(100, 'ana@gmail.com', '$2a$12$FqgCHaIfbdV5zdJ7i8NVEOL1XMybVlH9L3Kt3Owb1ED1NFKqOCxyO', 'ROLE_ADMIN'),
(101, 'bia@gmail.com', '$2a$12$FqgCHaIfbdV5zdJ7i8NVEOL1XMybVlH9L3Kt3Owb1ED1NFKqOCxyO', 'ROLE_CLIENT'),
(102, 'bob@gmail.com', '$2a$12$FqgCHaIfbdV5zdJ7i8NVEOL1XMybVlH9L3Kt3Owb1ED1NFKqOCxyO', 'ROLE_CLIENT');

insert into clients (id, name, cpf, id_user) values
(21, 'Biatriz Rodrigues', '09191773016', 101),
(22, 'Rodrigo Silva', '98401203015', 102);

insert into parking_spots (id, spot_code, status) values
(100, 'A-01', 'OCCUPIED'),
(200, 'A-02', 'OCCUPIED'),
(300, 'A-03', 'OCCUPIED'),
(400, 'A-04', 'OCCUPIED'),
(500, 'A-05', 'OCCUPIED');

insert into client_parking_spot (receipt_number, licence_plate, manufacturer, model, color, check_in, id_client, id_parking_spot) values
('20230313-101300', 'FIT-1010', 'FIAT', 'PALIO', 'GREEN', '2023-03-13 10:15:00', 22, 100),
('20230314-101400', 'SIE-1020', 'FIAT', 'SIENA', 'WHITE', '2023-03-14 10:15:00', 21, 200),
('20230315-101500', 'FIT-1030', 'FIAT', 'PALIO', 'GREEN', '2023-03-14 10:15:00', 22, 300),
('20230316-101600', 'SIE-1040', 'FIAT', 'SIENA', 'GREEN', '2023-03-14 10:15:00', 21, 400),
('20230317-101700', 'SIE-1050', 'FIAT', 'SIENA', 'GREEN', '2023-03-14 10:15:00', 22, 500);