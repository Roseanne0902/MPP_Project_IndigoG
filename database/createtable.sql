-- Create the database
CREATE DATABASE IF NOT EXISTS expense_tracker_indigo;

-- Use the database
USE expense_tracker_indigo;

-- Table for user roles
CREATE TABLE user_role
(
    role_id     VARCHAR(20) PRIMARY KEY,
    role_name   VARCHAR(100),
    description TEXT
);

-- Table for users
CREATE TABLE user
(
    user_id      VARCHAR(50) PRIMARY KEY,
    username     VARCHAR(100) NOT NULL,
    password     VARCHAR(100) NOT NULL,
    email        VARCHAR(100),
    phone_number VARCHAR(20),
    role_id      VARCHAR(20),
    FOREIGN KEY (role_id) REFERENCES user_role (role_id)
        ON DELETE SET NULL ON UPDATE CASCADE
);

-- Table for categories
CREATE TABLE category
(
    category_id VARCHAR(50) PRIMARY KEY,
    name        VARCHAR(100),
    description TEXT
);

-- Table for expenses
CREATE TABLE expense
(
    expense_id  VARCHAR(50) PRIMARY KEY,
    user_id     VARCHAR(50),
    amount      DECIMAL(10, 2),
    date        DATE,
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES user (user_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- Linking table for expense and category (many-to-many)
CREATE TABLE expensecategory
(
    expense_id  VARCHAR(50),
    category_id VARCHAR(50),
    PRIMARY KEY (expense_id, category_id),
    FOREIGN KEY (expense_id) REFERENCES expense (expense_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (category_id) REFERENCES category (category_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- Table for reports
CREATE TABLE report
(
    report_id  VARCHAR(50) PRIMARY KEY,
    role_id    VARCHAR(20),
    user_id    VARCHAR(50),
    start_date DATE,
    end_date   DATE,
    format     VARCHAR(50),
    FOREIGN KEY (role_id) REFERENCES user_role (role_id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user (user_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);