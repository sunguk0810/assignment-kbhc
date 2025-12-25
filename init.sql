CREATE DATABASE kbhc;
-- kbhc 사용자에게 권한 부여 (개발 테스트용이라 권한 다 부여)
GRANT ALL PRIVILEGES ON kbhc.* TO 'kbhc'@'%';
FLUSH PRIVILEGES;