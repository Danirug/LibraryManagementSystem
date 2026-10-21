-- Library Management System - MySQL schema and sample data
-- Run once:  mysql -u root -p < database/library_db.sql

DROP DATABASE IF EXISTS library_db;
CREATE DATABASE library_db;
USE library_db;

CREATE TABLE users (
    user_id   INT AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(50)  NOT NULL UNIQUE,
    password  CHAR(64)     NOT NULL          -- SHA-256 hash
);

CREATE TABLE books (
    book_id            VARCHAR(20)  PRIMARY KEY,
    title              VARCHAR(150) NOT NULL,
    author             VARCHAR(100) NOT NULL,
    category           VARCHAR(50)  NOT NULL,
    published_year     INT          NOT NULL,
    quantity           INT          NOT NULL,
    available_quantity INT          NOT NULL
);

CREATE TABLE members (
    member_id  VARCHAR(20)  PRIMARY KEY,
    full_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL,
    phone      CHAR(10)     NOT NULL,
    address    VARCHAR(255) NOT NULL
);

CREATE TABLE borrow_records (
    record_id   INT AUTO_INCREMENT PRIMARY KEY,
    member_id   VARCHAR(20) NOT NULL,
    book_id     VARCHAR(20) NOT NULL,
    issue_date  DATE        NOT NULL,
    due_date    DATE        NOT NULL,
    return_date DATE        NULL,           -- NULL while the book is still borrowed
    FOREIGN KEY (member_id) REFERENCES members (member_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id)   REFERENCES books (book_id)
);

-- Default login: admin / admin123
INSERT INTO users (username, password) VALUES ('admin', SHA2('admin123', 256));

INSERT INTO books VALUES
    ('B001', 'Clean Code',              'Robert C. Martin',  'Technology',  2008, 3, 2),
    ('B002', 'Sapiens',                 'Yuval Noah Harari', 'History',     2011, 2, 1),
    ('B003', 'The Alchemist',           'Paulo Coelho',      'Fiction',     1988, 4, 4),
    ('B004', 'A Brief History of Time', 'Stephen Hawking',   'Science',     1988, 2, 2),
    ('B005', 'Atomic Habits',           'James Clear',       'Non-Fiction', 2018, 3, 3);

INSERT INTO members VALUES
    ('M001', 'Alex Johnson', 'alex.johnson@example.com', '0711234567', '12 Main Street'),
    ('M002', 'Priya Sharma', 'priya.sharma@example.com', '0722345678', '45 Lake Road'),
    ('M003', 'Daniel Silva', 'daniel.silva@example.com', '0753456789', '8 Hill Avenue'),
    ('M004', 'Emma Wilson',  'emma.wilson@example.com',  '0764567890', '101 Park Lane');

INSERT INTO borrow_records (member_id, book_id, issue_date, due_date, return_date) VALUES
    ('M001', 'B001', CURDATE() - INTERVAL 10 DAY, CURDATE() + INTERVAL 4 DAY,  NULL),                         -- Borrowed
    ('M002', 'B002', CURDATE() - INTERVAL 20 DAY, CURDATE() - INTERVAL 6 DAY,  NULL),                         -- Overdue
    ('M003', 'B003', CURDATE() - INTERVAL 30 DAY, CURDATE() - INTERVAL 16 DAY, CURDATE() - INTERVAL 18 DAY);  -- Returned
