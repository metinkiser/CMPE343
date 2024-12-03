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
ADD roles VARCHAR(50); 

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
('user1', 'password1', 'Esma', 'Deniz', '555-111-1111', 'esma.deniz@example.com', '1995-01-01', '2021-05-01'),
('user2', 'password2', 'Hacer', 'Sayar', '555-222-2222', 'hacer.sayar@example.com', '1998-02-02', '2020-06-15'),
('user3', 'password3', 'Enes', 'Akyürek', '555-333-3333', 'enes.akyurek@example.com', '1990-03-03', '2022-01-10'),
('user4', 'password4', 'Zeynep', 'Özen', '555-444-4444', 'zeynep.ozen@example.com', '1999-04-04', '2019-09-05'),
('user5', 'password5', 'Berke', 'Dinç', '555-555-5555', 'berke.dinc@example.com', '1987-05-05', '2017-03-20'),
('user6', 'password6', 'Melike', 'Al', '555-666-6666', 'melike.al@example.com', '1995-06-06', '2018-11-11'),
('user7', 'password7', 'Onur', 'Oksay', '555-777-7777', 'onur.oksay@example.com', '1981-07-07', '2020-07-07'),
('user8', 'password8', 'Hilal', 'Benli', '555-888-8888', 'hilal.benli@example.com', '1996-08-08', '2022-04-20'),
('user9', 'password9', 'Burcu', 'Gülnaz', '555-999-9999', 'burcu.gulnaz@example.com', '1995-09-09', '2017-08-08'),
('user10', 'password10', 'Ahmet', 'Yırtıcı', '555-101-0101', 'ahmet.yirtici@example.com', '1997-10-10', '2018-12-12'),
('user11', 'password11', 'Nihal', 'Küçük', '555-102-0202', 'nihal.kucuk@example.com', '1995-11-11', '2021-06-01'),
('user12', 'password12', 'Buğra', 'Şahin', '555-103-0303', 'bugra.sahin@example.com', '1990-12-12', '2017-03-15'),
('user13', 'password13', 'Sümbül', 'Kaplan', '555-104-0404', 'sumbul.kaplan@example.com', '1995-01-01', '2019-01-01'),
('user14', 'password14', 'Alperen', 'Ay', '555-105-0505', 'alperen.ay@example.com', '1992-02-02', '2019-10-10'),
('user15', 'password15', 'Ceren', 'Yetkinler', '555-106-0606', 'ceren.yetkinler@example.com', '1984-03-03', '2020-05-15'),
('user16', 'password16', 'Simge', 'Gülmez', '555-107-0707', 'simge.gulmez@example.com', '1990-04-04', '2018-09-30'),
('user17', 'password17', 'Aysel', 'Şahin', '555-108-0808', 'aysel.sahin@example.com', '1991-05-05', '2020-11-20'),
('user18', 'password18', 'İbrahim', 'Geyik', '555-109-0909', 'ibrahim.geyik@example.com', '1995-06-06', '2022-02-28'),
('user19', 'password19', 'İkbal', 'Dağdelen', '555-110-1010', 'ikbal.dagdelen@example.com', '1991-07-07', '2018-08-08'),
('user20', 'password20', 'Emir', 'Özdinç', '555-111-1112', 'emir.ozdinc@example.com', '1990-08-08', '2023-03-03');

INSERT INTO employee_roles (employee_id, role_id)
VALUES
(1, 1),  -- Esma, Manager
(2, 2),  -- Hacer, Engineer
(3, 3),  -- Enes, Technician
(4, 4),  -- Zeynep, Intern
(5, 1),  -- Berke, Manager
(6, 2),  -- Melike, Engineer
(7, 3),  -- Onur, Technician
(8, 4),  -- Hilal, Intern
(9, 1),  -- Burcu, Manager
(10, 2), -- Ahmet, Engineer
(11, 3), -- Nihal, Technician
(12, 4), -- Buğra, Intern
(13, 1), -- Sümbül, Manager
(14, 2), -- Alperen, Engineer
(15, 3), -- Ceren, Technician
(16, 4), -- Simge, Intern
(17, 1), -- Aysel, Manager
(18, 2), -- İbrahim, Engineer
(19, 3), -- İkbal, Technician
(20, 4); -- Emir, Intern

CREATE TABLE roles (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL
);

INSERT INTO roles (role_name) VALUES
('Manager'),
('Engineer'),
('Technician'),
('Intern');
