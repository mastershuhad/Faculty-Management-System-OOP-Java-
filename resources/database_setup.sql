-- Database Initialization Script for Faculty Management System (FMS)
-- Database name: home (matching lab guide)

CREATE DATABASE IF NOT EXISTS `home` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `home`;

-- 1. Users Table (For Credentials and Role Mapping)
CREATE TABLE IF NOT EXISTS `users` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(100) UNIQUE NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    `role` VARCHAR(20) NOT NULL -- 'Admin', 'Student', 'Lecturer'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Departments Table
CREATE TABLE IF NOT EXISTS `departments` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) UNIQUE NOT NULL,
    `hod` VARCHAR(100) NOT NULL,
    `degree` VARCHAR(100) NOT NULL,
    `no_of_staff` INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Degrees Table
CREATE TABLE IF NOT EXISTS `degrees` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) UNIQUE NOT NULL,
    `department` VARCHAR(100) NOT NULL,
    `no_of_students` INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Lecturers Table
CREATE TABLE IF NOT EXISTS `lecturers` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(100) NOT NULL,
    `full_name` VARCHAR(100) NOT NULL,
    `department` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) NOT NULL,
    `mobile` VARCHAR(20) NOT NULL,
    FOREIGN KEY (`username`) REFERENCES `users`(`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Students Table
CREATE TABLE IF NOT EXISTS `students` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(100) NOT NULL,
    `full_name` VARCHAR(100) NOT NULL,
    `student_id` VARCHAR(20) UNIQUE NOT NULL, -- e.g. ET/2022/011
    `degree` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) NOT NULL,
    `mobile` VARCHAR(20) NOT NULL,
    FOREIGN KEY (`username`) REFERENCES `users`(`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Courses Table
CREATE TABLE IF NOT EXISTS `courses` (
    `code` VARCHAR(20) PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `credits` INT NOT NULL,
    `lecturer_name` VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. Course Enrollments Table (Student Grades)
CREATE TABLE IF NOT EXISTS `course_enrollments` (
    `student_id` VARCHAR(20) NOT NULL,
    `course_code` VARCHAR(20) NOT NULL,
    `grade` VARCHAR(20) DEFAULT 'Pending', -- 'A+', 'A', 'B', 'C', 'D', 'Pending'
    PRIMARY KEY (`student_id`, `course_code`),
    FOREIGN KEY (`student_id`) REFERENCES `students`(`student_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`course_code`) REFERENCES `courses`(`code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. Timetable Table
CREATE TABLE IF NOT EXISTS `timetable` (
    `time_slot` VARCHAR(10) PRIMARY KEY, -- '08.00', '10.00', '01.00', '03.00'
    `monday` VARCHAR(20) DEFAULT '',
    `tuesday` VARCHAR(20) DEFAULT '',
    `wednesday` VARCHAR(20) DEFAULT '',
    `thursday` VARCHAR(20) DEFAULT '',
    `friday` VARCHAR(20) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ==========================================
-- SEED DATA (Seeded with realistic Kelaniya FCT information)
-- ==========================================

-- Seed Users
INSERT INTO `users` (`username`, `password`, `role`) VALUES
('admin', 'admin123', 'Admin'),
('sanga', 'sanga123', 'Lecturer'),
('mithali', 'mithali123', 'Lecturer'),
('sanath', 'sanath123', 'Lecturer'),
('laalitha', 'laalitha123', 'Lecturer'),
('jeewandara', 'jeewandara123', 'Lecturer'),
('hesiri', 'hesiri123', 'Lecturer'),
('dias', 'dias123', 'Lecturer'),
('anuradhi', 'anuradhi123', 'Lecturer'),
('kumar', 'kumar123', 'Student'),
('mahela', 'mahela123', 'Student'),
('lasith', 'lasith123', 'Student'),
('sana', 'sana123', 'Student'),
('bhanuka', 'bhanuka123', 'Student'),
('chamari', 'chamari123', 'Student'),
('wanindu', 'wanindu123', 'Student'),
('harshitha', 'harshitha123', 'Student'),
('suhad', 'suhad123', 'Student')
ON DUPLICATE KEY UPDATE `password`=VALUES(`password`);

-- Seed Departments
INSERT INTO `departments` (`name`, `hod`, `degree`, `no_of_staff`) VALUES
('Applied Computing', 'Dr. Laalitha S. I. Liyanage', 'Engineering Technology', 15),
('Software Engineering', 'Dr. S. R. Liyanage', 'Information Technology', 17),
('Computer Systems Engineering', 'Dr. Hesiri Dhammika Weerasinghe', 'Computer Science', 12)
ON DUPLICATE KEY UPDATE `hod`=VALUES(`hod`), `degree`=VALUES(`degree`), `no_of_staff`=VALUES(`no_of_staff`);

-- Seed Degrees
INSERT INTO `degrees` (`name`, `department`, `no_of_students`) VALUES
('Engineering Technology', 'Applied Computing', 375),
('Information Technology', 'Software Engineering', 375),
('Computer Science', 'Computer Systems Engineering', 325),
('Bio Systems Technology', 'Applied Computing', 75)
ON DUPLICATE KEY UPDATE `department`=VALUES(`department`), `no_of_students`=VALUES(`no_of_students`);

-- Seed Lecturers
INSERT INTO `lecturers` (`username`, `full_name`, `department`, `email`, `mobile`) VALUES
('sanga', 'Dr. Kumar Sanga', 'Software Engineering', 'kumars@kln.ac.lk', '0771234567'),
('mithali', 'Dr. Mithali Raj', 'Applied Computing', 'mithalir@kln.ac.lk', '0777654321'),
('sanath', 'Prof. Sanath Jayasuriya', 'Computer Systems Engineering', 'sanathj@kln.ac.lk', '0779998888'),
('laalitha', 'Dr. Laalitha S. I. Liyanage', 'Applied Computing', 'laalithal@kln.ac.lk', '0771112222'),
('jeewandara', 'Dr. A.K. Jeewandara', 'Applied Computing', 'jeewandarak@kln.ac.lk', '0772223333'),
('hesiri', 'Dr. Hesiri Dhammika Weerasinghe', 'Computer Systems Engineering', 'hesiriw@kln.ac.lk', '0773334444'),
('dias', 'Prof. N. G. J. Dias', 'Software Engineering', 'diasn@kln.ac.lk', '0774445555'),
('anuradhi', 'Dr. Anuradhi Welhenge', 'Applied Computing', 'anuradhiw@kln.ac.lk', '0775556666')
ON DUPLICATE KEY UPDATE `full_name`=VALUES(`full_name`), `department`=VALUES(`department`), `email`=VALUES(`email`), `mobile`=VALUES(`mobile`);

-- Seed Students (2022/2023 Batch representing ET, IT, CS, BST)
INSERT INTO `students` (`username`, `full_name`, `student_id`, `degree`, `email`, `mobile`) VALUES
('kumar', 'Kumar Sangakkara', 'ET/2022/011', 'Engineering Technology', 'kumars-et22011@stu.kln.ac.lk', '0123456789'),
('mahela', 'Mahela Jayawardene', 'CT/2022/011', 'Information Technology', 'mahelaj-bi22001@stu.kln.ac.lk', '0772223333'),
('lasith', 'Lasith Malinga', 'CS/2022/011', 'Computer Science', 'lasithm-cs22005@stu.kln.ac.lk', '0774445555'),
('sana', 'Sana Mir', 'BT/2022/011', 'Bio Systems Technology', 'sanam-bs22003@stu.kln.ac.lk', '0776667777'),
('bhanuka', 'Bhanuka Rajapaksa', 'BT/2022/012', 'Bio Systems Technology', 'bhanukar-bs22012@stu.kln.ac.lk', '0771112222'),
('chamari', 'Chamari Athapaththu', 'BT/2022/013', 'Bio Systems Technology', 'chamaria-bs22013@stu.kln.ac.lk', '0773334444'),
('wanindu', 'Wanindu Hasaranga', 'BT/2022/014', 'Bio Systems Technology', 'waninduh-bs22014@stu.kln.ac.lk', '0775556666'),
('harshitha', 'Harshitha Samarawickrama', 'BT/2022/015', 'Bio Systems Technology', 'harshithas-bs22015@stu.kln.ac.lk', '0777778888'),
('suhad', 'Suhad Mohamed', 'CT/2022/012', 'Information Technology', 'suhad-ct22012@stu.kln.ac.lk', '0771231234')
ON DUPLICATE KEY UPDATE `full_name`=VALUES(`full_name`), `degree`=VALUES(`degree`), `email`=VALUES(`email`), `mobile`=VALUES(`mobile`);

-- Seed Courses (Categorized by prefixes for ET, IT/CT, CS, and BST streams)
INSERT INTO `courses` (`code`, `name`, `credits`, `lecturer_name`) VALUES
-- ET (Engineering Technology) Courses - prefix ETEC
('ETEC 21062', 'OOP for ET', 2, 'Dr. Kumar Sanga'),
('ETEC 21012', 'Electronics', 2, 'Dr. Mithali Raj'),
('ETEC 11012', 'Intro to Eng Tech', 2, 'Dr. A.K. Jeewandara'),
('ETEC 21013', 'Industrial Automation', 3, 'Dr. Laalitha S. I. Liyanage'),
-- IT / CT Courses - prefix CSCI or BIT
('CSCI 21062', 'OOP', 2, 'Dr. Kumar Sanga'),
('CSCI 21052', 'Data Structures', 2, 'Dr. Kumar Sanga'),
('CSCI 21022', 'Software Engineering', 2, 'Dr. Mithali Raj'),
('CSCI 21032', 'Database Management', 2, 'Dr. Kumar Sanga'),
-- CS (Computer Science) Courses - prefix COSC
('COSC 11013', 'Programming Techniques', 3, 'Prof. N. G. J. Dias'),
('COSC 21023', 'Computer Architecture', 3, 'Dr. Hesiri Dhammika Weerasinghe'),
('COSC 21052', 'Algorithms', 2, 'Prof. Sanath Jayasuriya'),
('COSC 21042', 'Theory of Computation', 2, 'Prof. Sanath Jayasuriya'),
-- BST (Biosystems Technology) Courses - prefix BSTE
('BSTE 11012', 'General Biology', 2, 'Dr. Anuradhi Welhenge'),
('BSTE 21023', 'Food Process Engineering', 3, 'Dr. Anuradhi Welhenge')
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`), `credits`=VALUES(`credits`), `lecturer_name`=VALUES(`lecturer_name`);

-- Seed Course Enrollments (Student Grades mapped precisely to their streams)
INSERT INTO `course_enrollments` (`student_id`, `course_code`, `grade`) VALUES
-- ET Student: Kumar Sangakkara
('ET/2022/011', 'ETEC 21062', 'A+'),
('ET/2022/011', 'ETEC 21012', 'B'),
('ET/2022/011', 'ETEC 11012', 'A'),
('ET/2022/011', 'ETEC 21013', 'B+'),
-- IT Student: Mahela Jayawardene
('CT/2022/011', 'CSCI 21062', 'A'),
('CT/2022/011', 'CSCI 21052', 'A+'),
('CT/2022/011', 'CSCI 21022', 'B+'),
('CT/2022/011', 'CSCI 21032', 'A-'),
-- CS Student: Lasith Malinga
('CS/2022/011', 'COSC 11013', 'A'),
('CS/2022/011', 'COSC 21023', 'B'),
('CS/2022/011', 'COSC 21052', 'A'),
('CS/2022/011', 'COSC 21042', 'B+'),
-- BST Student: Sana Mir
('BT/2022/011', 'BSTE 11012', 'A'),
('BT/2022/011', 'BSTE 21023', 'B+'),
-- Other BST Students enrolled in Dr. Anuradhi's courses
('BT/2022/012', 'BSTE 11012', 'B'),
('BT/2022/012', 'BSTE 21023', 'Pending'),
('BT/2022/013', 'BSTE 11012', 'A-'),
('BT/2022/013', 'BSTE 21023', 'B'),
('BT/2022/014', 'BSTE 11012', 'Pending'),
('BT/2022/014', 'BSTE 21023', 'C+'),
('BT/2022/015', 'BSTE 11012', 'A+'),
('BT/2022/015', 'BSTE 21023', 'A'),
-- IT Student: Suhad Mohamed
('CT/2022/012', 'CSCI 21062', 'A'),
('CT/2022/012', 'CSCI 21052', 'B+'),
('CT/2022/012', 'CSCI 21022', 'A-'),
('CT/2022/012', 'CSCI 21032', 'B')
ON DUPLICATE KEY UPDATE `grade`=VALUES(`grade`);

-- Seed Timetable
INSERT INTO `timetable` (`time_slot`, `monday`, `tuesday`, `wednesday`, `thursday`, `friday`) VALUES
('08.00-10.00', 'CSCI 21062', 'CSCI 21052', 'CSCI 21062', 'CSCI 21052', 'CSCI 21022'),
('10.00-12.00', 'CSCI 21022', 'CSCI 21032', 'CSCI 21032', 'CSCI 21022', 'CSCI 21062'),
('12.00-13.00', 'Lunch', 'Lunch', 'Lunch', 'Lunch', 'Lunch'),
('13.00-15.00', 'CSCI 21052', 'CSCI 21062', 'CSCI 21022', 'CSCI 21032', 'CSCI 21052')
ON DUPLICATE KEY UPDATE `monday`=VALUES(`monday`), `tuesday`=VALUES(`tuesday`), `wednesday`=VALUES(`wednesday`), `thursday`=VALUES(`thursday`), `friday`=VALUES(`friday`);