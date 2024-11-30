CREATE DATABASE firm_management;
USE firm_management;
SHOW TABLES;
USE firm_management;
SHOW TABLES;
SELECT TABLE_NAME, TABLE_SCHEMA
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = 'firm_management';

DESCRIBE employees;

SELECT * FROM employees;
SELECT * FROM employee_roles;
SELECT * FROM roles;

DROP TABLE IF EXISTS roles;

SELECT COUNT(*) AS toplam_satir FROM employees;


USE firm_management;
SELECT * FROM employees;

ALTER TABLE employees
ADD roles VARCHAR(50); -- "VARCHAR(50)" sütun uzunluğunu ihtiyaçlarınıza göre ayarlayabilirsiniz

SET SQL_SAFE_UPDATES = 0;

UPDATE employees
SET roles = (CASE FLOOR(1 + (RAND() * 3))
    WHEN 1 THEN 'Manager'
    WHEN 2 THEN 'Engineer'
    WHEN 3 THEN 'Operator'
END)
WHERE employee_id BETWEEN 1 AND 20;

ALTER TABLE employees
DROP COLUMN roles;

CREATE TABLE roles (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL
);

CREATE TABLE employees (
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_no VARCHAR(20),
    email VARCHAR(100),
    date_of_birth DATE,
    date_of_start DATE
);

CREATE TABLE employee_roles (
    employee_id INT,
    role_id INT,
    PRIMARY KEY (employee_id, role_id),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

INSERT INTO roles (role_name) VALUES
('Manager'),
('Engineer'),
('Technician'),
('Intern');

INSERT INTO employees (username, password, first_name, last_name, phone_no, email, date_of_birth, date_of_start)
VALUES
('user1', 'password1', 'Ahmet', 'Yılmaz', '555-111-1111', 'ahmet.yilmaz@example.com', '1990-01-01', '2020-05-01'),
('user2', 'password2', 'Mehmet', 'Kaya', '555-222-2222', 'mehmet.kaya@example.com', '1988-02-02', '2019-06-15'),
('user3', 'password3', 'Fatma', 'Demir', '555-333-3333', 'fatma.demir@example.com', '1995-03-03', '2021-01-10'),
('user4', 'password4', 'Zeynep', 'Çelik', '555-444-4444', 'zeynep.celik@example.com', '1993-04-04', '2020-09-05'),
('user5', 'password5', 'Ali', 'Şahin', '555-555-5555', 'ali.sahin@example.com', '1985-05-05', '2018-03-20'),
('user6', 'password6', 'Elif', 'Aydın', '555-666-6666', 'elif.aydin@example.com', '1992-06-06', '2019-11-11'),
('user7', 'password7', 'Hüseyin', 'Yıldız', '555-777-7777', 'huseyin.yildiz@example.com', '1991-07-07', '2017-07-07'),
('user8', 'password8', 'Ayşe', 'Eren', '555-888-8888', 'ayse.eren@example.com', '1994-08-08', '2021-04-20'),
('user9', 'password9', 'Emre', 'Arslan', '555-999-9999', 'emre.arslan@example.com', '1990-09-09', '2018-08-08'),
('user10', 'password10', 'Deniz', 'Kurt', '555-101-0101', 'deniz.kurt@example.com', '1987-10-10', '2019-12-12'),
('user11', 'password11', 'Merve', 'Bulut', '555-102-0202', 'merve.bulut@example.com', '1996-11-11', '2020-06-01'),
('user12', 'password12', 'Can', 'Güneş', '555-103-0303', 'can.gunes@example.com', '1989-12-12', '2016-03-15'),
('user13', 'password13', 'Serkan', 'Taş', '555-104-0404', 'serkan.tas@example.com', '1997-01-01', '2022-01-01'),
('user14', 'password14', 'Buse', 'Deniz', '555-105-0505', 'buse.deniz@example.com', '1991-02-02', '2020-10-10'),
('user15', 'password15', 'Gökhan', 'Kılıç', '555-106-0606', 'gokhan.kilic@example.com', '1986-03-03', '2018-05-15'),
('user16', 'password16', 'Esra', 'Kaya', '555-107-0707', 'esra.kaya@example.com', '1994-04-04', '2019-09-30'),
('user17', 'password17', 'Burak', 'Koç', '555-108-0808', 'burak.koc@example.com', '1993-05-05', '2017-11-20'),
('user18', 'password18', 'Selin', 'Polat', '555-109-0909', 'selin.polat@example.com', '1992-06-06', '2021-02-28'),
('user19', 'password19', 'Ece', 'Er', '555-110-1010', 'ece.er@example.com', '1990-07-07', '2019-08-08'),
('user20', 'password20', 'Yasin', 'Çiftçi', '555-111-1112', 'yasin.ciftci@example.com', '1988-08-08', '2022-03-03');

INSERT INTO employee_roles (employee_id, role_id)
VALUES
(1, 1),  -- Ahmet, Manager
(2, 2),  -- Mehmet, Engineer
(3, 3),  -- Fatma, Technician
(4, 4),  -- Zeynep, Intern
(5, 1),  -- Ali, Manager
(6, 2),  -- Elif, Engineer
(7, 3),  -- Hüseyin, Technician
(8, 4),  -- Ayşe, Intern
(9, 1),  -- Emre, Manager
(10, 2), -- Deniz, Engineer
(11, 3), -- Merve, Technician
(12, 4), -- Can, Intern
(13, 1), -- Serkan, Manager
(14, 2), -- Buse, Engineer
(15, 3), -- Gökhan, Technician
(16, 4), -- Esra, Intern
(17, 1), -- Burak, Manager
(18, 2), -- Selin, Engineer
(19, 3), -- Ece, Technician
(20, 4); -- Yasin, Intern

CREATE TABLE roles (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL
);

INSERT INTO roles (role_name) VALUES
('Manager'),
('Engineer'),
('Technician'),
('Intern');
