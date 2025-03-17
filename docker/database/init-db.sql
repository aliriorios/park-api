-- Granting permissions
GRANT ALL PRIVILEGES ON `park-api-db`.* TO 'alirio-dev'@'%';

-- DATABASE TO TEST
CREATE DATABASE IF NOT EXISTS `park-api-db-test`;
GRANT ALL PRIVILEGES ON `park-api-db-test`.* TO 'alirio-dev'@'%';

-- Apply
FLUSH PRIVILEGES;