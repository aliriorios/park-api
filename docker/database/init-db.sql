-- Granting permissions
GRANT ALL PRIVILEGES ON `park-api-db`.* TO 'alirio-dev'@'%';
GRANT ALL PRIVILEGES ON `park-api-db-test`.* TO 'alirio-dev'@'%';

-- Apply
FLUSH PRIVILEGES;